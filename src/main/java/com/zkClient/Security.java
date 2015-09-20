package com.zkClient;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import sun.misc.BASE64Encoder;



/**   
 * @Title: Sercurt.java 
 * @Package com.zkClient 
 * @Description: TODO 
 * @author 陈凯 chenkai-ds@petrochina.com.cn   
 * @date 2014-12-18 下午11:05:08 
 * @version V1.0   
 */

public class Security {

	public void genKeyAes() throws NoSuchAlgorithmException{
		KeyGenerator instance = KeyGenerator.getInstance("AES");
		instance.init(128);
		SecretKey key = instance.generateKey();
		BASE64Encoder base64Encoder = new BASE64Encoder();
		base64Encoder.encode(key.getEncoded());
	}
	
	
}
