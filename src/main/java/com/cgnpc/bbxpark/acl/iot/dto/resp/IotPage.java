package com.cgnpc.bbxpark.acl.iot.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class IotPage<T> {
    private long total;
    private List<T> rows;
}
