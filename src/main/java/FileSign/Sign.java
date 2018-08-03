package FileSign;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument.Content;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.io.SignerInputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.crypto.SecretKey;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.Certificate;
import java.security.interfaces.DSAPrivateKey;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.SecretKeyEntry;
import java.security.Security;
import java.security.Signature;
import java.security.Signer;
import java.util.Calendar;
import java.util.Date;

import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

public class Sign extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldFilePath;
	private File file;
	private JButton btnNewButton_1;
	private JButton btnNewButton_2;
	private File outFile;
	private byte[] sign = new byte[0];
	private JTextArea textAreaSign;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sign frame = new Sign();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Sign() {
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("\u9009\u62E9\u6587\u4EF6\uFF1A");
		lblNewLabel.setBounds(10, 10, 119, 15);
		contentPane.add(lblNewLabel);

		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		textFieldFilePath.setBounds(10, 36, 313, 21);
		contentPane.add(textFieldFilePath);
		textFieldFilePath.setColumns(10);

		JButton btnNewButton = new JButton("...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser("D:/");
				int value = chooser.showOpenDialog(Sign.this);
				if (value == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					textFieldFilePath.setText(file.getAbsolutePath());
				}
			}
		});
		btnNewButton.setBounds(332, 35, 93, 23);
		contentPane.add(btnNewButton);

		btnNewButton_1 = new JButton("\u751F\u6210\u7B7E\u540D");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (file != null) {
					try {
						
						//����Կ��
						KeyStore keyStore = KeyStore.getInstance("JCEKS");
						char[] password = "123456".toCharArray();
						FileInputStream fis = new FileInputStream("xhnewkeys.keystore");
						keyStore.load(fis, password);
						
						//ʵ����ProtectionParameter�ӿ��Դ򿪡���ȡ��Կ����˽Կ����ͨ��
						KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password);
						
						//ʵ����PrivateKeyEntry�ӿ��Դ���Կ���л�ȡָ����Կ
						KeyStore.PrivateKeyEntry privateKeyEntry = (PrivateKeyEntry) keyStore.getEntry("mydsakey", protParam);
						
						//��PrivateKeyEntry�ӿڵ�ʵ���л�ȡDSA˽Կ���������ļ�������
						DSAPrivateKey dsaPrivateKey = (DSAPrivateKey) privateKeyEntry.getPrivateKey();
						FileInputStream signFileIS = new FileInputStream(file);
						
						//��������֤����󲢳�ʼ��ΪSHA256withDSA��ǩ������
						Signature signature = Signature.getInstance("SHA256withDSA");
						
						//��ʼ������֤�����Ϊ����DSA��Կ�ġ�ǩ����ģʽ
						signature.initSign(dsaPrivateKey);
						if (file.exists()) {
							byte[] bs = new byte[1024];
							int fRead = 0;
							
							//��1024�ֽ�Ϊ��λ��ÿ���ԡ�һ�ھ͵�������֤��Ķ������ǩ����ֱ���������ļ���ǩ��
							while ((fRead = signFileIS.read(bs)) != -1) {
								signature.update(bs, 0, fRead);
							}
							JOptionPane.showMessageDialog(null, "ǩ���ɹ���");
							
							//һ��Ҫ�ǵùر�������Ҫ����֤�����
							fis.close();
							sign = signature.sign();
							
							//��ǩ���õ�֤����16���������������
							textAreaSign.setText(new HexBinaryAdapter().marshal(sign));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "����ѡ���ļ��ٽ���ǩ����");
				}
			}
		});
		btnNewButton_1.setBounds(84, 216, 93, 23);
		contentPane.add(btnNewButton_1);

		btnNewButton_2 = new JButton("\u8FD4\u56DE");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeWindows();
			}
		});
		btnNewButton_2.setBounds(252, 216, 93, 23);
		contentPane.add(btnNewButton_2);

		JLabel lblNewLabel_1 = new JLabel("\u7B7E\u540D\u751F\u6210\u4E3A\uFF1A");
		lblNewLabel_1.setBounds(10, 94, 107, 15);
		contentPane.add(lblNewLabel_1);

		textAreaSign = new JTextArea();
		textAreaSign.setEditable(false);
		textAreaSign.setLineWrap(true);
		textAreaSign.setWrapStyleWord(true);
		textAreaSign.setBounds(10, 119, 313, 87);
		contentPane.add(textAreaSign);
	}

	public byte[] getSign() {
		return sign;
	}

	public void closeWindows() {
		this.dispose();
	}
}
