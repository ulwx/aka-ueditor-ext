package com.baidu.ueditor.upload;

import java.io.InputStream;

public abstract interface UploaderPlugin {
	public abstract FileStorageInfo upload(InputStream paramInputStream, long paramLong) throws Exception;
}