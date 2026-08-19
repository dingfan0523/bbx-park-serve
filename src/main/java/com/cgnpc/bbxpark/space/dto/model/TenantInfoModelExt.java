
package com.cgnpc.bbxpark.space.dto.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
public class TenantInfoModelExt extends TenantInfoModel {
    @ApiModelProperty(value = "管理员集合")
    private List<Manager> managers;

    @Data
    public static class Manager{
        @ApiModelProperty(value = "管理员id")
        private String managerUserId;

        @ApiModelProperty(value = "管理员名称")
        private String managerUserName;

        @ApiModelProperty(value = "管理员员工号")
        private String managerStaffNo;
    }
}
