package com.cgnpc.ereport.service;

import com.cgnpc.ereport.model.ReportTokenModel;

/**
 * @author P629988
 */
public interface IEreportConfigService {

    ReportTokenModel getAuthToken();

    String getEreportCode();

}
