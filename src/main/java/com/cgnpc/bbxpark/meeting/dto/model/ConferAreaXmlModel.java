package com.cgnpc.bbxpark.meeting.dto.model;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author huangyongtao
 * @Description 片区列表出参xml
 * @date 2024/10/10 15:53
 */
@XmlRootElement(name="conferAreaList")
public class ConferAreaXmlModel {
    /**
     * 返回结果 200,：成功；非200：失败
     */
    private Integer result  ;

    /**
     * 片区信息
     */
    private List<AreaXmlModel> areaList;

    @XmlElement(name="result")
    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    @XmlElementWrapper(name="areaList")
    @XmlElement(name = "area")
    public List<AreaXmlModel> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<AreaXmlModel> areaList) {
        this.areaList = areaList;
    }
}
