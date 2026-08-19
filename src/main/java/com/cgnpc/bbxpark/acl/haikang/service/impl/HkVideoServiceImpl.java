package com.cgnpc.bbxpark.acl.haikang.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.cgnpc.bbxpark.acl.haikang.model.HkResult;
import com.cgnpc.bbxpark.acl.haikang.model.PlayBackURLsModel;
import com.cgnpc.bbxpark.acl.haikang.model.PlayBackURLsParam;
import com.cgnpc.bbxpark.acl.haikang.model.PlayPreviewURLsParam;
import com.cgnpc.bbxpark.acl.haikang.service.HkVideoService;
import com.cgnpc.bbxpark.acl.haikang.util.HaikangApiEnum;
import com.cgnpc.bbxpark.acl.haikang.util.HikHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Auther: ccy
 * @Date: 2023/10/31
 * @Description:
 */
@Slf4j
@Service
public class HkVideoServiceImpl implements HkVideoService {

    @Override
    public PlayBackURLsModel getPlaybackURLs(PlayBackURLsParam params) {
        String beginTime = params.getBeginTime().replace(" ", "T") + ".000+08:00";
        String endTime = params.getEndTime().replace(" ", "T") + ".000+08:00";
        params.setBeginTime(beginTime);
        params.setEndTime(endTime);
        if(StringUtils.isBlank(params.getProtocol())){
            params.setProtocol("ws");
        }
        HkResult result = HikHttpUtil.doPostStringArtemis(HaikangApiEnum.PLAYBACKURLS, JSON.toJSONString(params));
        return JSONUtil.toBean(JSONUtil.toJsonStr(result.getData()), PlayBackURLsModel.class);
    }

    @Override
    public PlayBackURLsModel getPlayliveURLs(PlayPreviewURLsParam params) {
        if(StringUtils.isBlank(params.getProtocol())){
            params.setProtocol("ws");
        }
        HkResult result = HikHttpUtil.doPostStringArtemis(HaikangApiEnum.PLAYLIVEURLS, JSON.toJSONString(params));
        return JSONUtil.toBean(JSONUtil.toJsonStr(result.getData()), PlayBackURLsModel.class);
    }
}
