package com.cgnpc.bbxpark.meeting.dto.model;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author huangyongtao
 * @Description 片区信息xml
 * @date 2024/10/10 16:21
 */
public class AreaXmlModel {
    /**
     * 片区id
     */
    private String areaId;
    /**
     * 片区名称
     */
    private String areaName;
    /**
     * 类型 1：片区；2：楼号
     */
    private String areaType;
    /**
     * 上级ID
     */
    private String pId;

    public AreaXmlModel() {}

    public AreaXmlModel(String areaId, String areaName, String areaType, String pId) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.areaType = areaType;
        this.pId = pId;
    }

    @XmlElement(name="areaId")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @XmlElement(name="areaName")
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @XmlElement(name="areaType")
    public String getAreaType() {
        return areaType;
    }

    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    @XmlElement(name="pId")
    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
