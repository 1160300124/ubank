package com.ulaiber.web.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

public class SignatureUtil {
	private final static String SIGN_TYPE_RSA = "RSA";
	private final static String SIGN_ALGORITHMS  = "SHA1WithRSA";
	private final static String CHARSETTING = "UTF-8";
	
	public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, String priKey) throws Exception {
		if (isNullOrEmpty(algorithm) || isNullOrEmpty(priKey))
			return null;
		
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        byte[] encodedKey = StreamUtil.readText(new ByteArrayInputStream(priKey.getBytes())).getBytes();
		encodedKey = Base64.decodeBase64(priKey.getBytes());

        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
	}
	
	public static PublicKey getPublicKeyFromX509(String algorithm, String pubKey) throws Exception {
		
		if (isNullOrEmpty(algorithm) || isNullOrEmpty(pubKey))
			return null;
		
		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		StringWriter writer = new StringWriter();
		StreamUtil.io(new InputStreamReader(new ByteArrayInputStream(pubKey.getBytes())), writer);
		
		byte[] encodeByte = writer.toString().getBytes();
		encodeByte = Base64.decodeBase64(pubKey.getBytes());
		
		return keyFactory.generatePublic(new X509EncodedKeySpec(encodeByte));
	}
	
	public static String sign(String plain, String prikey) throws Exception {
		if (isNullOrEmpty(plain) || isNullOrEmpty(prikey))
			return null;
		
		PrivateKey privatekey = getPrivateKeyFromPKCS8(SIGN_TYPE_RSA, prikey);
		Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
		signature.initSign(privatekey);
		signature.update(plain.getBytes(CHARSETTING));
		byte[] signed = signature.sign();
		
		return new String(Base64.encodeBase64(signed));
	}
	
	public static boolean virefy(String plain, String sign, String pubkey) throws Exception {
		if (isNullOrEmpty(plain) || isNullOrEmpty(sign) || isNullOrEmpty(pubkey))
			return false;
		
		PublicKey publicKey = getPublicKeyFromX509(SIGN_TYPE_RSA, pubkey);
		Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
		signature.initVerify(publicKey);
		signature.update(plain.getBytes(CHARSETTING));
		
		return signature.verify(Base64.decodeBase64(sign.getBytes()));
	}

	private static boolean isNullOrEmpty(String orgStr) {
		return (orgStr == null || orgStr.trim().length() == 0);
	}
	
	public static void main(String[] args) throws Exception {
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKh7bxbe5dhwuZzNkeDXrpHx7o5k+uIWrbY1+8c6Oqgq2AfajnK5v10OfQW85xNUn/4TzoRcCOCaK2LZO4QJoQmgs41x45jZNvI/f8EGcJvt2oCs3S2Da98+v6VVfDXoSfgeHlNRcDSYlZF2E31KQLtdTGva9IeECx2CpPIkbVeXAgMBAAECgYA0QlUq2uigQhbQtFLTUxMq4cgFEv1es3oeUpBOM5mOH/vyM7CLlWHuE1hkNzvVmyIlRS+BjqqSQD/E4Wy8f+AbAznky5F8q5Afe5ZKxi+n2M4ZMgh9uryVMcAHCXu1RnOrtsnGjJvp23ku4wZtWCHLNAuQfI9zj6ncq4v50RKKQQJBAN8tSbcT37Mq3Y50zVnnmGxNEgUZKJXwPw/KnO6EeR/Nfpmzy40GQU8y+GGoq5cVY5NDOqYVi6nh21mLXQij9AsCQQDBQt0zU8zurSNaoRYsjhNrHJHQjBt0WuIxtluZz44CTbNQw5/3kA1jVvt1EXOE1hF+l2QVIuLgvgIeHDwvenYlAkAeiusgtAaUVZR2r4N+/1P71lxV+Eh2pKdsuNTbS6Pr90qRLGr6BNYhSZ92dgftqE61U6kOG7q+aBuF2K3FxfJbAkA1oCES4fjmbYJ23mXxvQakXQwU6xufIKzNEIXAWzhTaU4NZgrYPc+JNhSWOl5siJ3YG5f4yXJc3DxoMHt+zSNFAkEA25eGORpWtXERhGXHZR8f3nWIHTAF+EM1O8T7YXp+y7sWs8YvtNI3ZtzjzncHWk4YdScNqkXC1UJz2hokLUR7Aw==";
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoe28W3uXYcLmczZHg166R8e6OZPriFq22NfvHOjqoKtgH2o5yub9dDn0FvOcTVJ/+E86EXAjgmiti2TuECaEJoLONceOY2TbyP3/BBnCb7dqArN0tg2vfPr+lVXw16En4Hh5TUXA0mJWRdhN9SkC7XUxr2vSHhAsdgqTyJG1XlwIDAQAB";
		String plain = "plain：\"PLAIN\":{\"BODY\":{\"returnCode\":\"AAAAAAA\",\"status\":\"00\",\"returnMsg\":\"交易成功\",\"mobileNo\":\"15555524587\"},\"HEAD\":{\"spdbJnlNo\":\"997907074816\",\"timeStamp\":\"1435152316796\",\"transId\":\"MiguRepay\",\"jnlNo\":\"Y000Y021120140605\",\"version\":\"1.0\"}}";
		String sign = sign(plain,privateKey);
//		String sign = "BYyaHBgXhAZcjW0VUW1Cx7IpACMCkdmLkF5WkkgVEJboNtDzbQ0hRJ6v6xYDCrHKwTTigq9VpVKnyWAdvYkXlQyTs5vK0wx9aPlLaPFj6e8PZfd3+GM+Azwt15vgoaLs6GxcAZJ7FQMVkRqZWRv1MNorMh0rPLNwbdZgVF3m1+g=";
		System.out.println(sign);
		System.out.println(virefy(plain, sign, publicKey));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
