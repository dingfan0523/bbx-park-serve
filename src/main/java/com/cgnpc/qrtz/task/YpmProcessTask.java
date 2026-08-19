package com.cgnpc.qrtz.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cgnpc.cud.core.common.util.SpringUtils;
import com.cgnpc.cud.workbench.manage.domain.IpmProcessAssigneeModel;
import com.cgnpc.cud.workbench.manage.domain.IpmProcessStatueModel;
import com.cgnpc.cud.workbench.manage.service.impl.IpmProcessAssigneeServiceImpl;
import com.cgnpc.cud.workbench.manage.service.impl.IpmProcessStatueServiceImpl;
import com.cgnpc.qrtz.domain.CommonMap;
import com.cgnpc.qrtz.service.IIpmAssAllService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 每周或每日定时检查超时的流程数据
 */
@EnableScheduling
@Component
@ConditionalOnProperty(value = "spring.quartz.autoStartup", havingValue = "true")
public class YpmProcessTask {

    @Autowired
    private IpmProcessStatueServiceImpl ipmProcessStatueService;

    @Autowired
    private IpmProcessAssigneeServiceImpl ipmProcessAssigneeService;


//    @Scheduled(cron = "*/10 * * * * ?") //0 0 1 * * ? 作业时间 每天 01:00:00
    @Scheduled(cron = "0 */10 * * * ?")    // 每10分钟执行，测试用
    public void autoPlanTask() {
        planTask();
    }

    void planTask() {
        List<Map>  maps = ipmProcessStatueService.selectAssList();
        for (Map map:maps) {
            CommonMap commonMap = JSONObject.parseObject(JSONObject.toJSONString(map), CommonMap.class);
            if ("1".equals(commonMap.getStr("overDateFlag1"))){
                System.out.println(commonMap.get("procInstId")+"已超时");
                IpmProcessStatueModel ipmProcessStatueModel = new IpmProcessStatueModel();
                ipmProcessStatueModel.setProcessInfoId(commonMap.getStr("procInstId"));
                ipmProcessStatueModel.setDateFlag("1");
                ipmProcessStatueModel.setModifyDate(new Date());
                // 更新超时状态
                LambdaQueryWrapper<IpmProcessStatueModel> wrapper =  new LambdaQueryWrapper<IpmProcessStatueModel>()
                        .eq(IpmProcessStatueModel::getProcessInfoId,commonMap.getStr("procInstId"));
                ipmProcessStatueService.update(ipmProcessStatueModel,wrapper);
                IpmProcessAssigneeModel ipmProcessAssigneeModel = new IpmProcessAssigneeModel();
                ipmProcessAssigneeModel.setDateFlag("1");
                ipmProcessAssigneeModel.setModifyDate(new Date());
                LambdaQueryWrapper<IpmProcessAssigneeModel> queryWrapper = new LambdaQueryWrapper<IpmProcessAssigneeModel>()
                        .eq(IpmProcessAssigneeModel::getId,commonMap.getStr("assId"));
                ipmProcessAssigneeService.update(ipmProcessAssigneeModel,queryWrapper);
            }else {
                if ("1".equals(commonMap.getStr("expireDateFlag1"))){
                    System.out.println(commonMap.get("procInstId")+"即将超时");
                    IpmProcessStatueModel ipmProcessStatueModel = new IpmProcessStatueModel();
                    ipmProcessStatueModel.setProcessInfoId(commonMap.getStr("procInstId"));
                    ipmProcessStatueModel.setDateFlag("2");
                    ipmProcessStatueModel.setModifyDate(new Date());
                    // 更新超时状态
                    LambdaQueryWrapper<IpmProcessStatueModel> wrapper =  new LambdaQueryWrapper<IpmProcessStatueModel>()
                            .eq(IpmProcessStatueModel::getProcessInfoId,commonMap.getStr("procInstId"));
                    ipmProcessStatueService.update(ipmProcessStatueModel,wrapper);
                    IpmProcessAssigneeModel ipmProcessAssigneeModel = new IpmProcessAssigneeModel();
                    ipmProcessAssigneeModel.setDateFlag("2");
                    ipmProcessAssigneeModel.setModifyDate(new Date());
                    LambdaQueryWrapper<IpmProcessAssigneeModel> queryWrapper = new LambdaQueryWrapper<IpmProcessAssigneeModel>()
                            .eq(IpmProcessAssigneeModel::getId,commonMap.getStr("assId"));
                    ipmProcessAssigneeService.update(ipmProcessAssigneeModel,queryWrapper);
                }
            }
        }
    }


}
