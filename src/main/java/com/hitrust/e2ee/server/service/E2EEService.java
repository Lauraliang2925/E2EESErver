package com.hitrust.e2ee.server.service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.hitrust.e2ee.util.Utility;
import com.hitrust.security.e2ee.E2EEJNI;

//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;

//@Component
//@Scope("singleton")
public class E2EEService {
	private static Logger LOG = LogManager.getLogger(E2EEService.class);

	@Autowired
	private E2EEJNI e2ee;
	String Modulus = "D68390E251318B1772EECEFD6963B1E66AD40B698EF5B6C6EA96BA6700C8C8B942180332DDDA8B40E2DF7D9C72B6054FF95D1B51D0F87CFA4A9E100AEB55123FA34A63895F22EBA8D66B0B4F11621E4BCEC08537200A0295865A981EF374418C920ED023A49DD08B2738E7877C136C5937D6AD4303DD2FF9ADCC3A105C8348CCAA47E1D095353A3E0CD639F5F265EB8DB98E52C2CF04E4EA9E7D7ECC5DD888A468A28D229FCF41A6D5AA4E4EAB0CB8AB286169F1441727A7AFCADA390FA9EF943AED8E62DD673DBD7D9620D19EDF3F24D99D3477734358B711095F3B239A4BEE3ACEE1E5B2F65EE044CDCF90726CA0B9E0D5C74A6D42509D6F9DCF3B8C1532B3";
	String Exponent = "010001";

	private String rsaData = "FxrXnr65jykChXFlgBQBaN/wkPrN37xZXyy2pr9haNzqCD5imsy2F6h9S2w9Ek4fx0nxGhNGyYu1qaO8Ow0sy8/JnhC6jsCreoEu8oGtJ8MdP1PfH6RwGJsOerm2CvzdoVRbYrDc7UubDXFH2o7Zb+4F7FQkMk+TimACHnOXVFshPaGyIK+aCT6JHUqph3+biZfhSs4pgF7p/0ELWxBeaG7TlLQo0yL1mBaWXoe4T/8vwky2sI9Pmy3I7R4x0f8VDaBqQZ3R2PAo85o9AKHe54QoZMokiPEh/u43EOB+2o0eLxAEfqu266Gt4qob2jXh5fuUgnjBhP2/cPRtdj2eCg==";

//	public native int encrypt(byte[] data, byte[] rtn);
//	public native Boolean verify(byte[] encrypted, byte[] source);

	public String encrypt(String encData, String encKeyWithRSA) {
		// rtn = e2ee.setPassWord(encData, encKeyWithRSA);
		return e2ee.setPassWord(encData, encKeyWithRSA);

	}

	public String encryptRawData(String data) {

		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(256, new SecureRandom());
			SecretKey secretKey = keyGen.generateKey();
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			System.out.println("AES_CBC_PKCS5PADDING IV:" + cipher.getIV());
			System.out.println("AES_CBC_PKCS5PADDING Algoritm:" + cipher.getAlgorithm());
			byte[] byteCipherText = cipher.doFinal(data.getBytes("UTF-8"));
//			SecretKeySpec AES = new SecretKeySpec(keyBytes, "AES");
//			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//			cipher.init(Cipher.ENCRYPT_MODE, AES);
//			byte[] result = cipher.doFinal( hexStr2Bin(plaintext));

			BigInteger modulus = new BigInteger(Modulus, 16);
			BigInteger exponent = new BigInteger(Exponent, 16);
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			RSAPublicKey key = (RSAPublicKey) factory.generatePublic(publicKeySpec);
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(secretKey.getEncoded());

			return e2ee.setPassWord(Utility.bin2HexStr(byteCipherText), Utility.bin2HexStr(result));

		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("encryptRawData error:", e);

		}

		return "";

	}

	public Boolean verifyRawData(String data, String encDBData) {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(256, new SecureRandom());
			SecretKey secretKey = keyGen.generateKey();
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			System.out.println("AES_CBC_PKCS5PADDING IV:" + cipher.getIV());
			System.out.println("AES_CBC_PKCS5PADDING Algoritm:" + cipher.getAlgorithm());
			byte[] byteCipherText = cipher.doFinal(data.getBytes("UTF-8"));

			BigInteger modulus = new BigInteger(Modulus, 16);
			BigInteger exponent = new BigInteger(Exponent, 16);
			RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			RSAPublicKey key = (RSAPublicKey) factory.generatePublic(publicKeySpec);
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] result = cipher.doFinal(secretKey.getEncoded());
			if (e2ee.verifyPassWord(Utility.bin2HexStr(byteCipherText), Utility.bin2HexStr(result), encDBData)
					.intValue() == 0) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error("verifyRawData error:", e);
		}

		return Boolean.FALSE;

	}

	public Boolean verify(String encData, String encKeyWithRSA, String encDBData) {
		if (e2ee.verifyPassWord(encData, encKeyWithRSA, encDBData).intValue() == 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	public E2EEService(String filepath) {
		// System.loadLibrary(filepath);
//		System.loadLibrary("Z:/work/TSBANK/050613KI/05-IM/e2ee/Debug/csxapi");
//		System.loadLibrary("Z:/work/TSBANK/050613KI/05-IM/e2ee/Debug/e2ee");
//		System.loadLibrary("Z:/work/TSBANK/050613KI/05-IM/e2ee/Debug/JniE2ee");

	}

}
