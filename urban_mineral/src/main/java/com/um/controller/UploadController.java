package com.um.controller;

import com.alibaba.fastjson.JSON;
import com.um.common.constants.ImgConstant;
import com.um.domain.common.Response;
import com.um.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ws
 * @project : com.um
 * @description : 上传控制器
 * @date : 2018/11/14 19:29
 */
@Slf4j
@RestController
public class UploadController extends BaseController{

    @Value("classpath:config.json")
    private Resource resource;

    /**
     * 百度富文本框上传
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/ueditor/upload/uploadImg")
    public String uploadImg(@RequestParam(name = "upfile",required = false) MultipartFile file,HttpServletRequest request){

        try {

            String actionType = request.getParameter("action");
            if(StringUtils.isEmpty(actionType)){
                return null;
            }

            if("config".equals(actionType)){
                String configJsonData =  IOUtils.toString(resource.getInputStream(), Charset.forName("UTF-8"));
                return configJsonData;
            }else if("uploadImg".equals(actionType)){

                if(file.isEmpty()){
                    log.error("文件不能为空");
                    return null;
                }

                //文件名
                String fileName = file.getOriginalFilename();
                //前缀，协议域名端口
                String prefix = request.getScheme() + "://" + request.getServerName();
                //后缀，文件后缀名
                String suffix = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());

                //图片验证
                //判断图片大小
                int size = (int) file.getSize();
                if(size > ImgConstant.IMG_STORAGE_SIZE){
                    log.error("图片太大，请压缩到5M内");
                    return null;
                }

                //判断图片后缀
                String [] suffixArr = ImgConstant.IMG_SUFFIX.split("\\|");
                List<String> suffixList = Arrays.asList(suffixArr);
                if(!suffixList.contains(suffix)){
                    log.error("图片格式不正确，请使用jpg|png|gif|jpeg|JPG|PNG|GIF|JPEG");
                    return null;
                }

                //文件写入绝对路径
                String fileDestination = ImgConstant.ROOT_PATH + "news/";

                //文件访问路径，被nginx转发至/data/static/img/{imgAccessPath}
                String imgAccessPath = "/news/";


                //随机出新的文件名，避免覆盖
                String newFileName = System.currentTimeMillis() + NumberUtil.generateNumber(3) + "." + suffix;

                fileDestination += newFileName;
                imgAccessPath += newFileName;

                //生成空文件
                File destination = new File(fileDestination);

                //把文件流写入新文件
                FileUtils.copyInputStreamToFile(file.getInputStream(),destination);

                Map<String,String> resMap = new HashMap<>();
                resMap.put("state","SUCCESS");
                resMap.put("url",prefix + imgAccessPath);
                log.info("百度富文本上传路径：" + (prefix + imgAccessPath));

                return JSON.toJSONString(resMap);
            }

        } catch (Exception e) {
            log.error("---uploadImg error",e);
            return null;
        }

        return null;
    }


    /**
     * 平台图片上传
     * @param file
     * @param type
     * @param request
     * @return
     */
    @PostMapping("/platform/upload/uploadImg")
    public Response uploadImg(@RequestParam("fileName") MultipartFile file, @RequestParam(value = "type",required = false) Integer type, HttpServletRequest request){

        Response response = new Response();


        try {

            if(file.isEmpty()){
                response.setResult(0);
                response.setFailReason("文件不能为空");
                return response;
            }

            if(null == type){
                type = 3;
            }

            //文件名
            String fileName = file.getOriginalFilename();
            //前缀，协议域名
            String prefix = request.getScheme() + "://" + request.getServerName();
            //后缀，文件后缀名
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());

            //图片验证
            //判断图片大小
            int size = (int) file.getSize();
            if(size > ImgConstant.IMG_STORAGE_SIZE){
                response.setResult(0);
                response.setFailReason("图片太大，请压缩到5M内");
                return response;
            }

            //判断图片后缀
            String [] suffixArr = ImgConstant.IMG_SUFFIX.split("\\|");
            List<String> suffixList = Arrays.asList(suffixArr);
            if(!suffixList.contains(suffix)){
                response.setResult(0);
                response.setFailReason("图片格式不正确，请使用jpg|png|gif|jpeg|JPG|PNG|GIF|JPEG");
                return response;
            }

            //文件写入绝对路径
            String fileDestination = ImgConstant.ROOT_PATH;

            //文件访问路径，被nginx转发至/data/static/img/{imgAccessPath}
            String imgAccessPath = "";


            switch (type){
                case 1:
                    fileDestination += "tradeinfo/";
                    imgAccessPath += "/tradeinfo/";
                    break;
                case 2:
                    fileDestination += "statement/";
                    imgAccessPath += "/statement/";
                    break;
            }

            //随机出新的文件名，避免覆盖
            String newFileName = System.currentTimeMillis() + NumberUtil.generateNumber(3) + "." + suffix;

            fileDestination += newFileName;
            imgAccessPath += newFileName;

            //生成空文件
            File destination = new File(fileDestination);

            //把文件流写入新文件
            FileUtils.copyInputStreamToFile(file.getInputStream(),destination);

            response.setModel(prefix + imgAccessPath);
            log.info("图片上传路径：" + response.getModel().toString());
            response.setResult(1);

        } catch (Exception e) {
            log.error("---uploadImg error",e);
            response.setResult(0);
            response.setFailReason("上传图片失败");
        }

        return response;
    }




}
