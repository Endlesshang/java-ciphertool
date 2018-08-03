package Mac;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class test {
	public static void main(String[] args) {
		SecretKey secretKey = new SecretKeySpec("123456".getBytes(), "HmacMD5");
		System.out.println(new HexBinaryAdapter().marshal(secretKey.getEncoded()));
	}
}
