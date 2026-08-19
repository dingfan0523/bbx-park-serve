package com.cgnpc.bbxpark.meeting.dto.model;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author huangyongtao
 * @Description 会议室列表出参xml
 * @date 2024/10/10 15:53
 */
@XmlRootElement(name="conferRoomList")
public class ConferRoomXmlModel {
    /**
     * 返回结果 200,：成功；非200：失败
     */
    private Integer result  ;

    /**
     * 会议室信息
     */
    private List<RoomXmlModel> roomList;

    @XmlElement(name="result")
    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @XmlElementWrapper(name="roomList")
    @XmlElement(name = "room")
    public List<RoomXmlModel> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomXmlModel> roomList) {
        this.roomList = roomList;
    }
}
