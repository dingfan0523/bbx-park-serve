package com.cgnpc.framework.deptinfo;


import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.client.RestClient;
import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.result.ApiResult;
import cn.com.cgnpc.aep.bizcenter.appcenter.sdk.vo.CgnRequestHeader;
import com.cgnpc.pro.api.ICudUserService;
import com.cgnpc.pro.auth.application.CudAepUtils;
import com.cgnpc.pro.config.aep.properties.CudAepProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/******************************
 * 用途说明:  获取部门部分信息
 * 作者姓名: pxmwrya
 * 创建时间: 2019/6/19_8:54
 ******************************/
@Slf4j
@Service
public class CurrentDept {

    @Autowired(required = false)
    RestClient restClient;

    @Autowired(required = false)
    CudAepProperties cudAepProperties;

    @Autowired(required = false)
    ICudUserService cudUserService;




    /**
    * 用途说明: 根据部门ID获取本部门信息  
    * 参数说明 deptId
    * 返回值说明:
    ***********************************/
    public Map getDeptInfo(String deptId){
    		
        Map deptMap = new HashMap<>();
        Map resultMap = new HashMap<>();

        //调用中台人员服务
        String deptInfo = "";
        String realServiceUrl = "/hrcenter/getOrgsByOrgIds";
        String url = CudAepUtils.getUrl(cudAepProperties.getActive()) + realServiceUrl;
        
        // 根据部门编码获取部门预算协调员和组织单元秘书 
        String realAssBugServiceUrl =  "/hrcenter/getAssBugAndSecretaryByDeptNo";
        String assBugUrl = CudAepUtils.getUrl(cudAepProperties.getActive()) + realAssBugServiceUrl;

        CgnRequestHeader header = null;
        ApiResult result = null;
        try{
        	// 中台部门信息
            header = CudAepUtils.getHeader(realServiceUrl,cudAepProperties);
            url = url+"?orgIds="+deptId;
            //调用远程接口
            result = restClient.postCgnVoForRest(url,header,null);
            if("200".equals(result.getCode())){
                ArrayList list = (ArrayList) result.getData();
                if(list != null && list.size() > 0){
                    deptMap = (HashMap)list.get(0);
                    //deptInfo = userMap.get("userName").toString();
                }
                String header1 = deptMap.get("header1").toString();
                String header2 = deptMap.get("header2").toString();
                resultMap.put("header1", header1);
                resultMap.put("header2", header2);
                
                String header1Str = "";
                String header2Str = "";
                
                if (!header1.isEmpty()) {
                	List header1List = cudUserService.getUsersByUserCenter(header1);
                	for (int i = 0; i < header1List.size(); i++) {
                		Map userMap = (Map)header1List.get(i);
                		if (i == 0 && !ObjectUtils.isEmpty(userMap.get("empName"))) {
                			header1Str = String.valueOf(userMap.get("empName"));
                		} else {
                			header1Str = header1Str + "," + userMap.get("empName");
                		}
                	}
                }
                resultMap.put("header1Str", header1Str);
                if (!header2.isEmpty()) {
                	List header2List = cudUserService.getUsersByUserCenter(header2);
            		for (int i = 0; i < header2List.size(); i++) {
            			Map userMap = (Map)header2List.get(i);
                		if (i == 0) {
                			header2Str = userMap.get("empName").toString();
                		} else {
                			header2Str = header2Str + "," + userMap.get("empName").toString();
                		}
                	}
                }
                resultMap.put("header2Str", header2Str);
            }
            
            //--------根据部门编码获取部门预算协调员和组织单元秘书 
            header = CudAepUtils.getHeader(realAssBugServiceUrl,cudAepProperties);
            url = assBugUrl+"?deptNo="+deptId;
            //调用远程接口
            result = restClient.postCgnVoForRest(url,header,null);
            String assistantBugget = "";
            String deptSecretary = "";
            String assistantBuggetId = "";
            String deptSecretaryId = "";

            if("200".equals(result.getCode())){
                ArrayList list = (ArrayList) result.getData();
                if(list != null && list.size() > 0){
                    deptMap = (HashMap)list.get(0);
                    //deptInfo = userMap.get("userName").toString();
                    assistantBugget = deptMap.get("assistantBugget").toString();
                    deptSecretary = deptMap.get("deptSecretary").toString();
                    assistantBuggetId = deptMap.get("assistantBuggetId").toString();
                    deptSecretaryId = deptMap.get("deptSecretaryId").toString();
                }
                // 预算协调员
                resultMap.put("assistantBugget", assistantBugget);
                resultMap.put("assistantBuggetId", assistantBuggetId);
            	// 秘书
                resultMap.put("deptSecretary", deptSecretary);
                resultMap.put("deptSecretaryId", deptSecretaryId);
            }
            	
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultMap;
    }
    
    /**********************************
     * 用途说明: 根据部门ID获取本公司信息  
     * 参数说明 deptId
     * 返回值说明:
     ***********************************/
     public Map getStaffCompanyInfo(String userId){
     		
     	 Map deptMap = new HashMap<>();

         //调用中台人员服务POST /hrcenter/getStaffCompanyInfo
         String deptInfo = "";
         String realServiceUrl = "/hrcenter/getStaffCompanyInfo";
         String url = CudAepUtils.getUrl(cudAepProperties.getActive()) + realServiceUrl;


         CgnRequestHeader header = null;
         ApiResult result = null;
         try{
         	// 中台部门信息
             header = CudAepUtils.getHeader(realServiceUrl,cudAepProperties);
             url = url+"?staffNo="+userId;
             //调用远程接口
             result = restClient.postCgnVoForRest(url,header,null);
             if("200".equals(result.getCode())){
                 ArrayList list = (ArrayList) result.getData();
                 String companyId = "";
                 String companyCode = "";
                 String companyName = "";
                 String staffNo = "";
                 String type = "1";
                 String staffName = "";
                 
                 if(list != null && list.size() > 0){
                     deptMap = (HashMap)list.get(0);
                     companyId = deptMap.get("companyId").toString();
                     companyCode = deptMap.get("companyCode").toString();
                     companyName = deptMap.get("companyName").toString();
                     staffNo = deptMap.get("staffNo").toString();
                     staffName = deptMap.get("staffName").toString();
                 }
                 deptMap.put("type", type);
                 deptMap.put("companyId", companyId);
             	 deptMap.put("companyName", companyName);
             	 deptMap.put("companyCode", companyCode);
             	 deptMap.put("staffNo", staffNo);
             	 deptMap.put("staffName", staffName);
             }
         }catch (Exception e){
             log.error(e.getMessage());
         }
         return deptMap;
     }
    
    
    
}