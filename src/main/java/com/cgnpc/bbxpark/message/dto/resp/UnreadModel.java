package com.cgnpc.bbxpark.message.dto.resp;

import lombok.Data;

import java.io.Serializable;

@Data
public class UnreadModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long type;

    private Long unreadCount;
}
