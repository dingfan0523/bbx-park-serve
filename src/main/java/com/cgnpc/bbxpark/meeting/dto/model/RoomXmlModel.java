package com.cgnpc.bbxpark.meeting.dto.model;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author huangyongtao
 * @Description 会议室信息xml
 * @date 2024/10/10 16:21
 */
public class RoomXmlModel {
    /**
     * 会议室id
     */
    private String roomId;
    /**
     * 会议室名称
     */
    private String roomName;
    /**
     * 楼号id
     */
    private String roomFId;
    /**
     * 楼层 默认1层
     */
    private Integer roomFloor;

    public RoomXmlModel() {}

    public RoomXmlModel(String roomId, String roomName, String roomFId, Integer roomFloor) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomFId = roomFId;
        this.roomFloor = roomFloor;
    }

    @XmlElement(name="roomId")
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @XmlElement(name="roomName")
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @XmlElement(name="roomFId")
    public String getRoomFId() {
        return roomFId;
    }

    public void setRoomFId(String roomFId) {
        this.roomFId = roomFId;
    }

    @XmlElement(name="roomFloor")
    public Integer getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(Integer roomFloor) {
        this.roomFloor = roomFloor;
    }
}
