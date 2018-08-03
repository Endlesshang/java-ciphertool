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
		
		//����BouncyCastleProvider���󣬲�����addProvider�����Լ���bc��
		BouncyCastleProvider bcProvider = new BouncyCastleProvider();
		Security.addProvider(bcProvider);

		//�Գ��������������ȱ��浱ǰ��ϵͳʱ�䣬Ȼ�󱣴浽Date��������Ϊ֤����Ч�ڵ���ʼ����
		long now = System.currentTimeMillis();
		Date startDate = new Date(now);
		
		//���β��е�subjectDN�ַ���Ϊ��������֤��ķ����ߵ���Ϣ
		X500Name dnName = new X500Name(subjectDN);

		// Using the current time stamp as the certificate serial number
		BigInteger certSerialNumber = new BigInteger(Long.toString(now));

		//��Calendar����������֤����Ч�ڵ���ֹ���ڣ��ȴ���Ĭ�ϳ�ʼ����calendar���������ǿյ�
		Calendar calendar = Calendar.getInstance();
		
		//�����涨��õĳ�ʼ���ڴ���calendar����
		calendar.setTime(startDate);
		
		//����add����ʹcalendar�д洢�����ڼ�һ�꣬�ٴ���һ���µ����ڶ������洢��һ��֮�������
		calendar.add(Calendar.YEAR, 1);
		Date endDate = calendar.getTime();

		//����contentSigher���󣬲���ʼ��Ϊָ��ǩ���㷨��ǩ��˽Կ�ġ�֤���������ɶ���
		ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate());

		//����X509֤�����ɶ���ʹ�������ı�������ʼ��
		JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, certSerialNumber, startDate,
				endDate, dnName, keyPair.getPublic());

		//�������������ơ����󣬵��������Ĺ�����Ϊ��ʱ��������������빹����֤���ǡ�ǩ���ߺ�ʹ����Ϊͬһ�ˡ�����ǩ��֤��
		BasicConstraints basicConstraints = new BasicConstraints(true);
		
		//�ѻ������ƶ�����뵽֤����������
		certBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), true, basicConstraints);

		//������ͨ��֤�飬ʹ��Convert����ת��
		return new JcaX509CertificateConverter().setProvider(bcProvider)
				.getCertificate(certBuilder.build(contentSigner));
	}

	public static void main(String[] args) throws Exception {
		// ����DSA��Կ��
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		// ������ǩ������֤��
		String subjectDN = "CN = xie hang OU = cauc O = cauc L = tj S = tj C = cn";
		String signatureAlgorithm = "SHA256WithDSA";
		Certificate certificate = selfSign(keyPair, subjectDN, signatureAlgorithm);
		System.out.println(certificate);

		// ����Կ�ԣ�˽Կ����ǩ������֤�飩������Կ���ļ�
		KeyStore keyStore = KeyStore.getInstance("JCEKS");
		char[] passWord = "123456".toCharArray();
		keyStore.load(null, passWord);
		keyStore.setKeyEntry("mydsakey", keyPair.getPrivate(), passWord, new Certificate[] { certificate });
		FileOutputStream fos = new FileOutputStream("xhnewkeys.keystore");
		keyStore.store(fos, passWord);
	}
}
