package com.cgnpc.bbxpark.acl.haikang.service;


import com.cgnpc.bbxpark.acl.haikang.model.PlayBackURLsModel;
import com.cgnpc.bbxpark.acl.haikang.model.PlayBackURLsParam;
import com.cgnpc.bbxpark.acl.haikang.model.PlayPreviewURLsParam;

/**
 * @Auther: ccy
 * @Date: 2023/10/31
 * @Description:
 */
public interface HkVideoService {
    PlayBackURLsModel getPlaybackURLs(PlayBackURLsParam params);

    PlayBackURLsModel getPlayliveURLs(PlayPreviewURLsParam params);
}
