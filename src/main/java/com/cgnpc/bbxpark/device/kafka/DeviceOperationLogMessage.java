package com.cgnpc.bbxpark.device.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceOperationLogMessage {

        private String createUserId;
        private Date createTime;
        private String response;
        private String createUserName;
        private String id;
        private Long groupId;
        private String deviceId;
        private Long type;
        private String params;
}
