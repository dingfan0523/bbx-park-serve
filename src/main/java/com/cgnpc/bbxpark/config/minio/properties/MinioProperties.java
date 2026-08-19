package com.cgnpc.bbxpark.config.minio.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String url = "http://10.33.15.71:9000";

    private String accessKey = "U1nJ0BeZErzGelxChXI5";

    private String secretKey = "nU6SLbStsWvm3jIG7aFrzNEtk68mCuEqhOacKeiP";

    private String bucketName = "bbx-uat";

}
