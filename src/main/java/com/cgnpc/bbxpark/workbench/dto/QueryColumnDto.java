package com.cgnpc.bbxpark.workbench.dto;

import lombok.Data;

@Data
public class QueryColumnDto {
    private String name;
    private String type;

    public QueryColumnDto(String name, String type) {
        this.name = name;
        this.type = type.toUpperCase();
    }

    public void setType(String type) {
        this.type = type == null ? "": type;
    }
}
