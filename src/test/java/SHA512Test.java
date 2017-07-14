import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;


public class SHA512Test {
	@Test
	public void testGenerate() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update("test".getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        sb.append(Hex.encodeHex(md.digest()));
        System.out.println(sb);
	}

}
