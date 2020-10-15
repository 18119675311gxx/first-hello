package com.beizeng.admin.common.utils.qiniu;

import com.beizeng.admin.common.config.QiniuConfig;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @description: <h1>QiniuUtil 七牛上传工具类</h1>
 * 没事别瞎动哦！！！
 * @author:
 **/
@Component
public class QiniuUtil {

    private static QiniuConfig qiniuConfig;

    public QiniuUtil(QiniuConfig qiniuConfig) {
        this.qiniuConfig = qiniuConfig;
    }

    private static Configuration cfg = new Configuration();
    private static UploadManager uploadManager = new UploadManager(cfg);

    /**
     * @param file     上传的文件
     * @param fileName 文件的路径
     * @Description: <h2>将文件上传到七牛云</h2>
     * @return: {@link String}  上传路径
     * @author:
     */
    public static String uploadToQn(MultipartFile file, String fileName) {
        String path = "";
        if (!file.isEmpty() && StringUtils.isNotEmpty(fileName)) {
            String fileType = file.getOriginalFilename().toString();
//          String fileType = multipartFile.getOriginalFilename().substring( multipartFile.getOriginalFilename().lastIndexOf("."));
            try {
                path = uploadFile(file.getInputStream(), fileName + fileType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return path;
        }
        return null;
    }

    /**
     * @param bytes    要上传 bytes
     * @param fileName 文件路径
     * @Description: <h2>将bytes上传到七牛云</h2>
     * @return: {@link String}
     * @author:
     * @Date: 2020/7/6
     */
    public static String uploadToQn(byte[] bytes, String fileName) {
        String path = "";
        if (bytes != null && bytes.length != 0 && StringUtils.isNotEmpty(fileName)) {
            path = uploadFile(bytes, fileName);
            return path;
        }
        return null;
    }

    private static byte[] input2byte(InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        try {
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    private static String getUpToken(String fileName) {
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        return auth.uploadToken(qiniuConfig.getBucket(), fileName);
    }

    private String getUpToken() {
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        return auth.uploadToken(qiniuConfig.getBucket());
    }

    private static boolean upload(byte[] data, String targetName, String upToken) {
        try {

            Response res = uploadManager.put(data, targetName, upToken);
            if (res.isOK()) {
                return true;
            } else {
                return false;
            }

        } catch (QiniuException e) {
            Response r = e.response;
            System.out.println(r.toString());
        }
        return false;
    }

    public static String uploadFile(byte[] fileByte, String targetName) {
        if (upload(fileByte, targetName, getUpToken(targetName))) {
            return qiniuConfig.getBaseUrl() + targetName;
        }
        return "";
    }

    public static String uploadFile(InputStream inStream, String targetName) {
        if (upload(input2byte(inStream), targetName, getUpToken(targetName))) {
            return qiniuConfig.getBaseUrl() + targetName;
        }
        return "";
    }

}
