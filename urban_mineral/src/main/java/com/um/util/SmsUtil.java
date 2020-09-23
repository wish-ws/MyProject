package com.um.util;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.um.domain.common.Response;
import com.um.domain.request.AliyunSmsRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * @author : ws
 * @project : com.um
 * @description : 阿里云短信服务工具类
 * @date : 2018/11/22 15:57
 */
@Slf4j
public class SmsUtil {

    //产品名称:云通信短信API产品,开发者无需替换
    private static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    private static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    private static final String accessKeyId = "123";
    private static final String accessKeySecret = "123";

    private static IAcsClient acsClient;


    public static Response sendSms(AliyunSmsRequest aliyunSmsRequest) throws ClientException {
        log.info("发送短信，请求参数：" + JSON.toJSONString(aliyunSmsRequest));
        Response response = new Response();

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        if(null == acsClient){
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            acsClient = new DefaultAcsClient(profile);
        }

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(aliyunSmsRequest.getPhoneNumbers());
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(aliyunSmsRequest.getSignName());
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(aliyunSmsRequest.getTemplateCode());
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam(aliyunSmsRequest.getTemplateParam());

        //hint 此处可能会抛出异常，注意catch
        try {
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if(null != sendSmsResponse && sendSmsResponse.getCode().equals(ResCodeEnum.OK.code)){
                log.info("短信发送成功，返回结果：" + JSON.toJSONString(sendSmsResponse));
                response.setResult(1);
            }else{
                log.info("短信发送失败,原因：" + (null == sendSmsResponse?"未返回结果":ResCodeEnum.getDescByCode(sendSmsResponse.getCode())));
                response.setResult(0);
                response.setFailReason(null == sendSmsResponse?"未返回结果":ResCodeEnum.getDescByCode(sendSmsResponse.getCode()));
            }
        } catch (ClientException e) {
            log.error("短信发送异常",e);
            response.setResult(0);
            response.setFailReason("短信发送异常");
        }
        return response;
    }


    public enum ResCodeEnum{
        OK("OK","请求成功"),
        RAM_PERMISSION_DENY("isp.RAM_PERMISSION_DENY","RAM权限DENY"),
        OUT_OF_SERVICE("isv.OUT_OF_SERVICE","业务停机"),
        PRODUCT_UN_SUBSCRIPT("isv.PRODUCT_UN_SUBSCRIPT","未开通云通信产品的阿里云客户"),
        PRODUCT_UNSUBSCRIBE("isv.PRODUCT_UNSUBSCRIBE","产品未开通"),
        ACCOUNT_NOT_EXISTS("isv.ACCOUNT_NOT_EXISTS","账户不存在"),
        ACCOUNT_ABNORMAL("isv.ACCOUNT_ABNORMAL","账户异常"),
        SMS_TEMPLATE_ILLEGAL("isv.SMS_TEMPLATE_ILLEGAL","短信模板不合法"),
        SMS_SIGNATURE_ILLEGAL("isv.SMS_SIGNATURE_ILLEGAL","短信签名不合法"),
        INVALID_PARAMETERS("isv.INVALID_PARAMETERS","参数异常"),
        SYSTEM_ERROR("isp.SYSTEM_ERROR","系统错误"),
        MOBILE_NUMBER_ILLEGAL("isv.MOBILE_NUMBER_ILLEGAL","非法手机号"),
        MOBILE_COUNT_OVER_LIMIT("isv.MOBILE_COUNT_OVER_LIMIT","手机号码数量超过限制"),
        TEMPLATE_MISSING_PARAMETERS("isv.TEMPLATE_MISSING_PARAMETERS","模板缺少变量"),
        BUSINESS_LIMIT_CONTROL("isv.BUSINESS_LIMIT_CONTROL","业务限流"),
        INVALID_JSON_PARAM("isv.INVALID_JSON_PARAM","JSON参数不合法，只接受字符串值");

        public String code;

        public String desc;

        ResCodeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }


        public static String getDescByCode(String code){
            String desc = null;
            if(StringUtils.isEmpty(code)){
                return desc;
            }
            for (ResCodeEnum resCodeEnum : ResCodeEnum.values()) {
                if(resCodeEnum.code.equals(code)){
                    desc = resCodeEnum.desc;
                }
            }
            return desc;
        }
    }


}
