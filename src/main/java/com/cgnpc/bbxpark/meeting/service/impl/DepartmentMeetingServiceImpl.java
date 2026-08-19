package com.cgnpc.bbxpark.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.acl.uic.service.IDepartmentApiService;
import com.cgnpc.bbxpark.common.enums.Delete;
import com.cgnpc.bbxpark.common.exception.GenericException;
import com.cgnpc.bbxpark.common.utils.DateUtils;
import com.cgnpc.bbxpark.common.utils.ExcelExportUtils;
import com.cgnpc.bbxpark.common.utils.TimeRangeUtils;
import com.cgnpc.bbxpark.common.utils.WebFrameworkUtils;
import com.cgnpc.bbxpark.meeting.domain.*;
import com.cgnpc.bbxpark.meeting.dto.param.DepartmentMeetingCountParam;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingAttendantTaskCountParam;
import com.cgnpc.bbxpark.meeting.mapper.DepartmentMeetingRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveRepository;
import com.cgnpc.bbxpark.meeting.mapper.MeetingRoomRepository;
import com.cgnpc.bbxpark.meeting.service.IDepartmentMeetingService;
import com.cgnpc.bbxpark.space.dto.model.DepartmentInfoModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * @create zhaoshuo
 * @time 2025/2/7
 * @desc
 */
@Slf4j
@Service("departmentMeetingService")
public class DepartmentMeetingServiceImpl extends ServiceImpl<MeetingReserveRepository, MeetingReserve> implements IDepartmentMeetingService {

    @Autowired
    private DepartmentMeetingRepository departmentMeetingRepository;

    @Autowired
    private MeetingRoomRepository meetingRoomRepository;

    @Value("${parent.department.id:50259024}")
    private String departmentId;

    @Autowired
    IDepartmentApiService departmentApiService;
    private static final float CELL_HEIGHT = 25;
    //会议室计划标准使用时长
    private final Integer dayMeetingDuration = 8;
    private final Integer weekMeetingDuration = 40;
    private final Integer monthMeetingDuration = 160;
    private final Integer quarterMeetingDuration = 480;
    private final Integer yearMeetingDuration = 1920;

    /**
     * 获取部门会议时长
     *
     * @return 部门会议时长
     */
    @Override
    public List<Map<String, Object>> getDepartmentMeetingTime(MeetingAttendantTaskCountParam param) {
        try {
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            //获取苍南公司所有下级部门节点
            List<DepartmentInfoModel> deptTreeNodes = departmentApiService.findSubDepartments(departmentId);
            //获得所有部门id
            List<String> departmentIds = new ArrayList<>();
            Map<String, Map<String, Object>> result = new HashMap<>();
            for (DepartmentInfoModel map : deptTreeNodes) {
                String idValue = map.getId();
                if (idValue != null) {
                    departmentIds.add(idValue);
                }
                Map<String, Object> deptValue = new HashMap<>();
                deptValue.put("deptName", map.getName());
                deptValue.put("localValied", 0);
                deptValue.put("localInValied", 0);
                deptValue.put("videoValied", 0);
                deptValue.put("videoInValied", 0);
                result.put(String.valueOf(idValue), deptValue);
            }

            Date endTime = param.getEndTime();
            Date beginTime = param.getStartTime();
            String endtime = DateUtils.format(endTime, "yyyy-MM-dd HH:mm:ss");
            String begintime = DateUtils.format(beginTime, "yyyy-MM-dd HH:mm:ss");
            //调用查询
            List<DepartmentMeeting> departmentMeetings = departmentMeetingRepository.getDepartmentMeetingTime(departmentIds, begintime, endtime, param.getTenantId());
            for (DepartmentMeeting departmentMeeting : departmentMeetings) {
                String departmentId = departmentMeeting.getDepartmentId();
                if (result.containsKey(String.valueOf(departmentId))) {
                    //换算为小时
                    BigDecimal divide = new BigDecimal(0);
                    if (departmentMeeting.getTotal() != null && departmentMeeting.getTotal() != 0) {
                        divide = new BigDecimal(departmentMeeting.getTotal()).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
                    }
                    if (departmentMeeting.getMeetingType().equals("ordinary")) {
                        if (departmentMeeting.getInValidFlag().equals(1)) {
                            //本地无效
                            result.get(String.valueOf(departmentId)).put("localInValied", divide);
                        } else if (departmentMeeting.getInValidFlag().equals(0)) {
                            //本地有效
                            result.get(String.valueOf(departmentId)).put("localValied", divide);
                        }
                    } else if (departmentMeeting.getMeetingType().equals("video")) {
                        if (departmentMeeting.getInValidFlag().equals(1)) {
                            //视频无效
                            result.get(String.valueOf(departmentId)).put("videoInValied", divide);
                        } else if (departmentMeeting.getInValidFlag().equals(0)) {
                            //视频有效
                            result.get(String.valueOf(departmentId)).put("videoValied", divide);
                        }
                    }
                }
            }
            List<Map<String, Object>> resultList = new ArrayList<>(result.values());
            return resultList;
        } catch (Exception e) {
            log.error("查询失败！", e);
            throw GenericException.fail("查询失败！！");
        }
    }

    /**
     * 各部门会议场次统计
     *
     * @return
     */
    public List<Map<String, Object>> getDepartmentMeetingCount(MeetingAttendantTaskCountParam param) {
        try {
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            //获取苍南公司所有下级部门节点
            List<DepartmentInfoModel> deptTreeNodes = departmentApiService.findSubDepartments(departmentId);
            //获得所有部门id
            List<String> departmentIds = new ArrayList<>();
            Map<String, Map<String, Object>> result = new HashMap<>();
            for (DepartmentInfoModel map : deptTreeNodes) {
                String idValue = map.getId();
                if (idValue != null) {
                    departmentIds.add(idValue);
                }
                Map<String, Object> deptValue = new HashMap<>();
                deptValue.put("deptName", map.getName());
                deptValue.put("localValied", 0);
                deptValue.put("localInValied", 0);
                deptValue.put("videoValied", 0);
                deptValue.put("videoInValied", 0);
                result.put(String.valueOf(idValue), deptValue);
            }
            Date endTime = param.getEndTime();
            Date beginTime = param.getStartTime();
            String endtime = DateUtils.format(endTime, "yyyy-MM-dd HH:mm:ss");
            String begintime = DateUtils.format(beginTime, "yyyy-MM-dd HH:mm:ss");
            //调用查询
            List<DepartmentMeeting> departmentMeetings = departmentMeetingRepository.getDepartmentMeetingCount(departmentIds, begintime, endtime, param.getTenantId());
            for (DepartmentMeeting departmentMeeting : departmentMeetings) {
                String departmentId = departmentMeeting.getDepartmentId();
                if (result.containsKey(String.valueOf(departmentId))) {
                    if (departmentMeeting.getMeetingType().equals("ordinary")) {
                        if (departmentMeeting.getInValidFlag().equals(1)) {
                            //本地无效
                            result.get(String.valueOf(departmentId)).put("localInValied", departmentMeeting.getTotal());
                        } else if (departmentMeeting.getInValidFlag().equals(0)) {
                            //本地有效
                            result.get(String.valueOf(departmentId)).put("localValied", departmentMeeting.getTotal());
                        }
                    } else if (departmentMeeting.getMeetingType().equals("video")) {
                        if (departmentMeeting.getInValidFlag().equals(1)) {
                            //视频无效
                            result.get(String.valueOf(departmentId)).put("videoInValied", departmentMeeting.getTotal());
                        } else if (departmentMeeting.getInValidFlag().equals(0)) {
                            //视频无效
                            result.get(String.valueOf(departmentId)).put("videoValied", departmentMeeting.getTotal());
                        }
                    }
                }
            }
            List<Map<String, Object>> resultList = new ArrayList<>(result.values());
            return resultList;
        } catch (Exception e) {
            log.error("查询失败！", e);
            throw GenericException.fail("查询失败！！");
        }
    }

    /**
     * 查询时间段内的会议总时长和总会议场数
     * Integer type , String beginTime , String endTime
     */
    @Override
    public DepartmentMeetingSum getDepartmentMeetingSta(MeetingAttendantTaskCountParam param) {
        try {
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            //返回入参
            DepartmentMeetingSum result = new DepartmentMeetingSum();
            //获取苍南公司所有下级部门节点
            List<DepartmentInfoModel> deptTreeNodes = departmentApiService.findSubDepartments(departmentId);
            // List<DepartmentInfoModel> deptTreeNodes = departmentApiService.findSubDepartments(departmentId);
            //部门总数
            Integer deptSumNum = deptTreeNodes.size();
            result.setDeptSumNum(deptSumNum);
            //获得所有部门id
            List<String> departmentIds = new ArrayList<>();
            for (DepartmentInfoModel map : deptTreeNodes) {
                departmentIds.add(map.getId());
            }
            //获取部门下所有人员数量
            List<HashMap<String, Object>> deptMemberList = departmentMeetingRepository.getMemberByDeptIds(departmentIds);
            //部门下人员数量
            Integer memberSumNum = deptMemberList.size();
            result.setMemberSumNum(memberSumNum);
            Date endTime = param.getEndTime();
            Date beginTime = param.getStartTime();
            String endtime = DateUtils.format(endTime, "yyyy-MM-dd HH:mm:ss");
            String begintime = DateUtils.format(beginTime, "yyyy-MM-dd HH:mm:ss");
            //调用查询获取会议统计
            List<DepartmentMeetingSum> departmentMeetings = departmentMeetingRepository.getDepartmentMeetingSum(departmentIds, begintime, endtime, param.getTenantId());
            //环比数据查询
            // 将 java.util.Date 转换为 java.time.LocalDate
            LocalDate beginLocalDate = beginTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            TimeRangeUtils.TimeRange previousTimeRange = TimeRangeUtils.getPreviousTimeRange(beginLocalDate, param.getTimeType());
            LocalDateTime lastEndTime = previousTimeRange.getEnd();
            LocalDateTime lastStartTime = previousTimeRange.getStart();
            // 定义日期时间格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String lastEndTimeForm = lastEndTime.format(formatter);
            String lastStartTimeForm = lastStartTime.format(formatter);
            //调用查询获取上个周期的会议统计
            List<DepartmentMeetingSum> lastMeetings = departmentMeetingRepository.getDepartmentMeetingSum(departmentIds, lastStartTimeForm, lastEndTimeForm, param.getTenantId());
            //环比时间段统计信息
            DepartmentMeetingSum lastMeetingsSum = new DepartmentMeetingSum();
            for (DepartmentMeetingSum departmentMeeting : lastMeetings) {
                if (lastMeetingsSum.getTotalDurations() == null || lastMeetingsSum.getTotalDurations() == 0) {
                    lastMeetingsSum.setTotalDurations(departmentMeeting.getTotalDurations());
                } else {
                    lastMeetingsSum.setTotalDurations(lastMeetingsSum.getTotalDurations() + departmentMeeting.getTotalDurations());
                }
                if (lastMeetingsSum.getTotalMeetings() == null || lastMeetingsSum.getTotalMeetings() == 0) {
                    lastMeetingsSum.setTotalMeetings(departmentMeeting.getTotalMeetings());
                } else {
                    lastMeetingsSum.setTotalMeetings(lastMeetingsSum.getTotalMeetings() + departmentMeeting.getTotalMeetings());
                }
            }
            if (lastMeetingsSum.getTotalDurations() == null || lastMeetingsSum.getTotalDurations() == 0) {
                lastMeetingsSum.setTotalDuration(new BigDecimal(0));
            } else {
                lastMeetingsSum.setTotalDuration(new BigDecimal(lastMeetingsSum.getTotalDurations()).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP));
            }
            //当前时间段统计信息
            DepartmentMeetingSum departmentMeetingSum = new DepartmentMeetingSum();
            //无效会议数量
            Integer invalidNum = 0;
            for (DepartmentMeetingSum departmentMeeting : departmentMeetings) {
                if (departmentMeeting.getInValidFlag() == 1)
                    invalidNum = departmentMeeting.getTotalMeetings();
                if (departmentMeetingSum.getTotalDurations() == null || departmentMeetingSum.getTotalDurations() == 0) {
                    departmentMeetingSum.setTotalDurations(departmentMeeting.getTotalDurations());
                } else {
                    departmentMeetingSum.setTotalDurations(departmentMeetingSum.getTotalDurations() + departmentMeeting.getTotalDurations());
                }
                if (departmentMeetingSum.getTotalMeetings() == null || departmentMeetingSum.getTotalMeetings() == 0) {
                    departmentMeetingSum.setTotalMeetings(departmentMeeting.getTotalMeetings());
                } else {
                    departmentMeetingSum.setTotalMeetings(departmentMeetingSum.getTotalMeetings() + departmentMeeting.getTotalMeetings());
                }
            }
            result.setTotalMeetings(departmentMeetingSum.getTotalMeetings());
            if (departmentMeetingSum.getTotalDurations() == null || departmentMeetingSum.getTotalDurations() == 0) {
                result.setTotalDuration(new BigDecimal(0));
            } else {
                result.setTotalDurations(departmentMeetingSum.getTotalDurations());
                result.setTotalDuration(new BigDecimal(departmentMeetingSum.getTotalDurations()).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP));
            }
            //部门平均会议场次
            BigDecimal avgDepartmentMeeting = new BigDecimal(0);
            if (departmentMeetingSum.getTotalMeetings() != null && departmentMeetingSum.getTotalMeetings() != 0
                    && deptSumNum != null && deptSumNum != 0)
                avgDepartmentMeeting = intCalculate(departmentMeetingSum.getTotalMeetings(), deptSumNum, 2);
            result.setAvgDepartmentMeeting(avgDepartmentMeeting);
            //上个周期平均会议场次
            BigDecimal LastAvgMeeting = new BigDecimal(0);
            if (lastMeetingsSum.getTotalMeetings() != null && lastMeetingsSum.getTotalMeetings() != 0
                    && deptSumNum != null && deptSumNum != 0)
                LastAvgMeeting = intCalculate(lastMeetingsSum.getTotalMeetings(), deptSumNum, 2);
            //平均会议场次环比增长计算
            String avgMeetingGrowthRate = growthRate(avgDepartmentMeeting, LastAvgMeeting);
            result.setAvgMeetingGrowthRate(avgMeetingGrowthRate);
            //部门平均会议时长
            BigDecimal avgDuration = new BigDecimal(0);
            if (departmentMeetingSum.getTotalDurations() != null && departmentMeetingSum.getTotalDurations() != 0
                    && deptSumNum != null && deptSumNum != 0)
                avgDuration = intCalculate(departmentMeetingSum.getTotalDurations(), deptSumNum, 2).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
            result.setAvgDuration(avgDuration);
            //上个周期平均会议时长
            BigDecimal LastAvgDuration = new BigDecimal(0);
            if (lastMeetingsSum.getTotalDurations() != null && lastMeetingsSum.getTotalDurations() != 0
                    && deptSumNum != null && deptSumNum != 0)
                LastAvgDuration = intCalculate(lastMeetingsSum.getTotalDurations(), deptSumNum, 2).divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
            //平均会议时长环比增长计算
            String avgDurationGrowthRate = growthRate(avgDuration, LastAvgDuration);
            result.setAvgDurationGrowthRate(avgDurationGrowthRate);
            //会议无效占比
            BigDecimal invalid = new BigDecimal(0);
            if (departmentMeetingSum.getTotalMeetings() != null && departmentMeetingSum.getTotalMeetings() != 0
                    && invalidNum != null && invalidNum != 0)
                invalid = intCalculate(invalidNum, departmentMeetingSum.getTotalMeetings(), 4).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
            result.setInvalid(invalid + "%");
            //会议室数量
            LambdaQueryWrapper<MeetingRoom> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MeetingRoom::getDeleted, Delete.NORMAL.getKey());
            queryWrapper.eq(MeetingRoom::getTenantId, param.getTenantId());
            Integer meetingRoomCount = meetingRoomRepository.selectCount(queryWrapper);
            result.setMeetingRoomCount(meetingRoomCount);
            //会议室利用率 每天8小时 一周算五天 一个月算四周
            BigDecimal roomUseRatio = new BigDecimal(0);
            if (meetingRoomCount == null || meetingRoomCount == 0){
                result.setRoomUseRatio("-");
            }else if ( result.getTotalDurations() == null || result.getTotalDurations() == 0){
                result.setRoomUseRatio(new BigDecimal(0) + "%");
            } else {
                roomUseRatio = getRoomUseRatio(new BigDecimal(result.getMeetingRoomCount()), result.getTotalDuration(), param.getTimeType());
                result.setRoomUseRatio(roomUseRatio + "%");
            }
            //上阶段会议室利用率
            BigDecimal lastRoomUseRatio = new BigDecimal(0);
            if (meetingRoomCount != null && meetingRoomCount != 0 && lastMeetingsSum.getTotalDurations() != null && lastMeetingsSum.getTotalDurations() != 0) {
                lastRoomUseRatio = getRoomUseRatio(new BigDecimal(result.getMeetingRoomCount()), lastMeetingsSum.getTotalDuration(), param.getTimeType());
            }
            //会议室利用率环比增长
            String roomUseRatioGrowthRate = growthRate(roomUseRatio, lastRoomUseRatio);
            result.setRoomUseRatioGrowthRate(roomUseRatioGrowthRate);
            //会议室平均会议场次
            if (meetingRoomCount == null || meetingRoomCount == 0 || result.getTotalMeetings() == null || result.getTotalMeetings() == 0) {
                result.setRoomAvgMeetingCount(new BigDecimal(0));
            } else {
                BigDecimal roomAvgMeetingCount = new BigDecimal(result.getTotalMeetings()).divide(new BigDecimal(meetingRoomCount), 2, RoundingMode.HALF_UP);
                result.setRoomAvgMeetingCount(roomAvgMeetingCount);
            }
            return result;
        } catch (Exception e) {
            log.error("查询失败！", e);
            throw GenericException.fail("查询失败！！");
        }
    }


    /**
     * 分页查询所有部门会议统计
     */
    @Override
    public IPage<DepartmentMeetingStats> queryDepartmentMeetingPage(DepartmentMeetingCountParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        //获取苍南公司所有下级部门节点
        List<DepartmentInfoModel> deptTreeNodes = departmentApiService.findSubDepartments(departmentId);
        List<String> departmentIds = new ArrayList<>();
        for (DepartmentInfoModel map : deptTreeNodes) {
            String idValue = map.getId();
            if (idValue != null) {
                departmentIds.add(idValue);
            }
        }
        param.setDepartmentIds(departmentIds);
        if (StringUtils.isEmpty(param.getSortBy())) {
            param.setSortBy("departmentId");
        } else {
            if (!param.getSortBy().equals("totalMeetings") && !param.getSortBy().equals("invalidMeetings")
                    && !param.getSortBy().equals("totalDuration") && !param.getSortBy().equals("signInCount")) {
                throw GenericException.fail("排序参数有误！！");
            }
            if (param.getSortBy().equals("totalDuration")){
                param.setSortBy("totalDurations");
            }
        }
        if (StringUtils.isEmpty(param.getSortOrder())) {
            param.setSortOrder("desc");
        }

        List<DepartmentMeetingStats> dataList = departmentMeetingRepository.queryDeptMeeting(param);
        //手动获取分页数据
        List<DepartmentMeetingStats> paginate = paginate(dataList, param.getCurrent(), param.getSize());
        //转换为小时
        for (DepartmentMeetingStats departmentMeetingStats : paginate) {
            if (departmentMeetingStats.getTotalDurations()!= null && departmentMeetingStats.getTotalDurations()!=0){
                departmentMeetingStats.setTotalDuration(new BigDecimal(departmentMeetingStats.getTotalDurations()).divide(new BigDecimal(60),2,RoundingMode.HALF_UP));
            }else {
                departmentMeetingStats.setTotalDuration(new BigDecimal(0));
            }
        }
        IPage<DepartmentMeetingStats> pageData = new Page<DepartmentMeetingStats>();
        pageData.setCurrent(param.getCurrent());
        pageData.setSize(param.getSize());
        pageData.setTotal(dataList.size());
        pageData.setRecords(paginate);
        return pageData;
    }

    /**
     * 分页查询部门会议详情
     */
    @Override
    public IPage<DepartmentMeetingDetail> queryDepartmentMeetingDetail(DepartmentMeetingCountParam param) {
        param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
        if (param.getDepartmentId() == null) {
            throw GenericException.fail("入参有误，部门id不能为空！！");
        }
        if (StringUtils.isEmpty(param.getSortBy())) {
            param.setSortBy("id");
        } else {
            if (!param.getSortBy().equals("duration") && !param.getSortBy().equals("signInCount")) {
                throw GenericException.fail("排序参数有误！！");
            }
            if (param.getSortBy().equals("duration")){
                param.setSortBy("durations");
            }
        }
        if (StringUtils.isEmpty(param.getSortOrder())) {
            param.setSortOrder("desc");
        }

        List<DepartmentMeetingDetail> departmentMeetingDetails = departmentMeetingRepository.queryDepartmentMeetingDetail(param);
        //手动获取分页数据
        List<DepartmentMeetingDetail> paginate = paginate(departmentMeetingDetails, param.getCurrent(), param.getSize());
        //转换为小时
        for (DepartmentMeetingDetail dm : paginate) {
            if (dm.getDurations()!= null && dm.getDurations()!=0){
                dm.setDuration(new BigDecimal(dm.getDurations()).divide(new BigDecimal(60),2,RoundingMode.HALF_UP));
            }else {
                dm.setDuration(new BigDecimal(0));
            }
        }
        IPage<DepartmentMeetingDetail> pageData = new Page<>();
        pageData.setRecords(paginate);
        pageData.setSize(param.getSize());
        pageData.setCurrent(param.getCurrent());
        pageData.setTotal(departmentMeetingDetails.size());
        return pageData;
    }

    /**
     * 所有部门会议统计导出
     */
    @Override
    public void departmentMeetingExport(DepartmentMeetingCountParam param, HttpServletResponse response) {
        try {
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            //获取苍南公司所有下级部门节点
            List<DepartmentInfoModel> deptTreeNodes = departmentApiService.findSubDepartments(departmentId);
            List<String> departmentIds = new ArrayList<>();
            for (DepartmentInfoModel map : deptTreeNodes) {
                String idValue = map.getId();
                if (idValue != null) {
                    departmentIds.add(idValue);
                }
            }
            param.setDepartmentIds(departmentIds);
            if (StringUtils.isEmpty(param.getSortBy())) {
                param.setSortBy("departmentId");
            } else {
                if (!param.getSortBy().equals("totalMeetings") && !param.getSortBy().equals("invalidMeetings")
                        && !param.getSortBy().equals("totalDuration") && !param.getSortBy().equals("signInCount")) {
                    throw GenericException.fail("排序参数有误！！");
                }
                if (param.getSortBy().equals("totalDuration")){
                    param.setSortBy("totalDurations");
                }
            }
            if (StringUtils.isEmpty(param.getSortOrder())) {
                param.setSortOrder("desc");
            }

            List<DepartmentMeetingStats> dataList = departmentMeetingRepository.queryDeptMeeting(param);
            //转换为小时
            for (DepartmentMeetingStats departmentMeetingStats : dataList) {
                if (departmentMeetingStats.getTotalDurations()!= null && departmentMeetingStats.getTotalDurations()!=0){
                    departmentMeetingStats.setTotalDuration(new BigDecimal(departmentMeetingStats.getTotalDurations()).divide(new BigDecimal(60),2,RoundingMode.HALF_UP));
                }else {
                    departmentMeetingStats.setTotalDuration(new BigDecimal(0));
                }
            }

            exportExcel(response, "部门会议统计", dataList, DepartmentMeetingStats.class, "苍南基地会议考核统计表");
        } catch (IOException e) {
            log.error("导出失败！", e);
            throw GenericException.fail("导出失败！！");
        }
    }

    /**
     * 部门会议详情导出
     */
    @Override
    public void deptMeetingDetailExport(DepartmentMeetingCountParam param, HttpServletResponse response) {
        try {
            param.setTenantId(WebFrameworkUtils.getHeaderTenantId());
            //获取苍南公司所有下级部门节点
            List<DepartmentInfoModel> deptTreeNodes = departmentApiService.findSubDepartments(departmentId);
            List<String> departmentIds = new ArrayList<>();
            for (DepartmentInfoModel map : deptTreeNodes) {
                String idValue = map.getId();
                if (idValue != null) {
                    departmentIds.add(idValue);
                }
            }
            param.setDepartmentIds(departmentIds);
            if (StringUtils.isEmpty(param.getSortBy())) {
                param.setSortBy("id");
            } else {
                if (!param.getSortBy().equals("duration") && !param.getSortBy().equals("signInCount")) {
                    throw GenericException.fail("排序参数有误！！");
                }
                if (param.getSortBy().equals("duration")){
                    param.setSortBy("durations");
                }
            }
            if (StringUtils.isEmpty(param.getSortOrder())) {
                param.setSortOrder("desc");
            }

            List<DepartmentMeetingDetail> departmentMeetingDetails = departmentMeetingRepository.queryDepartmentMeetingDetail(param);
            //转换为小时和状态中文值
            for (DepartmentMeetingDetail departmentMeetingDetail : departmentMeetingDetails) {
                departmentMeetingDetail.setInValidFlagStr(departmentMeetingDetail.getInValidFlag() == 0 ? "有效" : "无效");
                if (departmentMeetingDetail.getDurations()!= null && departmentMeetingDetail.getDurations()!=0){
                    departmentMeetingDetail.setDuration(new BigDecimal(departmentMeetingDetail.getDurations()).divide(new BigDecimal(60),2,RoundingMode.HALF_UP));
                }else {
                    departmentMeetingDetail.setDuration(new BigDecimal(0));
                }
            }
            exportExcel(response, "会议详情统计", departmentMeetingDetails, DepartmentMeetingDetail.class, "苍南园区会议考核详情表");
        } catch (IOException e) {
            log.error("导出失败！", e);
            throw GenericException.fail("导出失败！！");
        }
    }

    /**
     * 导出 Excel 文件
     *
     * @param response HttpServletResponse
     * @param fileName 文件名（无需后缀）
     * @param dataList 数据列表
     * @param clazz    数据模型类
     */
    public static void exportExcel(HttpServletResponse response, String fileName,
                                   List<?> dataList, Class<?> clazz, String title) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName + ".xlsx");

        // 2. 导出 Excel
        ExcelExportUtils.exportExcelWithTwoHeaders(
                response,
                fileName,
                title,
                dataList,
                clazz
        );

    }

    /**
     * 获取会议室利用率
     *
     * @param meetingRoomCount 会议室数量
     * @param totalDuration    总会议时长
     * @param timeType         类型
     * @return
     */
    private BigDecimal getRoomUseRatio(BigDecimal meetingRoomCount, BigDecimal totalDuration, String timeType) {
        BigDecimal roomUseRatio = new BigDecimal("0");
        switch (timeType) {
            case "day":
                roomUseRatio = totalDuration.divide(meetingRoomCount.multiply(new BigDecimal(dayMeetingDuration)), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                break;
            case "week":
                roomUseRatio = totalDuration.divide(meetingRoomCount.multiply(new BigDecimal(weekMeetingDuration)), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                break;
            case "month":
                roomUseRatio = totalDuration.divide(meetingRoomCount.multiply(new BigDecimal(monthMeetingDuration)), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                break;
            case "quarter":
                roomUseRatio = totalDuration.divide(meetingRoomCount.multiply(new BigDecimal(quarterMeetingDuration)), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                break;
            case "year":
                roomUseRatio = totalDuration.divide(meetingRoomCount.multiply(new BigDecimal(yearMeetingDuration)), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                break;
            default:
                throw GenericException.fail("timeType参数错误");
        }
        return roomUseRatio;
    }


    /**
     * 环比增长率计算
     *
     * @param currentValue  当前周期
     * @param previousValue 上个周期
     * @return
     */
    public String growthRate(BigDecimal currentValue, BigDecimal previousValue) {
        // 计算增长量
        BigDecimal growth = currentValue.subtract(previousValue);
        // 计算增长率
        BigDecimal growthRate;
        if (previousValue.compareTo(BigDecimal.ZERO) == 0) {
            // 如果上一个周期的数值为零，避免除以零的错误
            return "-";
        } else {
            growthRate = growth.divide(previousValue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
        }
        return growthRate + "%";
    }

    /**
     * 计算占比
     *
     * @param a 除数
     * @param b 被除数
     * @param c 保留位数
     * @return
     */
    private BigDecimal intCalculate(Integer a, Integer b, Integer c) {
        try {
            // 将Integer转换为BigDecimal
            BigDecimal dividend = new BigDecimal(a);
            BigDecimal divisor = new BigDecimal(b);

            // 进行除法运算，并保留两位小数，四舍五入
            BigDecimal result = dividend.divide(divisor, c, RoundingMode.HALF_UP);
            return result;
        } catch (Exception e) {
            log.error("数据计算失败", e);
            return new BigDecimal(0);
        }
    }

    /**
     * 对列表进行分页
     *
     * @param list       原始列表
     * @param pageNumber 当前页码，从 1 开始
     * @param pageSize   每页的记录数
     * @param <T>        列表元素的类型
     * @return 分页后的列表
     */
    public static <T> List<T> paginate(List<T> list, long pageNumber, long pageSize) {
        if (list == null || list.isEmpty() || pageNumber <= 0 || pageSize <= 0) {
            return new ArrayList<>();
        }
        long startIndex = (pageNumber - 1) * pageSize;
        if (startIndex >= list.size()) {
            return new ArrayList<>();
        }
        int endIndex = Math.toIntExact(Math.min(startIndex + pageSize, list.size()));
        return list.subList((int) startIndex, endIndex);
    }

}
