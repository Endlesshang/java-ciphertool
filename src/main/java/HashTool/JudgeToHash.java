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
		
		//���bc��
		Security.addProvider(new BouncyCastleProvider());
		
		//�����ļ�������
		FileInputStream fileInputStream = new FileInputStream(file);
		
		//������ϢժҪ����ָ��Ϊ�βζ�Ӧ���㷨
		MessageDigest mDigest = MessageDigest.getInstance(cryptoAlgorithm);
		
		//��װΪ�ļ�ժҪ������
		DigestInputStream dInputStream = new DigestInputStream(fileInputStream, mDigest);
		
		//��4MΪ��λ���ļ���һ�飬Ȼ������ϢժҪ�������hashֵ
		byte[] bs = new byte[4096];
		while((dInputStream.read(bs)) != -1);
		byte[] digest = mDigest.digest();
		
		//�ر�������
		dInputStream.close();
		return new HexBinaryAdapter().marshal(digest);
	}
}
