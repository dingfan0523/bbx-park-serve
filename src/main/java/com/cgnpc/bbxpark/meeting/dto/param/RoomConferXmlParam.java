package com.cgnpc.bbxpark.meeting.dto.param;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author huangyongtao
 * @Description 会议列表入参xml
 * @date 2024/10/10 15:53
 */
@XmlRootElement(name="roomConferList")
public class RoomConferXmlParam {

    /**
     * 会议室id
     */
    private String roomId;
    /**
     * 片区ID(areaID、roomID必须有一个不为空)
     */
    private String areaId;

    /**
     * 会议室所在楼层，填写areaId时，roomfloor必填，否则默认1层
     */
    private Integer roomFloor = 1;

    /**
     * 时间(格式：yyyy-MM-dd 必填 返回当前日期的所有没过期会议)
     */
    private String date;

    @XmlElement(name="roomId")
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @XmlElement(name="areaId")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @XmlElement(name="roomFloor")
    public Integer getRoomFloor() {
        return roomFloor;
    }

    public void setRoomFloor(Integer roomFloor) {
        this.roomFloor = roomFloor;
    }

    @XmlElement(name="date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
