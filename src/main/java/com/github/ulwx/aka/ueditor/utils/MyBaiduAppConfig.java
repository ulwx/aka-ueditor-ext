package com.github.ulwx.aka.ueditor.utils;

import com.github.ulwx.aka.webmvc.BeanGet;

public class MyBaiduAppConfig {

    public static String getFileUploadPlugin(){
        AkaFileUeditorSetting akaFileUeditorSetting=BeanGet.getBean(AkaFileUeditorSetting.class);
        return akaFileUeditorSetting.getUploaderPlugin();
    }
    public static String getDirectUploadUrl(){
        AkaFileUeditorSetting akaFileUeditorSetting=BeanGet.getBean(AkaFileUeditorSetting.class);
        return akaFileUeditorSetting.getDirectUploadUrl();
    }


}
