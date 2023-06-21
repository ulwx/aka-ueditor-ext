package com.baidu.ueditor.upload;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

public final class Base64Uploader {
	private static Logger log = LoggerFactory.getLogger(Base64Uploader.class);

	public static State save(String content, Map<String, Object> conf) {
		byte[] data = decode(content);

		long maxSize = ((Long) conf.get("maxSize")).longValue();
		if (!validSize(data, maxSize)) {
			return new BaseState(false, 1);
		}
		String suffix = FileType.getSuffix("JPG");

		String savePath = PathFormat.parse((String) conf.get("savePath"), (String) conf.get("filename"));

		savePath = savePath + suffix;
		String physicalPath = (String) conf.get("rootPath") + savePath;

		String uploadClass = (String) conf.get("uploadClass");
		if ((uploadClass != null) && (!uploadClass.isEmpty())) {
			try {
				Class clzz = Class.forName(uploadClass);
				UploaderPlugin up = (UploaderPlugin) clzz.newInstance();
				InputStream is = new ByteArrayInputStream(data);
				FileStorageInfo fstateInfo = up.upload(is, maxSize);

				BaseState state = new BaseState(true);
				state.putInfo("size", fstateInfo.getSize());
				state.putInfo("title", fstateInfo.getTitle());

				state.putInfo("url", fstateInfo.getUrl());
				state.putInfo("type", fstateInfo.getType());
				state.putInfo("original", fstateInfo.getOriginal());

				return state;
			} catch (Exception e) {
				log.error(e+"",e);
				return new BaseState(false, 3);
			}
		}
		State storageState = StorageManager.saveBinaryFile(data, physicalPath);
		if (storageState.isSuccess()) {
			storageState.putInfo("url", PathFormat.format(savePath));
			storageState.putInfo("type", suffix);
			storageState.putInfo("original", "");
		}
		return storageState;
	}
	final static Base64.Decoder decoder = Base64.getDecoder();
	final static Base64.Encoder encoder = Base64.getEncoder();
	private static byte[] decode(String content) {
		return decoder.decode(content);
	}

	private static boolean validSize(byte[] data, long length) {
		return data.length <= length;
	}
}