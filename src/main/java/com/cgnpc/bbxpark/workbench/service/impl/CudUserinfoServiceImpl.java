package com.cgnpc.bbxpark.workbench.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cgnpc.bbxpark.workbench.domain.CudUserinfo;
import com.cgnpc.bbxpark.workbench.dto.CudUserinfoDto;
import com.cgnpc.bbxpark.workbench.mapper.CudUserinfoMapper;
import com.cgnpc.bbxpark.workbench.service.CudUserinfoService;
import com.cgnpc.cud.cache.redis.RedisUtil;
import com.cgnpc.framework.filter.UserOnlineFilterChain;
import com.cgnpc.pro.model.respvo.CudUserInfoVO;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 用途说明: 保存用户个性化设置
 * 作者姓名: P633860
 * 创建时间: 2024/6/11
 */
@Service
public class CudUserinfoServiceImpl extends ServiceImpl<CudUserinfoMapper, CudUserinfo> implements CudUserinfoService {

    @Override
    public boolean setTheme(CudUserinfoDto dto) {
        CudUserinfo userinfo = new CudUserinfo();
        BeanUtils.copyProperties(dto,userinfo);
        BeanUtils.copyProperties(dto.getBackgroundSettings(),userinfo);
        BeanUtils.copyProperties(dto.getCardSettings(),userinfo);
        BeanUtils.copyProperties(dto.getTableSettings(),userinfo);
        BeanUtils.copyProperties(dto.getButtonFormSettings(),userinfo);
        LambdaQueryWrapper<CudUserinfo> query = new LambdaQueryWrapper<>();
        query.eq(CudUserinfo::getUserId, userinfo.getUserId());
        if (ObjectUtil.isEmpty(this.getOne(query))) {
            userinfo.setCreateDate(new Date());
            return this.save(userinfo);
        } else {
            return this.update(userinfo,query);
        }
    }

    @Override
    public CudUserinfoDto getTheme(String userId) {
        CudUserinfoDto dto = new CudUserinfoDto();
        LambdaQueryWrapper<CudUserinfo> query = new LambdaQueryWrapper<>();
        query.eq(CudUserinfo::getUserId, userId);
        CudUserinfo userinfo = this.getOne(query);
        BeanUtils.copyProperties(userinfo,dto);

        CudUserinfoDto.BackgroundSettings backgroundSettings = new CudUserinfoDto.BackgroundSettings();
        BeanUtils.copyProperties(userinfo,backgroundSettings);
        dto.setBackgroundSettings(backgroundSettings);

        CudUserinfoDto.CardSettings cardSettings = new CudUserinfoDto.CardSettings();
        BeanUtils.copyProperties(userinfo,cardSettings);
        dto.setCardSettings(cardSettings);

        CudUserinfoDto.TableSettings tableSettings = new CudUserinfoDto.TableSettings();
        BeanUtils.copyProperties(userinfo,tableSettings);
        dto.setTableSettings(tableSettings);

        CudUserinfoDto.ButtonFormSettings buttonFormSettings = new CudUserinfoDto.ButtonFormSettings();
        BeanUtils.copyProperties(userinfo,buttonFormSettings);
        dto.setButtonFormSettings(buttonFormSettings);
        return dto;
    }

    /**
     * 统计在线用户
     */
    @SuppressWarnings("all")
    @Override
    public List<Map<String, Object>> getOnlineUsers(){
        List<Map<String, Object>> onlineUsers = new ArrayList<>();
        try {
            Set<String> getOnlineUsers  = RedisUtil.getOnlineUsers();
            Set<String> set1 =  new HashSet<>();
            Set<String> set2 =  new HashSet<>();
            List<Map<String, Object>> tmp = new ArrayList<>();
            for (String user : getOnlineUsers) {
                Map<String, Object> oumap    = new HashMap<>();
                Map<String, Object> getOumap = (Map<String, Object>) RedisUtil.get(user);
                oumap.putAll(getOumap);
                oumap.put("pc","");
                oumap.put("mobile","");

                //device type
                if (!getOumap.isEmpty() &&getOumap.get("pc") != null){
                    long delayedTimestamp = Long.valueOf(getOumap.get("pc").toString());
                    if (delayedTimestamp > UserOnlineFilterChain.currentTimestamp()){
                        oumap.put("pc",1);
                    }
                }
                if (!getOumap.isEmpty() &&getOumap.get("mobile") != null){
                    long delayedTimestamp = Long.valueOf(getOumap.get("mobile").toString());
                    if (delayedTimestamp > UserOnlineFilterChain.currentTimestamp()){
                        oumap.put("mobile",1);
                    }
                }
                String userId = user.replace("online_users:","");
                oumap.put("logins",RedisUtil.size(userId));
                tmp.add(oumap);
                set1.add(userId);
            }

            //online users
            HashMap oum = Maps.newHashMap();
            oum.put("onlineUsers",tmp);
            onlineUsers.add(oum);

            Set<String> getLogins = RedisUtil.getLogins();
            for (String user : getLogins) {
                set2.add(user.replace("logins:",""));
            }

            //offline user
            Set<String> intersection = new HashSet<>(set1);
            intersection.retainAll(set2);
            Set<String> uids = new HashSet<>(set2);
            uids.removeAll(intersection);
            if (CollUtil.isNotEmpty(uids)){
                List<?> offlineUsers = uids.stream().map(e -> {
                    HashMap m = Maps.newHashMap();
                    m.put("userId",e);
                    m.put("logins",RedisUtil.size(e));
                    return m;
                }).collect(Collectors.toList());
                HashMap m = Maps.newHashMap();
                m.put("offlineUsers",offlineUsers);
                onlineUsers.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return onlineUsers;
    }

    /**
     * 获取所在的部门信息
     */
    @Override
    public HashMap<String, Object> getDepartmentInfo(HashMap<String, Object> result, CudUserInfoVO vo){
        result.put("userName",vo.getUserName());
        result.put("userNamePy",vo.getUserNamePy());
        result.put("nowUserName",vo.getNowUserName());
        int lastIndexOfBackslash = vo.getUserDeptNamePath().lastIndexOf('\\');
        if (lastIndexOfBackslash != -1) {
            result.put("userDeptName",vo.getUserDeptNamePath().substring(lastIndexOfBackslash + 1));
            String[] a = vo.getUserDeptNamePath().split("\\\\");
            if (a.length >= 3) {
                result.put("userDeptName", a[a.length - 3] + "/" + a[a.length - 2] + "/" + a[a.length - 1]);
            }
        } else {
            result.put("userDeptName",vo.getUserDeptName());
        }
        return result;
    }

}
