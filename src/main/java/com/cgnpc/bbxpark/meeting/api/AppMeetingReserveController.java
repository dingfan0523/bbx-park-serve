
package com.cgnpc.bbxpark.meeting.api;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cgnpc.bbxpark.acl.uic.model.OrgDepartmentNode;
import com.cgnpc.bbxpark.common.constant.MeetingConstant;
import com.cgnpc.bbxpark.common.enums.MeetingReserveEndTypeEnum;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.AssertUtils;
import com.cgnpc.bbxpark.common.utils.DateUtils;
import com.cgnpc.bbxpark.common.utils.StringUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.common.web.CudResult;
import com.cgnpc.bbxpark.meeting.dto.model.*;
import com.cgnpc.bbxpark.meeting.dto.param.*;
import com.cgnpc.bbxpark.meeting.service.IMeetingAttendantEvaluateService;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveService;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveSyncService;
import com.cgnpc.bbxpark.meeting.service.MeetingReserveExportService;
import com.cgnpc.mobile.annotation.RequiredToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * @Description 会议预约服务控制类
 * @author huangyongtao
 * @date 2024/8/26 11:13
 */
@RestController
@Validated
@RequestMapping("/api/meeting/reserve/app")
@Api(tags = "智慧会议-移动端-会议")
@Slf4j
public class AppMeetingReserveController {


    /**
     * 会议预约服务接口.
     */
    @Autowired
    private IMeetingReserveService meetingReserveService;
    @Autowired
    private IMeetingReserveSyncService meetingReserveSyncService;
    @Autowired
    private MeetingReserveExportService meetingReserveExportService;

    @Autowired
    IMeetingAttendantEvaluateService meetingAttendantEvaluateService;
    
    /**
     * 移动端-会议数量查询
     */
    @ApiOperation(value = "移动端-会议数量查询")
    @GetMapping(value = "/pageCount")
    @RequiredToken
    public CudResult<AppMeetingReserveCountModel> pageCount() {
        return CudResult.success(meetingReserveService.pageCount());
    }

    /**
     * 移动端-会议分页列表
     */
    @ApiOperation(value = "移动端-会议分页列表")
    @PostMapping(value = "/page")
    @RequiredToken
    public CudResult<IPage<AppMeetingReserveModel>> pageApp(@RequestBody AppMeetingReservePageParam param) {
        param.setUserId(WebFrameworkUtils.getHeaderUserId());
        return CudResult.success(meetingReserveService.pageApp(param));
    }

    /**
     * 移动端-会服会议分页列表
     */
    @ApiOperation(value = "移动端-会服会议分页列表")
    @PostMapping(value = "/attendant/page")
    @RequiredToken
    public CudResult<IPage<AppMeetingReserveModel>> attendantPageApp(@RequestBody AppMeetingReservePageParam param) {
        return CudResult.success(meetingReserveService.attendantPageApp(param));
    }

    /**
     * 移动端-会议详情
     */
    @ApiOperation(value = "移动端-会议详情.")
    @GetMapping(value = "/detail/{id}")
    @RequiredToken
    public CudResult<AppMeetingReserveDetailModel> detailApp(@PathVariable(value = "id")Long id) {
        return CudResult.success(meetingReserveService.detailApp(id));
    }

    /**
     * 移动端-更改签到规则
     */
    @ApiOperation(value = "移动端-更改签到规则")
    @PostMapping(value = "/editRule")
    @RequiredToken
    public CudResult<Boolean> editRule(@RequestBody AppMeetingSignRuleParam param) {
        return CudResult.success(meetingReserveService.editRule(param));
    }

    /**
     * 移动端-保存会议附件
     */
    @ApiOperation(value = "移动端-保存会议附件")
    @PostMapping(value = "/saveFile")
    public CudResult<Boolean> saveFile(@RequestBody AppMeetingFileParam param) {
        return CudResult.success(meetingReserveService.appSaveFile(param));
    }

    /**
     * 移动端-删除会议附件
     */
    @ApiOperation(value = "移动端-删除会议附件")
    @GetMapping(value = "/file/remove/{id}")
    @RequiredToken
    public CudResult<Boolean> removeFile(@PathVariable(value = "id") Long id) {
        return CudResult.success(meetingReserveService.appRemoveFile(id));
    }

    /**
     * 移动端-取消会议
     */
    @ApiOperation(value = "移动端-取消会议")
    @GetMapping(value = "/cancel/{id}")
    @RequiredToken
    public CudResult<Boolean> cancel(@PathVariable(value = "id") Long id) {
        return CudResult.success(meetingReserveService.cancel(id));
    }

    /**
     * 移动端-发起人结束会议
     */
    @ApiOperation(value = "移动端-发起人结束会议")
    @PostMapping(value = "/finish")
    @RequiredToken
    public CudResult<Boolean> finish(@RequestBody AppMeetingFinishParam param) {
        param.setEndType(MeetingReserveEndTypeEnum.RESERVE.getCode());
        return CudResult.success(meetingReserveService.finish(param));
    }
    /**
     * 移动端-会服人员结束会议
     */
    @ApiOperation(value = "移动端-会服人员结束会议")
    @PostMapping(value = "/attendant/finish")
    @RequiredToken
    public CudResult<Boolean> attendantFinish(@RequestBody AppMeetingFinishParam param) {
        param.setEndType(MeetingReserveEndTypeEnum.SERVICE.getCode());
        return CudResult.success(meetingReserveService.finish(param));
    }

    /**
     * 移动端-发起人重置会议结束时间
     */
    @ApiOperation(value = "移动端-发起人重置会议结束时间")
    @GetMapping(value = "/resetRealEndTime/{id}")
    @RequiredToken
    public CudResult<Boolean> resetRealEndTime(@PathVariable(value = "id") Long id) {
        return CudResult.success(meetingReserveService.resetRealEndTime(id, null, MeetingReserveEndTypeEnum.RESERVE.getCode()));
    }
    /**
     * 移动端-会服重置会议结束时间
     */
    @ApiOperation(value = "移动端-会服重置会议结束时间")
    @PostMapping(value = "/attendant/resetRealEndTime")
    @RequiredToken
    public CudResult<Boolean> attendantResetRealEndTime(@RequestBody AppMeetingResetEndTimeParam param) {
        return CudResult.success(meetingReserveService.resetRealEndTime(param.getId(), param.getEndTime(), MeetingReserveEndTypeEnum.SERVICE.getCode()));
    }

    /***
     * @Description 移动端-获取最近的一条预约信息
     * @author huangyongtao
     * @date 2024/8/26 17:04
     */
    @ApiOperation(value = "移动端-获取最近的一条预约信息")
    @PostMapping(value = "/getNearest")
    @RequiredToken
    public CudResult<AppSimpleReserveModel> getNearest() {
        return CudResult.success(meetingReserveService.getNearest());
    }

    /**
     * 移动端-生成会议预约二维码内容
     */
    @ApiOperation(value = "移动端-生成会议预约二维码内容")
    @PostMapping(value = "/qrcode")
    @RequiredToken
    public CudResult<String> qrcode(@RequestBody MeetingReserveParam param) {
        // AES加密
        AES aes = SecureUtil.aes(MeetingConstant.AES_KEY.getBytes());
        //查询会议信息
        AppMeetingReserveSimpleModel model = meetingReserveService.getSimple(param.getId());
        String lastTime = model.getLastRuleTime() != null ? DateUtils.format(model.getLastRuleTime(),"yyyy-MM-dd HHmmss"):"";
        // 加密为16进制表示
        String qrcode = aes.encryptHex(MeetingConstant.MEETING_QRCODE + param.getId()+":"+lastTime);
        log.info("生成二维码|二维码参数:{}|二维码:{}",MeetingConstant.MEETING_QRCODE + param.getId()+":"+lastTime,qrcode);
        return CudResult.success(qrcode);
    }

    /**
     * 移动端-查询会议预约二维码内容
     */
    @ApiOperation(value = "移动端-查询会议预约二维码内容")
    @PostMapping(value = "/findQrcode")
    @RequiredToken
    public CudResult<AppMeetingReserveDetailModel> findQrcode(@RequestBody MeetingReserveParam param) {
        AssertUtils.notNull(param.getQrcode(), "二维码内容不能为空");
        // AES解密
        AES aes = SecureUtil.aes(MeetingConstant.AES_KEY.getBytes());
        String qrcode = aes.decryptStr(param.getQrcode());
        AssertUtils.isTrue(qrcode.startsWith(MeetingConstant.MEETING_QRCODE),"非会议二维码");
        String lastTime = "";
        try{
            log.info("解析二维码|二维码:{}|二维码解析:{}",param.getQrcode(),qrcode);
            String[] qrcodeStr = qrcode.split(":");
            lastTime = qrcodeStr[2];
            param.setId(Long.valueOf(qrcodeStr[1]));
        }catch (Exception e){
            throw GenericException.fail("非会议二维码");
        }
        if(!StringUtils.isEmpty(lastTime)){
            //查询会议信息
            AppMeetingReserveSimpleModel model = meetingReserveService.getSimple(param.getId());
            String lastRuleTime = model.getLastRuleTime() != null ? DateUtils.format(model.getLastRuleTime(),"yyyy-MM-dd HHmmss"):"";
            log.info("解析二维码|二维码:{}|解析时间:{}|规则时间:{}",qrcode,lastTime,lastRuleTime);
            AssertUtils.isFalse(!lastRuleTime.equals(lastTime),"二维码失效");
        }
        return CudResult.success(meetingReserveService.detailApp(param.getId()));
    }

    /**
     * 移动端-生成会议预约二维码内容
     */
    @ApiOperation(value = "移动端-签到列表")
    @PostMapping(value = "/signPage")
    @Deprecated
    @RequiredToken
    public CudResult<IPage<AppMeetingReserveModel>> signPage(@RequestBody MeetingReserveParam param) {
        IPage<AppMeetingReserveModel> page= new Page<>(1, 10, 0);
        return CudResult.success(page);
    }

    /**
     * 移动端-校验会议室是否被占用
     */
    @ApiOperation(value = "移动端-校验会议室是否被占用")
    @PostMapping(value = "/time/check")
    @RequiredToken
    public CudResult<Boolean> checkTime(@RequestBody MeetingReserveParam param) {
        AssertUtils.notNull(param.getStartTime(), "开始时间不能为空");
        AssertUtils.notNull(param.getEndTime(), "结束时间不能为空");
        AssertUtils.notNull(param.getRoomId(), "会议室不能为空");
        return CudResult.success(meetingReserveService.checkTime(param));
    }

    /**
     * PC端-新增会议预约.
     */
    @ApiOperation(value = "移动端-新增会议预约")
    @PostMapping(value = "/add")
    @RequiredToken
    public CudResult<AppMeetingReserveDraftModel> add(@RequestBody MeetingReserveAppSaveParam param) {
        return CudResult.success(meetingReserveService.appAdd(param));
    }

    /**
     * 移动端-会议编辑页面的详情
     */
    @ApiOperation(value = "移动端-会议编辑页面的详情.")
    @GetMapping(value = "/editDetail/{id}")
    @RequiredToken
    public CudResult<AppMeetingReserveDetailModel> detailEditApp(@PathVariable(value = "id") Long id) {
        return CudResult.success(meetingReserveService.detailEditApp(id));
    }

    /**
     * 移动端-会议编辑
     */
    @ApiOperation(value = "移动端-会议编辑.")
    @PostMapping(value = "/edit")
    @RequiredToken
    public CudResult<AppMeetingReserveDraftModel> edit(@RequestBody MeetingReserveAppSaveParam param) {
        return CudResult.success(meetingReserveService.appEdit(param));
    }

    /**
     * 移动端-删除会议草稿
     */
    @ApiOperation(value = "移动端-删除会议草稿.")
    @PostMapping(value = "/delete")
    @RequiredToken
    public CudResult<Boolean> delete(@RequestBody MeetingReserveAppSaveParam param) {
        return CudResult.success(meetingReserveService.appDelete(param));
    }

    /**
     * 移动端-会议草稿分页查询
     */
    @ApiOperation(value = "移动端-会议草稿分页查询")
    @PostMapping(value = "/draft/page")
    @RequiredToken
    public CudResult<IPage<AppMeetingReserveModel>> draftPage(@RequestBody AppMeetingReservePageParam param) {
        return CudResult.success(meetingReserveService.draftPage(param));
    }


    /***
     * 移动端-会议设为有效
     */
    @ApiOperation(value = "移动端-会议设为有效")
    @PostMapping(value = "/valid")
    @RequiredToken
    public CudResult<Boolean> valid(@RequestBody MeetingReserveValidParam param) {
        return CudResult.success(meetingReserveService.valid(param));
    }

    /***
     * 移动端-会议设为无效
     */
    @ApiOperation(value = "移动端-会议设为无效")
    @PostMapping(value = "/inValid")
    @RequiredToken
    public CudResult<Boolean> inValid(@RequestBody MeetingReserveInValidParam param) {
        return CudResult.success(meetingReserveService.inValid(param));
    }

    /***
     * 移动端-会议延时
     */
    @ApiOperation(value = "移动端-会议延时")
    @PostMapping(value = "/delay")
    @RequiredToken
    public CudResult<Boolean> delay(@RequestBody MeetingReserveDelayParam param) {
        AssertUtils.notNull(param.getDelayTime(), "会议延时时间不能为空");
        return CudResult.success(meetingReserveService.delay(param));
    }

    /**
     * 移动端-呼叫会服
     */
    @ApiOperation(value = "移动端-呼叫会服")
    @GetMapping(value = "/call/{id}")
    @RequiredToken
    public CudResult<Boolean> call(@PathVariable(value = "id") Long id) {
        return CudResult.success(meetingReserveService.call(id));
    }

    /***
     * 移动端-会服评价
     */
    @ApiOperation(value = "移动端-会服评价")
    @PostMapping(value = "/evaluate")
    @RequiredToken
    public CudResult<Boolean> evaluate(@RequestBody MeetingAttendantEvaluateParam param) {
        AssertUtils.notNull(param.getReserveId(), "会议id不能为空");
        AssertUtils.notNull(param.getScore(), "评价分数不能为空");
        return CudResult.success(meetingAttendantEvaluateService.add(param));
    }

    /**
     * 移动端-会服页面的详情
     */
    @ApiOperation(value = "移动端-会服页面的详情.")
    @GetMapping(value = "/serviceDetail/{id}")
    @RequiredToken
    public CudResult<AppMeetingReserveDetailModel> serviceDetail(@PathVariable(value = "id")Long id) {
        return CudResult.success(meetingReserveService.serviceDetail(id, null));
    }

    /**
     * 移动端-获取用户部门列表信息
     */
    @ApiOperation(value = "移动端-获取用户部门列表信息.")
    @GetMapping(value = "/userDepartment/{userId}")
    @RequiredToken
    public CudResult<List<OrgDepartmentNode>> getUserDepartment(@PathVariable(value = "userId")String userId) {
        return CudResult.success(meetingReserveService.getUserDepartment(userId));
    }

}
