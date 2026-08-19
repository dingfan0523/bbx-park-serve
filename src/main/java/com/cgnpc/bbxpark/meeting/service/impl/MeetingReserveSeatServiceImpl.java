
package com.cgnpc.bbxpark.meeting.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.common.utils.BeanUtils;
import com.cgnpc.bbxpark.meeting.domain.MeetingReserveSeat;
import com.cgnpc.bbxpark.meeting.dto.model.MeetingReserveSeatModel;
import com.cgnpc.bbxpark.meeting.dto.param.MeetingReserveSeatParam;
import com.cgnpc.bbxpark.meeting.mapper.MeetingReserveSeatRepository;
import com.cgnpc.bbxpark.meeting.service.IMeetingReserveSeatService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/***
 * @Description 会议预约座位服务实现
 * @author huangyongtao
 * @date 2024/12/24 14:16
 */
@Service("meetingReserveSeatService")
public class MeetingReserveSeatServiceImpl extends ServiceImpl<MeetingReserveSeatRepository, MeetingReserveSeat> implements IMeetingReserveSeatService {

    @Override
    public Boolean addBatch(Long reserveId, List<MeetingReserveSeatParam> params) {
        List<MeetingReserveSeat> seats = params.stream().map(param->{
            MeetingReserveSeat seat = BeanUtils.convertTo(param, MeetingReserveSeat::new);
            seat.setId(null);
            seat.setReserveId(reserveId);
            return seat;
        }).collect(Collectors.toList());
        return saveBatch(seats);
    }

    @Override
    public List<MeetingReserveSeatModel> findByReserveId(Long reserveId) {
        List<MeetingReserveSeat> list = list(Wrappers.<MeetingReserveSeat>lambdaQuery().eq(MeetingReserveSeat::getReserveId, reserveId));
        return BeanUtils.convertListTo(list, MeetingReserveSeatModel::new);
    }

    @Override
    public Boolean removeByReserveId(Long reserveId) {
        return remove(Wrappers.<MeetingReserveSeat>lambdaQuery().eq(MeetingReserveSeat::getReserveId, reserveId));
    }
}
