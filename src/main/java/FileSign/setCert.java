package FileSign;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.Calendar;
import java.util.Date;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;


public class setCert {
	public static Certificate selfSign(KeyPair keyPair, String subjectDN, String signatureAlgorithm) throws Exception {
		
		//创建BouncyCastleProvider对象，并调用addProvider方法以激活bc包
		BouncyCastleProvider bcProvider = new BouncyCastleProvider();
		Security.addProvider(bcProvider);

		//以长整形数字类型先保存当前的系统时间，然后保存到Date对象中作为证书有效期的起始日期
		long now = System.currentTimeMillis();
		Date startDate = new Date(now);
		
		//以形参中的subjectDN字符串为参数创建证书的发布者的信息
		X500Name dnName = new X500Name(subjectDN);

		// Using the current time stamp as the certificate serial number
		BigInteger certSerialNumber = new BigInteger(Long.toString(now));

		//用Calendar对象来计算证书有效期的终止日期，先创建默认初始化的calendar对象，里面是空的
		Calendar calendar = Calendar.getInstance();
		
		//把上面定义好的初始日期传入calendar对象
		calendar.setTime(startDate);
		
		//调用add方法使calendar中存储的日期加一年，再创建一个新的日期对象来存储加一年之后的日期
		calendar.add(Calendar.YEAR, 1);
		Date endDate = calendar.getTime();

		//创建contentSigher对象，并初始化为指定签名算法和签名私钥的“证书内容生成对象”
		ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());

		//创建X509证书生成对象并使用上述的变量来初始化
		JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, certSerialNumber, startDate,
				endDate, dnName, keyPair.getPublic());

		//创建“基本限制”对象，当这个对象的构造器为真时，这个对象所参与构建的证书是【签发者和使用者为同一人】的自签名证书
		BasicConstraints basicConstraints = new BasicConstraints(true);
		
		//把基本限制对象加入到证书生成器中
		certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), true, basicConstraints);

		//返回普通的证书，使用Convert类来转换
		return new JcaX509CertificateConverter().setProvider(bcProvider)
				.getCertificate(certBuilder.build(contentSigner));
	}

	public static void main(String[] args) throws Exception {
		// 生成DSA密钥对
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		// 生成自签名数字证书
		String subjectDN = "CN = xie hang OU = cauc O = cauc L = tj S = tj C = cn";
		String signatureAlgorithm = "SHA256WithDSA";
		Certificate certificate = selfSign(keyPair, subjectDN, signatureAlgorithm);
		System.out.println(certificate);

		// 将密钥对（私钥和自签名数字证书）存入密钥库文件
		KeyStore keyStore = KeyStore.getInstance("JCEKS");
		char[] passWord = "123456".toCharArray();
		keyStore.load(null, passWord);
		keyStore.setKeyEntry("mydsakey", keyPair.getPrivate(), passWord, new Certificate[] { certificate });
		FileOutputStream fos = new FileOutputStream("xhnewkeys.keystore");
		keyStore.store(fos, passWord);
	}
}
