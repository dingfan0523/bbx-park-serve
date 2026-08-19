
package com.cgnpc.bbxpark.settings.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.cgnpc.bbxpark.settings.dto.model.DictItemModel;
import com.cgnpc.cud.core.aep.AepContext;
import com.cgnpc.cud.core.exception.CudBusinessException;
import com.cgnpc.cud.core.exception.enums.CudSystemMsgEnums;
import com.cgnpc.cud.dict.Service.SysDictItemService;
import com.cgnpc.cud.dict.Service.SysDictService;
import com.cgnpc.cud.dict.domain.KVItem;
import com.cgnpc.cud.dict.domain.SysDict;
import com.cgnpc.cud.dict.domain.SysDictItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * @Description 设备分组关系服务实现
 * @author huangyongtao
 * @date 2024/8/12 13:57
 */
@Service
public class DictServiceImpl {

	@Autowired
    private SysDictItemService sysDictItemService;

    @Autowired
    private SysDictService sysDictService;


	public List<DictItemModel> findDictItemList(){
        List<DictItemModel> dictItemModels = new ArrayList<>();
        List<SysDict> sysDictList = sysDictService.list(Wrappers.<SysDict>lambdaQuery().select(SysDict::getId, SysDict::getDictCode).eq(SysDict::getAppId, getAppId()).eq(SysDict::getDictStatus, "1"));
        if(CollectionUtil.isEmpty(sysDictList)){
            return dictItemModels;
        }
        Map<String, String> dictName = sysDictList.stream().collect(Collectors.toMap(SysDict::getId, SysDict::getDictCode));
        List<SysDictItem> dictItems = sysDictItemService.list(Wrappers.<SysDictItem>lambdaQuery().eq(SysDictItem::getAppId, getAppId()).eq(SysDictItem::getItemStatus, "1"));
        if(CollectionUtil.isEmpty(dictItems)){
            return dictItemModels;
        }
        dictItems.forEach(item->{
            if(dictName.containsKey(item.getDictId())){
                DictItemModel model = new DictItemModel();
                model.setDictTypeCode(dictName.get(item.getDictId()));
                model.setDictTypeId(item.getDictId());
                model.setCode(item.getItemCode());
                model.setValue(item.getItemValue());
                model.setLabel(item.getItemText());
                model.setDescription(item.getItemDesc());
                model.setSortOrder(item.getItemSortOrder());
                dictItemModels.add(model);
            }
        });
        return dictItemModels;
    }
    private String getAppId() {
        String appId = AepContext.getAppId();
        if (StrUtil.isBlank(appId)) {
            throw new CudBusinessException(CudSystemMsgEnums.CN_CUD_DICT_APPID_NOT_FIND);
        } else {
            return appId;
        }
    }

    public List<DictItemModel> findItemsByDictType(String dictType) {
        //xml错误：ITEM_STATUS = "1",需要改成单引号
//        List<KVItem> sysDictItemKVByDict = sysDictItemService.getSysDictItemKVByDict(dictType,getAppId());
//        return sysDictItemKVByDict.stream().map(item -> {
//            DictItemModel itemModel = new DictItemModel();
//            itemModel.setCode(item.getItemValue());
//            itemModel.setValue(item.getItemValue());
//            itemModel.setDictTypeCode(dictType);
//            itemModel.setLabel(item.getItemText());
//            return itemModel;
//        }).collect(Collectors.toList());
        List<DictItemModel> dictItemModels = new ArrayList<>();
        List<SysDict> sysDictList = sysDictService.list(Wrappers.<SysDict>lambdaQuery().select(SysDict::getId, SysDict::getDictCode)
                .eq(SysDict::getAppId, getAppId())
                .eq(SysDict::getDictCode, dictType)
                .eq(SysDict::getDictStatus, "1"));
        if(CollectionUtil.isEmpty(sysDictList)){
            return dictItemModels;
        }
        List<SysDictItem> dictItems = sysDictItemService.list(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getAppId, getAppId())
                .eq(SysDictItem::getDictId, sysDictList.get(0).getId())
                .eq(SysDictItem::getItemStatus, "1"));
        if(CollectionUtil.isEmpty(dictItems)){
            return dictItemModels;
        }
        dictItems.forEach(item->{
            DictItemModel model = new DictItemModel();
            model.setDictTypeCode(dictType);
            model.setDictTypeId(item.getDictId());
            model.setCode(item.getItemCode());
            model.setValue(item.getItemValue());
            model.setLabel(item.getItemText());
            model.setDescription(item.getItemDesc());
            model.setSortOrder(item.getItemSortOrder());
            dictItemModels.add(model);
        });
        return dictItemModels;
    }
}
