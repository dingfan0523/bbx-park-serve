package com.cgnpc.bbxpark.device.dto.param;

import com.cgnpc.bbxpark.settings.domain.File;
import com.cgnpc.bbxpark.device.domain.IocProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @create zhaoshuo
 * @time 2025/2/24
 * @desc ioc产品新增参数
 */
@Data
public class IocProductAddParam extends IocProduct implements Serializable {
    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "产品图片路径集合")
    private List<File>  fileList ;
}
