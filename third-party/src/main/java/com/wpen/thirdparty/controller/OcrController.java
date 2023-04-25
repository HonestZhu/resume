package com.wpen.thirdparty.controller;

import com.alibaba.fastjson.JSONObject;
import com.chinamobile.cmss.sdk.ocr.ECloudDefaultClient;
import com.chinamobile.cmss.sdk.ocr.http.constant.Region;
import com.chinamobile.cmss.sdk.ocr.http.signature.Credential;
import com.chinamobile.cmss.sdk.ocr.request.IECloudRequest;
import com.chinamobile.cmss.sdk.ocr.request.ocr.OcrRequestFactory;

import java.io.IOException;
import java.util.HashMap;

import com.wpen.thirdparty.util.FileHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/ocr")
public class OcrController {

    @Value("${ecloud.user_ak}")
    private String userAk;

    @Value("${ecloud.user_sk}")
    private String userSk;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException, IllegalArgumentException {
        if(file.isEmpty()) {
            System.out.println("empty");
            return "";
        }



        String contentType = file.getContentType();
        String base64code = "";
        // 处理docx文件和pdf文件
        if (contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") || contentType.equals("application/msword") || contentType.equals("application/pdf")) {
            base64code = FileHandler.convertFileToBase64(file);
        } // 处理图片文件
        else if (contentType.equals("image/jpeg") || contentType.equals("image/png")) {
            base64code = FileHandler.convertPictrueToBase64(file);
        } else {
            throw new IllegalArgumentException();
        }
        return "<img src=\"data:image/png;base64, " + base64code + "\">";
//        Credential credential = new Credential(userAk, userSk);
//        ECloudDefaultClient client = new ECloudDefaultClient(credential, Region.POOL_SZ);
//
//        HashMap<String, Object> generalParams = new HashMap<>();
//        JSONObject generalOptions = new JSONObject();
//        generalOptions.put("rotate_180", true);
//        generalOptions.put("language", "zh");
//        generalParams.put("options", generalOptions);
//
//
//        //参数为图片的base64编码
//        IECloudRequest generalRequestBase64 = OcrRequestFactory.getOcrBase64Request("/api/ocr/v1/general",
//                base64code, generalParams);
//
//        try {
//
//            JSONObject responseBase64 = (JSONObject) client.call(generalRequestBase64);
//            System.out.println(responseBase64.toJSONString());
//            return responseBase64.toJSONString();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "";
    }
}
