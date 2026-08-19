
package com.cgnpc.bbxpark.settings.api;

import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.settings.dto.model.AwardModel;
import com.cgnpc.bbxpark.settings.dto.param.AwardListParam;
import com.cgnpc.bbxpark.settings.service.IAwardService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/***
 * @Description 评优评奖app服务控制类
 * @author huangyongtao
 * @date 2025/11/13 9:22
 */
@RestController
@RequestMapping("/api/app/award")
@Api(tags = "评优评奖app接口")
public class ApiAwardController {

    /**
     * 评优评奖服务接口.
     */
    @Autowired
    private IAwardService awardService;

    /**
     * 获取评优评奖展示中列表.
     */
    @ApiOperation(value = "获取评优评奖展示中列表")
    @PostMapping(value = "/findDisplay")
    @RequiredToken
    public CudResult<List<AwardModel>> findDisplay(@RequestBody AwardListParam param) {
            return CudResult.success(awardService.findDisplay(param));
    }
}
