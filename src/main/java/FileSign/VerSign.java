package FileSign;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import FileEnOrDe.FileDecrypt;

import javax.swing.JTextField;
import javax.crypto.SecretKey;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStore.SecretKeyEntry;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.awt.event.ActionEvent;

public class VerSign extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldFilePath;
	private File file;
	private JButton btnNewButton_1;
	private byte[] sign;
	private JTextField textFieldSignature;
	private File signFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VerSign frame = new VerSign();
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
	public VerSign() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		textFieldFilePath.setBounds(10, 50, 303, 21);
		contentPane.add(textFieldFilePath);
		textFieldFilePath.setColumns(10);

		JButton btnNewButton = new JButton("...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//�½��ļ�ѡ������ѡ��Ŀ¼ΪD�̸�Ŀ¼
				JFileChooser chooser = new JFileChooser("D:/");
				
				//���ļ�ѡ���
				int value = chooser.showOpenDialog(VerSign.this);
				
				//��ѡ��õ��ļ����浽˽�б����У���·�������������
				if (value == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					textFieldFilePath.setText(file.getAbsolutePath());
				}
			}
		});
		btnNewButton.setBounds(331, 49, 93, 23);
		contentPane.add(btnNewButton);

		JLabel lblNewLabel = new JLabel("\u8BF7\u9009\u62E9\u9700\u9A8C\u8BC1\u7B7E\u540D\u7684\u6587\u4EF6\uFF1A");
		lblNewLabel.setBounds(10, 25, 184, 15);
		contentPane.add(lblNewLabel);

		btnNewButton_1 = new JButton("\u9A8C\u8BC1\u7B7E\u540D");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((file != null) && (textFieldSignature.getText() != null)) {
					try {
						
						//���������Կ��
						KeyStore keyStore = KeyStore.getInstance("JCEKS");
						char[] password = "123456".toCharArray();
						FileInputStream fis = new FileInputStream("xhnewkeys.keystore");
						keyStore.load(fis, password);
						
						//����X509֤����󲢴���Կ������ָ�����ֻ�ȡָ����֤��
						X509Certificate certificate = (X509Certificate) keyStore.getCertificate("mydsakey");
						
						//����Կ���л�ȡDSA��Կ
						DSAPublicKey dsaPublicKey = (DSAPublicKey) certificate.getPublicKey();
						
						//��������֤�����ָ��ǩ���㷨ΪSHA256withDSA������ʼ��ģʽΪ��֤ǩ��ģʽ
						Signature signature = Signature.getInstance("SHA256withDSA");
						signature.initVerify(dsaPublicKey);
						
						//���ļ�ѡ�����ж������ļ��������֤�����򵯳���Ϣ��ʾ�û�δѡ���ļ�
						if (file.exists()) {
							FileInputStream signFileIS = new FileInputStream(file);
							byte[] bs = new byte[1024];
							int fRead = 0;
							
							//��1024�ֽ�Ϊ��λ��ÿ���ԡ�һ������ļ��е������е�ǩ�����µ�signature������
							while ((fRead = signFileIS.read(bs)) != -1) {
								signature.update(bs, 0, fRead);
							}
							
							//�ر���
							signFileIS.close();
							
							//У���û�������ļ�ǩ���Ƿ����У�鳤��
							try {
								// sign = certificate.getSignature();
								sign = new HexBinaryAdapter().unmarshal(textFieldSignature.getText());
							} catch (Exception e2) {
								JOptionPane.showMessageDialog(null, "�������ǩ������Ϊ92����ĸ����");

							}
							if (signature.verify(sign)) {
								JOptionPane.showMessageDialog(null, "���ļ��е�ǩ�������������ǩ����");
							} else {
								JOptionPane.showMessageDialog(null, "���ļ��е�ǩ���������������ǩ����");
							}
						}
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "�����Ҫ��֤���ļ�ѡ����Լ��Ϸ�ǩ������ã�");
				}
			}
		});
		btnNewButton_1.setBounds(92, 213, 93, 23);
		contentPane.add(btnNewButton_1);

		textFieldSignature = new JTextField();
		textFieldSignature.setBounds(10, 131, 303, 21);
		contentPane.add(textFieldSignature);
		textFieldSignature.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("\u8BF7\u8F93\u5165\u5408\u6CD5\u7684\u7B7E\u540D\uFF1A");
		lblNewLabel_1.setBounds(10, 106, 199, 15);
		contentPane.add(lblNewLabel_1);

		JButton btnNewButton_2 = new JButton("\u8FD4\u56DE");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				closeWindows();
			}
		});
		btnNewButton_2.setBounds(251, 213, 93, 23);
		contentPane.add(btnNewButton_2);
	}

	public void setSign(byte[] sign) {
		this.sign = sign;
	}

	public void closeWindows() {
		this.dispose();
	}
}
