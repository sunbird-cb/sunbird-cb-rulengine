package org.sunbird.ruleengine.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

public class PasswordUtil {

	public static Map<String, String> enctyptPassword(String password,
			String salt) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {

		// Salt generation 64 bits long
		byte[] bSalt = new byte[8];

		String sSalt = null;
		if (salt == null) {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.nextBytes(bSalt);
			sSalt = DatatypeConverter.printBase64Binary(bSalt);
		} else {

			sSalt = salt;

		}
		String sDigest = getHash(GenericConstant.HASH_ITERATION_NO, password,
				sSalt);

		Map<String, String> map = new HashMap<String, String>();
		map.put(GenericConstant.PASSWORD, sDigest);
		map.put(GenericConstant.SALT, sSalt);
		return map;
	}

	public static String getHash(int iterationNb, String password, String salt)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-384");/*/ SHA-2 /*/
		digest.reset();
		digest.update(salt.getBytes("UTF-8"));
		byte[] input = digest.digest(password.getBytes("UTF-8"));
		for (int i = 0; i < iterationNb; i++) {
			digest.reset();
			input = digest.digest(input);
		}

		String sDigest = DatatypeConverter.printBase64Binary(input);

		return sDigest;

	}

}
