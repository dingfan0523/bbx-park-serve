package com.cgnpc.bbxpark.space.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceFileParam;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class BusinessParkSpaceClassListener extends AnalysisEventListener<ParkSpaceFileParam> {
    private List<ParkSpaceFileParam> addList = new ArrayList<>();

    private static final String REGEX = "^[a-zA-Z0-9]*$";


    /**
     * 解析校验数据
     *
     * @Param
     * @Return
     */
    @Override
    public void invoke(ParkSpaceFileParam param, AnalysisContext analysisContext) {
        if (StrUtil.isBlank(param.getSpaceCode())) {
            param.setImportErrorDesc("空间编码不能为空");
        } else if (!param.getSpaceCode().matches(REGEX)) {
            param.setImportErrorDesc("空间编码格式不正确，请检查输入");
        } else if (param.getSpaceCode().length() > 30) {
            param.setImportErrorDesc("空间编码长度不能超过30个字符");
        } else if (StrUtil.isBlank(param.getParentSpaceCode())) {
            param.setImportErrorDesc("上级空间编码不能为空");
        } else if (!param.getParentSpaceCode().matches(REGEX)) {
            param.setImportErrorDesc("上级空间编码格式不正确，请检查输入");
        } else if (param.getParentSpaceCode().length() > 30) {
            param.setImportErrorDesc("上级空间编码长度不能超过30个字符");
        } else if (StrUtil.isBlank(param.getSpaceName())) {
            param.setImportErrorDesc("空间名称不能为空");
        } else if (param.getSpaceName().length() > 30) {
            param.setImportErrorDesc("空间名称长度不能超过30个字符");
        }
        addList.add(param);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 数据解析完成后的操作，例如数据校验、存储等
    }

    public List<ParkSpaceFileParam> getAddList() {
        return addList;
    }
}
