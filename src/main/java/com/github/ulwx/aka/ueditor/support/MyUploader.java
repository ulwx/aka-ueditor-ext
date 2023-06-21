package com.github.ulwx.aka.ueditor.support;

import com.alibaba.fastjson.JSONObject;
import com.baidu.ueditor.upload.FileStorageInfo;
import com.baidu.ueditor.upload.StorageManager;
import com.baidu.ueditor.upload.UploaderPlugin;
import com.github.ulwx.aka.ueditor.utils.MyBaiduAppConfig;
import com.ulwx.tool.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyUploader implements UploaderPlugin {
    private static Logger log = LoggerFactory.getLogger(MyUploader.class);
    @Override
    public FileStorageInfo upload(InputStream is, long maxSize) throws Exception {
        String url = MyBaiduAppConfig.getDirectUploadUrl();
        Map<String, Object> map = new HashMap<>();
        File upFile = saveTmpFile(is, maxSize);
        map.put("file", upFile);
        map.put("type", 3);
        map.put("ftype", 1);
        map.put("dir", "ueditor");
        map.put("id", "");
        map.put("userid", "");
        map.put("requestid", SnowflakeIdWorker.instance.nextId());
        map.put("timestamp", CTime.getLongMillsecs());
        String ret = HttpUtils.postFiles(url, map);
        
        
        log.debug("ret="+ret);

        Map retMap = (Map) JSONObject.parse(ret);
        String msg=(String)retMap.get("message");
        int status=(Integer)retMap.get("status");
        if(status==1) {
            Map data=(Map)retMap.get("data");
            String httpUrl= StringUtils.trim((String)data.get("httpPath"));
            String retUrl=StringUtils.trim((String)data.get("ossHttpPath"));
            if(StringUtils.hasText(httpUrl)){
                retUrl=httpUrl;
            }
            String fname= FileUtils.getFileName(retUrl);

            FileStorageInfo fs = new FileStorageInfo();
            fs.setOriginal(fname);
            fs.setSize(upFile.length());
            fs.setTitle(fname);
            fs.setType(FileUtils.getTypePart((String) data.get("relaFilePath")));
            fs.setUrl(retUrl);
            return fs;
        }else {
            throw new Exception(msg);
        }
    }

    private static File getTmpFile() {
        File tmpDir = org.apache.commons.io.FileUtils.getTempDirectory();
        String tmpFileName = (Math.random() * 10000 + "").replace(".", "");
        return new File(tmpDir, tmpFileName);
    }

    private static File saveTmpFile(InputStream is, long maxSize) throws Exception {

        File tmpFile = getTmpFile();

        byte[] dataBuf = new byte[2048];
        BufferedInputStream bis = new BufferedInputStream(is, StorageManager.BUFFER_SIZE);

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile),
                    StorageManager.BUFFER_SIZE);

            int count = 0;
            while ((count = bis.read(dataBuf)) != -1) {
                bos.write(dataBuf, 0, count);
            }
            bos.flush();
            bos.close();

            if (tmpFile.length() > maxSize) {
                tmpFile.delete();
                throw new Exception("文件过大，请确认！");
            }

            return tmpFile;

        } catch (Exception e) {
            log.error(e + "", e);
            throw e;
        }

    }
}
