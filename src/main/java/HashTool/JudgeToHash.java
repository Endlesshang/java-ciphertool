package HashTool;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.Security;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class JudgeToHash {
	public static String FileEncrypt(File file, String cryptoAlgorithm) throws Exception {
		
		//添加bc包
		Security.addProvider(new BouncyCastleProvider());
		
		//声明文件输入流
		FileInputStream fileInputStream = new FileInputStream(file);
		
		//声明消息摘要对象，指定为形参对应的算法
		MessageDigest mDigest = MessageDigest.getInstance(cryptoAlgorithm);
		
		//封装为文件摘要输入流
		DigestInputStream dInputStream = new DigestInputStream(fileInputStream, mDigest);
		
		//以4M为单位把文件读一遍，然后用消息摘要对象计算hash值
		byte[] bs = new byte[4096];
		while((dInputStream.read(bs)) != -1);
		byte[] digest = mDigest.digest();
		
		//关闭输入流
		dInputStream.close();
		return new HexBinaryAdapter().marshal(digest);
	}
}
