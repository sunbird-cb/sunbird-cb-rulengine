package org.sunbird.ruleengine.common;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * This class is a utility class for Encryption and Decryption of String
 * parameters using RuleEngine Cipher Key.
 *  
 * @author chirodip.p
 * @version 1.0
 * @since 1.0
 * @see javax.crypto.Cipher 
 */


public class RuleEngineAESCipher 
{
	private String encryptionKey;
	private RuleEngineAESCipher cipher=null;
	
	public RuleEngineAESCipher()
	{
		if(cipher == null)
			cipher = new RuleEngineAESCipher("V@lf0rm@");
	}
	
	private RuleEngineAESCipher(String inputEncryptionKey)
	{
		try
		{
			encryptionKey = inputEncryptionKey;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Returns an Encrypted version of the argument string using RuleEngine Cipher Key
	 * @param msg The Input Message
	 * @return The Encrypted Message
	 * @since 1.0
	 */
	public String getEncrypted(String msg)
	{
		byte[] encryptedBytes = null;
		try
		{
			 Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
		     encryptedBytes = cipher.doFinal(msg.getBytes()); 
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return Base64.encodeBase64String(encryptedBytes);
	}

	/**
	 * Returns an Decrypted version of the argument string using RuleEngine Cipher Key
	 * @param msg The Encrypted Message
	 * @return The Decrypted Message
	 * @since 1.0
	 */
	public String getDecrypted(String encryptedMsg)
	{
		byte[] plainBytes = null;
		try
		{
			Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
			plainBytes = cipher.doFinal(Base64.decodeBase64(encryptedMsg));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
        return new String(plainBytes);
	}
	
	 private Cipher getCipher(int cipherMode)
	            throws Exception
	    {
	        String encryptionAlgorithm = "AES";
	        SecretKeySpec keySpecification = new SecretKeySpec(
	                encryptionKey.getBytes("UTF-8"), encryptionAlgorithm);
	        Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
	        cipher.init(cipherMode, keySpecification);

	        return cipher;
	    }
}
