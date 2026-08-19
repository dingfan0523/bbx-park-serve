package com.cgnpc.bbxpark.meeting.dto.param;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author huangyongtao
 * @Description 会议室列表入参xml
 * @date 2024/10/10 15:53
 */
@XmlRootElement(name="conferRoomList")
public class ConferRoomXmlParam {
    /**
     * 片区ID(非必填)
     */
    private String areaId;

    @XmlElement(name="areaId")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}
