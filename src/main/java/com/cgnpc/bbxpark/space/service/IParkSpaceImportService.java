
package com.cgnpc.bbxpark.space.service;

import com.cgnpc.bbxpark.space.dto.model.ParkSpaceFileModel;
import com.cgnpc.bbxpark.space.dto.model.ParkSpaceImportTemporaryModel;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceDataParam;
import com.cgnpc.bbxpark.space.dto.param.ParkSpaceImportTemporaryListParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;



public interface IParkSpaceImportService {


    ParkSpaceFileModel importSpace(ParkSpaceDataParam param);

    List<ParkSpaceImportTemporaryModel> findByBatchCodeParkSpaceList(ParkSpaceImportTemporaryListParam param);


    Boolean checkTentAdminAuthority();

    ParkSpaceFileModel importSpaceFile(MultipartFile file);

    void downloadSpaceTemplate(HttpServletResponse response, HttpServletRequest request);


    void downloadFailData(HttpServletResponse response, String batchCode);

}
