package com.cgnpc.bbxpark.meeting.dto.model;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author huangyongtao
 * @Description 会议信息xml
 * @date 2024/10/10 16:21
 */
public class ConferXmlModel {
    /**
     * 会议id
     */
    private String conferId;
    /**
     * 会议名称
     */
    private String conferName;
    /**
     * 召集人姓名
     */
    private String accountName;
    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 会议室名称
     */
    private String roomName;

    /**
     * 楼号ID
     */
    private String fId;

    public ConferXmlModel() {}

    public ConferXmlModel(String conferId, String conferName, String accountName, String startTime, String endTime, String roomName, String fId) {
        this.conferId = conferId;
        this.conferName = conferName;
        this.accountName = accountName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomName = roomName;
        this.fId = fId;
    }

    @XmlElement(name="conferId")
    public String getConferId() {
        return conferId;
    }

    public void setConferId(String conferId) {
        this.conferId = conferId;
    }

    @XmlElement(name="conferName")
    public String getConferName() {
        return conferName;
    }

    public void setConferName(String conferName) {
        this.conferName = conferName;
    }

    @XmlElement(name="accountName")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @XmlElement(name="startTime")
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @XmlElement(name="endTime")
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @XmlElement(name="roomName")
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @XmlElement(name="fId")
    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }
}
