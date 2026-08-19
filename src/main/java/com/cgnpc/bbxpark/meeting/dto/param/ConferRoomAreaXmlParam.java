package com.cgnpc.bbxpark.meeting.dto.param;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author huangyongtao
 * @Description 片区列表入参xml
 * @date 2024/10/10 15:53
 */
@XmlRootElement(name="conferRoomList")
public class ConferRoomAreaXmlParam {
    /**
     * 片区ID(片区ID  不填则返回所有片区信息和楼号信息，填写返回片区下的楼号信息)
     */
    private String parentId;

    @XmlElement(name="parentId")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
