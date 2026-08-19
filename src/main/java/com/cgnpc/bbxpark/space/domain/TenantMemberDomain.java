
package com.cgnpc.bbxpark.space.domain;

import com.cgnpc.bbxpark.space.dto.model.TenantMemberModel;
import lombok.Data;

import java.io.Serializable;

@Data
public class TenantMemberDomain extends TenantMemberModel implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3760651777340640204L;


    private String userName;

    private String nickName;

    private String sexLabel;

    private String userAvatar;

    private String tenantName;
    /**
     *用户标识.
     **/
    private String departmentId;
    /**
     *用户标识.
     **/
    private String departmentName;

    private String staffid;

}
