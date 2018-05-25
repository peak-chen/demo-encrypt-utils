
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;



public class AESUtil {

	private static final String ivStr = "ABCDEFabcdef!@#$";

	/**
	 * @description: 加密
	 * @param src
	 * @param key
	 * @return
	 */
	public static String Encrypt(String src, String key)  {
		// 判断Key是否正确
		if (key == null) {
			throw new BusinessException(
					BusinessError.RESULTCODE_SYSTEM_ERROR.getStatus(),
					"Token加密异常,加密Key为空null");
		}
		// 判断Key是否为16位
		if (key.length() != 16) {
			throw new BusinessException(
					BusinessError.RESULTCODE_SYSTEM_ERROR.getStatus(),
					"Token加密异常,加密Key长度不是16位");
		}
		byte[] raw = key.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		byte[] encrypted =null;
		try{
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"0102030405060708
			IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			encrypted= cipher.doFinal(src.getBytes());
		}catch(Exception e){
			throw new BusinessException(
					BusinessError.RESULTCODE_SYSTEM_ERROR.getStatus(),
					"Token加密异常");
		}
		return Base64.encodeBase64String(encrypted);
		// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
	}

	/**
	 * @description:  解密
	 * @param src
	 * @param key
	 * @return
	 */
	public static String Decrypt(String src, String key) {
		try {
			// 判断Key是否正确
			if (key == null) {
				throw new BusinessException(
						BusinessError.RESULTCODE_SYSTEM_ERROR.getStatus(),
						"Token解密异常,解密Key为空null");
			}
			// 判断Key是否为16位
			if (key.length() != 16) {
				throw new BusinessException(
						BusinessError.RESULTCODE_SYSTEM_ERROR.getStatus(),
						"Token解密异常,解密Key长度不是16位");
			}
			byte[] raw = key.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(ivStr.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = Base64.decodeBase64(src);// 先用base64解密
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "UTF-8");
			return originalString;
		} catch (Exception ex) {
			throw new BusinessException(
					BusinessError.RESULTCODE_SYSTEM_ERROR.getStatus(),
					"Token解密异常");
		}
	}

	public static void main(String[] args) throws Exception {
		/*System.out.println(Encrypt("{" + "\"parkId\":\"11111\","
				+ "\"inTime\":\"2015-09-02 12:23:14\","
				+ "\"outTime\":\"2015-09-02 12:33:14\"," + "\"parkTime\":10,"
				+ "\"carPlate\":\"浙A12345\"," + "\"plateType\":1,"
				+ "\"plateColor\":1," + "\"carType\":1,"
				+ "\"cardNo\":\"1343445\"," + "\"inUnid\":\"22334455def223\","
				+ "\"outUnid\":\"22334455def465\"," + "\"payMoney\": 2000,"
				+ "\"payType\":0," + "\"bookUnid\":\"6dafa98897\","
				+ "\"needPay\":2000," + "\"parkType\":1,"
				+ "\"gateId\":\"12343335\"," + "\"collectorCode\":\"001\","
				+ "\"collectorName\":\"小张\"," + "\"remark\":\"rtur\","
				+ "\"couponCode\":\"321wdfs23\"," + "\"deductMoney\":20,"
				+ "\"payTime\":\"2015-09-02 12:33:14\"" + "}",
				"0102030405060708"));
		System.out
				.println(Decrypt(
						"mA/0FH6Ollvq8QX8b10T/cH+wgQ8GJjFykIyMuKX3zVURuWOGdprrP89Lvqza1KqHy+T2oQKwbJjGNM37w1S1o0NSnts9htYQ/s7ikQb6aqtqpOZ5D8bFlB2NHJbVHPQh7O/udIAKjYhtGLRGS+0QwX6WqYyFRyrSkR6bt5Hw76BullA06KcOxzzcm6HKzoWEQeeVZ6kAWHRo2ryrRLkVAWvLaOVYMKHEFloR3TPuEzfvfJr+w8IdbZzqU+82iH/GaDNDpQ9TG6W0caDF9fNTGZZY5PIwlLH2N9N9aLRTIbRtRe7LPELn6VhVLbcu3rYdAKrpZNyUPRuln/8Q8TZ6CLcpZn/gnOvV3jMaQa7VxRUyaxWs0H4qABZaP1GABUf3JnSMKBVWQX9+xdKeqWQopTLcZCZIcGFa1tMjnIvBNz0cfMfNAD0VgZd8AIV+clIXDIAChSazjjb7gkJk8SPihzw4c6sllnFTCKfzI6ZwjYlQ9K8JA6YwvGskWqAxX1Cfqo6SgfshVrc3ohNfApgrg1ZfcV2cAf2P/vddqIY0iCiOOXmsvYyH+nM+12/ZADnzDNKa/KKcYXr59MIWJ2enF0wx42V5DfQSE8UMHXP17+5k7vHK5S3WmtxWsdVTOfL",
						"0102030405060708"));
		System.out
				.println(JSON
						.parseObject(Decrypt(
								"mA/0FH6Ollvq8QX8b10T/cH+wgQ8GJjFykIyMuKX3zVURuWOGdprrP89Lvqza1KqHy+T2oQKwbJjGNM37w1S1o0NSnts9htYQ/s7ikQb6aqtqpOZ5D8bFlB2NHJbVHPQh7O/udIAKjYhtGLRGS+0QwX6WqYyFRyrSkR6bt5Hw76BullA06KcOxzzcm6HKzoWEQeeVZ6kAWHRo2ryrRLkVAWvLaOVYMKHEFloR3TPuEzfvfJr+w8IdbZzqU+82iH/GaDNDpQ9TG6W0caDF9fNTGZZY5PIwlLH2N9N9aLRTIbRtRe7LPELn6VhVLbcu3rYdAKrpZNyUPRuln/8Q8TZ6CLcpZn/gnOvV3jMaQa7VxRUyaxWs0H4qABZaP1GABUf3JnSMKBVWQX9+xdKeqWQopTLcZCZIcGFa1tMjnIvBNz0cfMfNAD0VgZd8AIV+clIXDIAChSazjjb7gkJk8SPihzw4c6sllnFTCKfzI6ZwjYlQ9K8JA6YwvGskWqAxX1Cfqo6SgfshVrc3ohNfApgrg1ZfcV2cAf2P/vddqIY0iCiOOXmsvYyH+nM+12/ZADnzDNKa/KKcYXr59MIWJ2enF0wx42V5DfQSE8UMHXP17+5k7vHK5S3WmtxWsdVTOfL",
								"0102030405060708")));*/
		System.out.println(Encrypt("12345678900142658968,10.21.38.111","0102030405060708"));
		System.out.println(Decrypt("8pTj10nR3XdOorf6IcmbDhI3USt/CavRzSYTHvc28fk86JitBp+X8puuh0a0BhPCK6eQ7c9cpXkjzYh4CrXhsOAooDJ+R6v5t6jpk3Bz90oNXwMmUTbDtlTXEQAjl+XtNi15qSTKMUrQkzwDH2w3AB72xjSYtv1DHfW/xfzdiK/nTFnP4opopwX5tqFAU/evIYbxb5uoMz3KA76vdWZyTcu+/kZAzyzm/SyO0v7ST/xXtwa5u7U5qH+NyHDRRrf7u1XrI62Z3+lfVNcrweHCjB+DVvEwGajMUdFd3nS8cDte98zc9+V76Y+Yvma3kZqDaqNLw9TMQ6bU5nb+FbzrTn8rPM4rX0KLeWYZHSyHBXmznt4FvI/EJ3e20Ett7dA58oE3SWLQAURs0wvvpAsc5wyFbdjucxMaI8109sDQtTUshFeuh/C4ZYR+or3ecJyXgOwVhIkKoWfmqBcpcLwcO1bwA27gbxr5vtHiJ9M9vHeNJ3T13El077uymT86jcdYRIpmCMhzx5Q3gvf7jM0P+GlSiPj7sxaxsj5MJ8XOrEw=", "0102030405060708"));
		
		
	}
}
