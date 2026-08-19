
package com.cgnpc.bbxpark.restaurant.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.analysis.ExcelReadExecutor;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.cgnpc.bbxpark.settings.dto.model.DictItemModel;
import com.cgnpc.bbxpark.settings.service.impl.DictServiceImpl;
import com.cgnpc.bbxpark.common.utils.CommentFileUtils;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesImportReturnModel;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesScheduleImortReturnModel;
import com.cgnpc.bbxpark.restaurant.dto.model.DishesTypeModel;
import com.cgnpc.bbxpark.restaurant.dto.model.RestaurantTimeModel;
import com.cgnpc.bbxpark.restaurant.dto.param.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;



@Slf4j
@Service
public class DishesScheduleServiceApp {
    private static final long FILE_SIZE = 5;
    /**
     * http客户端.
     */

    /**
     * 字典查询feign
     */
    @Autowired
    private DictServiceImpl dictService;


    @Autowired
    private IDishesScheduleService dishesScheduleService;
    @Autowired
    private IRestaurantTimeService restaurantTimeService;


    /**
     * 移动端-获取菜品类型列表.
     *
     * @Param param 菜品类型查询条件
     * @Return 菜品类型信息列表
     */
    public List<DishesTypeModel> listType(DishesTypeListParam param) {
        List<String> valueList = dishesScheduleService.listType(param);
        //查询字典集合
        /*DictItemListParam param1 = new DictItemListParam();
        param1.setDictTypeCode("foodCategory");
        List<DictItemModel> itemList = dictItemFeignClient.list(param1).getBody().getResult();*/
        List<DictItemModel> itemList = dictService.findItemsByDictType("foodCategory");
        //组合数据
        return itemList.stream().filter(dictItemModel -> valueList.contains(dictItemModel.getValue())).map(dictItemModel -> {
            DishesTypeModel model = new DishesTypeModel();
            model.setLabel(dictItemModel.getLabel());
            model.setValue(dictItemModel.getValue());
            return model;
        }).collect(Collectors.toList());
    }

    /**
     * 菜品周排导入
     * @return 错误信息
     */
    public List<DishesScheduleImortReturnModel> importSchedule(MultipartFile file, String params) {
        //文件格式校验
        CommentFileUtils.uploadVerify(FILE_SIZE, file);
        //参线入参集合
        List<MealLineImportParam> mealLineFrontParams = JSONUtil.toList(params, MealLineImportParam.class);
        //反参数据
        List<DishesScheduleImortReturnModel> returnModels = new ArrayList<>();
        //校验通过数据
        List<DishesScheduleParam> dishesScheduleParams = new ArrayList<>();
        //读取并校验excel数据
        analysisAndCheckExcelData(returnModels, mealLineFrontParams, dishesScheduleParams, file);
        if (CollUtil.isNotEmpty(returnModels)) {
            return returnModels;
        }
        //保存数据
        dishesScheduleService.adds(dishesScheduleParams);
        return Collections.emptyList();
    }


    /**
     * 解析导入信息
     * @param file 文件
     */
    private void analysisAndCheckExcelData(List<DishesScheduleImortReturnModel> returnModels, List<MealLineImportParam> mealLineFrontParams, List<DishesScheduleParam> dishesScheduleParams, MultipartFile file) {
        try {
            Long restaurantId = mealLineFrontParams.get(0).getRestaurantId();
            //通过餐厅id查询用餐类型配置
            RestaurantTimeListParam param = new RestaurantTimeListParam();
            param.setRestaurantId(restaurantId);
            List<String> timeTypes = restaurantTimeService.timeList(param).stream().map(RestaurantTimeModel::getType).collect(Collectors.toList());
            //获取辣度字典
            Map<String, DictItemModel> foodSpicyMap = new HashMap<>(4);
            getDictData(foodSpicyMap,"foodSpicy");
            //获取餐品类别字典
            Map<String, DictItemModel> foodCategoryMap = new HashMap<>(4);
            getDictData(foodCategoryMap,"foodCategory");
            //获取营业时间字典
            Map<String, DictItemModel> restaurantTimeMap = new HashMap<>(4);
            getDictData(restaurantTimeMap,"restaurantTime");

            Map<String, MealLineImportParam> lineImportParamMap = mealLineFrontParams.stream().collect(Collectors.toMap(MealLineImportParam::getMealLineName, Function.identity(), (key1, key2) -> key2));
            ExcelReadExecutor excelReadExecutor = EasyExcel.read(file.getInputStream()).build().excelExecutor();
            List<ReadSheet> sheets = excelReadExecutor.sheetList();
            for (int i = 0; i < sheets.size(); i++) {
                String sheetName = sheets.get(i).getSheetName();
                if (ObjectUtil.isEmpty(lineImportParamMap.get(sheetName))) {
                    DishesScheduleImortReturnModel returnModel = new DishesScheduleImortReturnModel();
                    returnModel.setErrMessage(sheetName + "不存在");
                    returnModels.add(returnModel);
                    continue;
                }
                MealLineImportParam mealLineImportParam = lineImportParamMap.get(sheetName);
                DishesScheduleImportListener dishesScheduleImportListener = new DishesScheduleImportListener(timeTypes,foodSpicyMap, foodCategoryMap, restaurantTimeMap);
                ZipSecureFile.setMinInflateRatio(-1.0d);
                EasyExcel.read(file.getInputStream(), DishesScheduleImportParam.class, dishesScheduleImportListener).headRowNumber(2).sheet(i).doRead();
                List<DishesScheduleImportParam> addList = dishesScheduleImportListener.getAddList();
                //校验sheet存在重复数据
                checkRepeatData(addList);
                List<DishesScheduleImportParam> scheduleImportParams = addList.stream().filter(f -> StrUtil.isNotEmpty(f.getErrMessage())).collect(Collectors.toList());
                //校验sheet存在非法数据
                if (CollUtil.isNotEmpty(scheduleImportParams)) {
                    DishesScheduleImortReturnModel returnModel = new DishesScheduleImortReturnModel();
                    returnModel.setName(sheetName);
                    List<DishesImportReturnModel> dishesImportReturnModels = scheduleImportParams.stream().map(p -> {
                        DishesImportReturnModel importReturnModel = new DishesImportReturnModel();
                        importReturnModel.setErrMessage(p.getErrMessage());
                        return importReturnModel;
                    }).collect(Collectors.toList());
                    returnModel.setDishesImportReturnModels(dishesImportReturnModels);
                    returnModels.add(returnModel);
                    continue;
                }
                //校验通过数据
                List<DishesScheduleParam> dishesSchedules = addList.stream().map(p -> {
                    DishesScheduleParam dishesScheduleParam = BeanUtil.toBean(p, DishesScheduleParam.class);
                    dishesScheduleParam.setRestaurantId(mealLineImportParam.getRestaurantId());
                    dishesScheduleParam.setMealLineId(mealLineImportParam.getMealLineId());
                    dishesScheduleParam.setType(foodCategoryMap.get(p.getType()).getValue());
                    dishesScheduleParam.setMealTime(restaurantTimeMap.get(p.getMealTime()).getValue());
                    dishesScheduleParam.setPungencyDegree(ObjectUtil.isEmpty(p.getPungencyDegree())?null:Integer.valueOf(foodSpicyMap.get(p.getPungencyDegree()).getValue()));
                    dishesScheduleParam.setProductionDate(DateUtil.parse(p.getProductionDate(), "yyyy/MM/dd"));
                    dishesScheduleParam.setPrice(new BigDecimal(p.getPrice()));
                    dishesScheduleParam.setWeight(ObjectUtil.isEmpty(p.getWeight())?null:String.valueOf(p.getWeight()));
                    return dishesScheduleParam;
                }).collect(Collectors.toList());
                dishesScheduleParams.addAll(dishesSchedules);
            }
        } catch (Exception e) {
            DishesScheduleImortReturnModel returnModel = new DishesScheduleImortReturnModel();
            returnModel.setErrMessage("菜品导入异常，原因是:"+e.getMessage());
            returnModels.add(returnModel);
            log.error("菜品导入异常，原因是{}", e.getMessage());
        }
    }


    /**
     * 获取字典信息
     * @param dictTypeCode 父字典编码
     */
    private void getDictData(Map<String, DictItemModel> foodSpicyMap, String dictTypeCode) {
        Map<String, DictItemModel> dictItemModelMap = Objects.requireNonNull(dictService.findItemsByDictType(dictTypeCode))
                .stream().collect(Collectors.toMap(DictItemModel::getLabel, Function.identity(), (key1, key2) -> key2));
        foodSpicyMap.putAll(dictItemModelMap);
    }


    /**
     * 校验重复数据
     * @param addList 文件
     */
    private void checkRepeatData(List<DishesScheduleImportParam> addList) {

        LinkedHashMap<String, List<DishesScheduleImportParam>> dishLinkMap = addList.stream()
                .collect(Collectors.groupingBy(a -> a.getProductionDate()+ "-" + a.getMealTime() + "-" + a.getName()))
                .entrySet().stream().filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        if (ObjectUtil.isEmpty(dishLinkMap)) {
            return;
        }
        dishLinkMap.forEach((key, value) -> {
            List<DishesScheduleImportParam> dishesScheduleImportParams = dishLinkMap.get(key);
            String nums = dishesScheduleImportParams.stream()
                    .map(DishesScheduleImportParam::getNumber)
                    .collect(Collectors.joining(", "));
            dishesScheduleImportParams.forEach(f -> f.setErrMessage(StrUtil.isEmpty(f.getErrMessage()) ? "第" + f.getNumber() + "行: " + "第" + nums + "行数据重复" : f.getErrMessage() + "；第" + nums + "行数据重复"));
        });


    }
}
