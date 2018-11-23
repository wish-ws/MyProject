package com.um.mapper;

import com.um.common.MyMapper;
import com.um.domain.common.BaseRequest;
import com.um.domain.dto.ClientFeedbackDTO;
import com.um.domain.po.ClientFeedbackPO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : ws
 * @project : com.um
 * @description :
 * @date : 2018/11/15 15:19
 */
@Component("feedbackMapper")
public interface FeedbackMapper extends MyMapper<ClientFeedbackPO> {

    List<ClientFeedbackDTO> queryFeedbackPage(BaseRequest baseRequest);
}
