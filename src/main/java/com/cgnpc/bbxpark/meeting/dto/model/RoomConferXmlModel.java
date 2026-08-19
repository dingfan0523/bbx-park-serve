package com.cgnpc.bbxpark.meeting.dto.model;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author huangyongtao
 * @Description 会议列表出参xml
 * @date 2024/10/10 15:53
 */
@XmlRootElement(name="roomConferList")
public class RoomConferXmlModel {
    /**
     * 返回结果 200,：成功；非200：失败
     */
    private Integer result  ;

    /**
     * 会议信息
     */
    private List<ConferXmlModel> conferList;

    @XmlElement(name="result")
    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @XmlElementWrapper(name="conferenceList")
    @XmlElement(name = "conference")
    public List<ConferXmlModel> getConferList() {
        return conferList;
    }

    public void setConferList(List<ConferXmlModel> conferList) {
        this.conferList = conferList;
    }
}
