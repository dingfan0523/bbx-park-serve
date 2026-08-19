package com.cgnpc.bbxpark.settings.dto.param;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;


@Data
public class ConfigBatchParam {

    @Size(min=1)
    @Size(max=500)
    private List<Long> ids;
}
