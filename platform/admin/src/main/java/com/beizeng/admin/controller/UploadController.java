package com.beizeng.admin.controller;

import com.beizeng.admin.common.utils.qiniu.QiniuUtil;
import com.liren.basic.common.response.JsonReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * @description: <h1>UploadController 上传控制类</h1>
 * @author:
 **/

@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    private JsonReturn postUserInforUpDate(HttpServletRequest request, @RequestParam("files") MultipartFile[] files) throws IOException {

        if (files == null) {
            return JsonReturn.send(JsonReturn.CODE_ERROR, "参数错误");
        }
        List<String> list = new LinkedList<String>();
        //给文件添加一个随机的 文件名称字符串
        String imagesKey = UUID.randomUUID().toString();

        for (MultipartFile file : files) {
            String imagesName = imagesKey;
            imagesName = "cut/" + imagesName + "/";
            String qnUrl = QiniuUtil.uploadToQn(file, imagesName);
            System.out.println("qnUrl = " + qnUrl);
            list.add(qnUrl);
        }
        log.info("list：{}", list);
        return JsonReturn.send(JsonReturn.CODE_SUCCESS, "上传成功", list);
    }
}
