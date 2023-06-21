package com.github.ulwx.aka.ueditor.utils;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix =  "aka.ueditor" )
public class AkaFileUeditorSetting {
//    uploader-plugin: com.baidu.ueditor.support.MyUploader
//    #直连上传的URL，用于图片，文件，视频上传的直连URL
//    direct-upload-url: http://localhost:9090/fileserver/uploadfile/upload-Upload-genBean.action
    private String uploaderPlugin;
    private String directUploadUrl;

    public String getUploaderPlugin() {
        return uploaderPlugin;
    }

    public void setUploaderPlugin(String uploaderPlugin) {
        this.uploaderPlugin = uploaderPlugin;
    }

    public String getDirectUploadUrl() {
        return directUploadUrl;
    }

    public void setDirectUploadUrl(String directUploadUrl) {
        this.directUploadUrl = directUploadUrl;
    }
}
