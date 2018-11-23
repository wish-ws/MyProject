package com.um.service;

import com.um.domain.common.BaseRequest;
import com.um.domain.common.PaginationSupportDTO;
import com.um.domain.dto.ClientFeedbackDTO;
import com.um.domain.dto.PlatformStatisticsDTO;
import com.um.domain.request.LogQueryRequest;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 16:51
 */
public interface BackstageService {

    PlatformStatisticsDTO queryPlatformStatistics();

    PaginationSupportDTO queryFeedbackPage(BaseRequest baseRequest);

    PaginationSupportDTO querySystemLogPage(LogQueryRequest logQueryRequest);

    void addFeedback(ClientFeedbackDTO clientFeedbackDTO);
}
