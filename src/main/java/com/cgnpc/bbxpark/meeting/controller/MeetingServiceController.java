
package com.cgnpc.bbxpark.meeting.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cgnpc.bbxpark.common.constant.Constant;
import com.cgnpc.bbxpark.common.constant.InsertGroup;
import com.cgnpc.bbxpark.common.constant.UpdateGroup;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingServiceModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingServiceListParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingServicePageParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingServiceParam;
import com.cgnpc.bbxpark.meeting.service.IMeetingServiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;
import java.util.List;

/***
 * @Description 会服服务控制类
 * @author huangyongtao
 * @date 2024/8/26 10:05
 */
@Validated
@RestController
@RequestMapping(Constant.BASE_PATH + "/meeting/service")
@Api(tags = "智慧会议-PC端-会服")
public class MeetingServiceController {

    /**
     * 会服服务接口.
     */
    @Autowired
    private IMeetingServiceService meetingServiceService;

    /**
     * PC端-会服分页列表
     */
    @ApiOperation(value = "PC端-会服分页列表")
    @PostMapping(value = "/page")
    public CudResult<IPage<MeetingServiceModel>> page(@RequestBody MeetingServicePageParam param) {
        return CudResult.success(meetingServiceService.page(param));
    }

    /**
     * PC端-会服列表
     */
    @ApiOperation(value = "PC端-会服列表")
    @PostMapping(value = "/list")
    public CudResult<List<MeetingServiceModel>> list(@RequestBody MeetingServiceListParam param) {
        return CudResult.success(meetingServiceService.list(param));
    }

    /**
     * PC端-会服详情
     */
    @ApiOperation(value = "PC端-会服详情")
    
    @GetMapping(value = "/detail/{id}")
    public CudResult<MeetingServiceModel> detail(@PathVariable(value = "id")Long id) {
        return CudResult.success(meetingServiceService.detail(id));
    }

    /**
     * 新增会服.
     */
    @ApiOperation(value = "新增会服")
    @PostMapping(value = "/add")
    public CudResult<Boolean> add(@Validated({Default.class, InsertGroup.class}) @RequestBody MeetingServiceParam param) {
        return CudResult.success(meetingServiceService.add(param));
    }

    /**
     * 编辑会服.
     */
    @ApiOperation(value = "编辑会服")
    @PostMapping(value = "/edit")
    public CudResult<Boolean> edit(@RequestBody @Validated({Default.class, UpdateGroup.class}) MeetingServiceParam param) {
        return CudResult.success(meetingServiceService.edit(param));
    }

    /**
     * 删除会服.
     */
    @ApiOperation(value = "删除会服")
    
    @PostMapping(value = "/remove/{id}")
    public CudResult<Boolean> remove(@PathVariable(value = "id")Long id) {
        return CudResult.success(meetingServiceService.remove(id));
    }
}
