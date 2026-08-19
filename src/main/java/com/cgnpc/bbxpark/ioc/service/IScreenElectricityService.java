package com.cgnpc.bbxpark.ioc.service;

import com.cgnpc.bbxpark.energy.dto.model.EnergyBranchModel;
import com.cgnpc.bbxpark.ioc.dto.model.*;
import com.cgnpc.bbxpark.ioc.dto.param.BranchEnergyFlowParam;

import java.util.List;

public interface IScreenElectricityService {
    ElectricityOverviewModel getElectricityOverview(Integer year,Integer month);

    List<ElectricityTrendModel> getElectricityTrend(Integer year,Integer month);

    List<ElectricityAreaCompareModel> getAreaCompare(Long id,Integer year,Integer month);

    ElectricityManageAnalysisModel getManageAnalysis(String branchType);

    List<ElectricityRealtimeConfigModel> getRealtimeConfig(Long id,String branchType);


    List<ElectricityLossRateModel> getLossRate(Integer year,Integer month);

    List<EnergyBranchModel> getFlowData(BranchEnergyFlowParam param);

    List<ElectricityPerCapitaTrendModel> getPerCapitaTrend(Integer year,Integer month);

    List<ElectricitySafetyReminderModel> getSafetyReminderList(Integer year,Integer month,String branchType);
}
