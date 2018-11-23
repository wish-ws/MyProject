package com.um.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.um.domain.common.Response;
import com.um.domain.request.AliyunSmsRequest;
import com.um.service.AliyunSmsService;
import com.um.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author : ws
 * @project : com.um
 * @description : 短信发送服务,如需扩展，可在外面再包装一层业务
 * @date : 2018/11/22 17:20
 */
@Slf4j
@Service
public class AliyunSmsServiceImpl implements AliyunSmsService {


    @Override
    public boolean sendSms(AliyunSmsRequest aliyunSmsRequest) {

        boolean sendSuccess = false;
        if(StringUtils.isEmpty(aliyunSmsRequest.getPhoneNumbers())){
            log.error("发送短信：发送手机号不能为空");
            return false;
        }
        if(StringUtils.isEmpty(aliyunSmsRequest.getSignName())){
            log.error("发送短信：签名不能为空");
            return false;
        }
        if(StringUtils.isEmpty(aliyunSmsRequest.getTemplateCode())){
            log.error("发送短信：模板编号不能为空");
            return false;
        }
        if(StringUtils.isEmpty(aliyunSmsRequest.getTemplateParam())){
            log.error("发送短信：模板参数不能为空");
            return false;
        }

        try {
            Response response = SmsUtil.sendSms(aliyunSmsRequest);
            if(1 == response.getResult()){
                return true;
            }else{
                return false;
            }
        } catch (ClientException e) {
            log.error("发送短信失败",e);
            return false;
        }

    }
}
