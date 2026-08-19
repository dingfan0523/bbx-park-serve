package com.cgnpc.bbxpark.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.cgnpc.bbxpark.common.entity.BaseExEntity;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.cud.core.IAppContext;
import com.cgnpc.cud.gen.service.impl.CudFormMetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;


@Component
@Primary
@ConditionalOnProperty(
        name = {"mybatis-plus.form-meta-object.type"},
        havingValue = "bbx",
        matchIfMissing = true
)
public class BbxEntityObjectHandler implements MetaObjectHandler {

    private static Logger logger = LoggerFactory.getLogger(CudFormMetaObjectHandler.class);
    private static final String CREATOR_ID = "creatorId";
    private static final String UPDATOR_ID = "updatorId";
    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";
    private static final String DELETED = "deleted";
    private static final String TENANT_ID = "tenantId";
    private static final Integer DEFAULT_DELETE_FLAG = 1;
    @Autowired(
            required = false
    )
    private IAppContext appContext;

    public void insertFill(MetaObject metaObject) {
        if(Objects.nonNull(metaObject) && metaObject.getOriginalObject() instanceof BaseExEntity){
            Date now = new Date();
            String createTimeProp = metaObject.findProperty(CREATE_TIME, true);
            BaseExEntity baseDO = (BaseExEntity) metaObject.getOriginalObject();
            if (Objects.nonNull(createTimeProp) && Objects.isNull(baseDO.getCreateTime())) {
                this.setInsertFieldValByName(CREATE_TIME, now, metaObject);
            }

            String createNoProp = metaObject.findProperty(CREATOR_ID, true);
            if (Objects.nonNull(createNoProp) && Objects.isNull(baseDO.getCreatorId())) {
                this.setInsertFieldValByName(CREATOR_ID, this.getUsername(), metaObject);
            }

            String tenantIdProp = metaObject.findProperty(TENANT_ID, true);
            if (Objects.nonNull(tenantIdProp) && Objects.isNull(baseDO.getTenantId())) {
                this.setInsertFieldValByName(TENANT_ID, WebFrameworkUtils.getHeaderTenantId(), metaObject);
            }

            String updatorNoProp = metaObject.findProperty(UPDATOR_ID, true);
            if (Objects.nonNull(updatorNoProp) && Objects.isNull(baseDO.getUpdatorId())) {
                this.updateFillInfo(metaObject);
            }
            String deleteFlagProp = metaObject.findProperty(DELETED, true);
            if (Objects.nonNull(deleteFlagProp) && Objects.isNull(baseDO.getDeleted())) {
                this.setInsertFieldValByName(DELETED, DEFAULT_DELETE_FLAG, metaObject);
            }
        }

    }

    public void updateFill(MetaObject metaObject) {
        this.updateFillInfo(metaObject);
    }

    private void updateFillInfo(MetaObject metaObject) {
        if(Objects.nonNull(metaObject)){
            Date now = new Date();
            String updateTimeProp = metaObject.findProperty(UPDATE_TIME, true);
            if (Objects.nonNull(updateTimeProp)) {
                this.setFieldValByName(UPDATE_TIME, now, metaObject);
            }
            String updateNoProp = metaObject.findProperty(UPDATOR_ID, true);
            if (Objects.nonNull(updateNoProp)) {
                this.setFieldValByName(UPDATOR_ID, this.getUsername(), metaObject);
            }
        }
    }

    private String getUsername() {
       return WebFrameworkUtils.getHeaderUserId();
    }
}
