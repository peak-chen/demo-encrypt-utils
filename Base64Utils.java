
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * @Description: Base64Utils
 * @Date 2017年02月13日
 * @tag
 */
public class Base64Utils {

	private static final int CACHE_SIZE = 1024;

	public static byte[] decode(String base64) throws Exception {
		return Base64.decodeBase64(base64);

	}

	public static String encode(byte[] bytes) throws Exception {
		return Base64.encodeBase64String(bytes);
	}

	public static String encodeFile(String filePath) throws Exception {
		byte[] bytes = fileToByte(filePath);
		return encode(bytes);
	}

	public static void decodeToFile(String filePath, String base64)
			throws Exception {
		byte[] bytes = decode(base64);
		byteArrayToFile(bytes, filePath);
	}

	public static byte[] fileToByte(String filePath) {
		byte[] data = new byte[0];
		File file = new File(filePath);
		if (file.exists()) {
			FileInputStream in = null;
			ByteArrayOutputStream out = null;
			try {
				in = new FileInputStream(file);
				out = new ByteArrayOutputStream(2048);
				byte[] cache = new byte[CACHE_SIZE];
				int nRead = 0;
				while ((nRead = in.read(cache)) != -1) {
					out.write(cache, 0, nRead);
				}
				data = out.toByteArray();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (out != null) {
					try {
						out.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return data;
	}

	public static void byteArrayToFile(byte[] bytes, String filePath) {
		InputStream in = new ByteArrayInputStream(bytes);
		File destFile = new File(filePath);
		if (destFile.getParentFile() != null
				&& !destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		try {
			destFile.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(destFile);
			byte[] cache = new byte[CACHE_SIZE];
			int nRead = 0;
			while ((nRead = in.read(cache)) != -1) {
				out.write(cache, 0, nRead);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}