package Mac;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.io.MacInputStream;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.jcajce.provider.digest.SHA1.SHA1Mac;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.MacCalculator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.crypto.KeyGenerator;
//import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JPasswordField;

public class Mac extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldInput;
	private JTextField textFieldMacValue;
	private JComboBox comboBoxMacAlgorithm;
	private File file;
	private JButton btnFileChooser;
	private JPasswordField passwordFieldMacPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mac frame = new Mac();
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
	public Mac() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 714, 457);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("\u8BF7\u8F93\u5165\u5B57\u7B26\u4E32\uFF1A");
		lblNewLabel.setBounds(93, 13, 110, 15);
		contentPane.add(lblNewLabel);

		textFieldInput = new JTextField();
		textFieldInput.setBounds(93, 38, 303, 21);
		contentPane.add(textFieldInput);
		textFieldInput.setColumns(10);

		JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (comboBox.getSelectedItem().toString() == "string") {
					textFieldInput.setEditable(true);
					btnFileChooser.setEnabled(false);
				} else if (comboBox.getSelectedItem().toString() == "file") {
					textFieldInput.setEditable(false);
					btnFileChooser.setEnabled(true);
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "string", "file" }));
		comboBox.setBounds(10, 38, 66, 21);
		contentPane.add(comboBox);

		btnFileChooser = new JButton("...");
		btnFileChooser.setEnabled(false);
		btnFileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser("D:/");
				int value = chooser.showOpenDialog(Mac.this);
				if (value == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					textFieldInput.setText(file.getAbsolutePath());
				}
			}
		});
		btnFileChooser.setBounds(406, 37, 93, 23);
		contentPane.add(btnFileChooser);

		textFieldMacValue = new JTextField();
		textFieldMacValue.setEditable(false);
		textFieldMacValue.setBounds(93, 155, 303, 21);
		contentPane.add(textFieldMacValue);
		textFieldMacValue.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("MAC\u7801\uFF1A");
		lblNewLabel_1.setBounds(10, 158, 54, 15);
		contentPane.add(lblNewLabel_1);

		JButton btnNewButtonCal = new JButton("calculator");
		btnNewButtonCal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textFieldInput.getText() != null) {
					if (!(new String(passwordFieldMacPassword.getPassword()).equals(""))) {
						if (comboBox.getSelectedItem().toString() == "string") {
							byte[] tempData = textFieldInput.getText().getBytes();
							try {
								textFieldMacValue.setText(
										new HexBinaryAdapter().marshal(getMacCode(tempData, new String(passwordFieldMacPassword.getPassword()), comboBoxMacAlgorithm.getSelectedItem().toString())));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (comboBox.getSelectedItem().toString() == "file") {
							try {
								textFieldMacValue.setText(calculateFileMacCode(file, comboBoxMacAlgorithm.getSelectedItem().toString(), new String(passwordFieldMacPassword.getPassword())));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					else {
						JOptionPane.showMessageDialog(null, "请输入消息校验密钥！");
					}
				} else {
					JOptionPane.showMessageDialog(null, "请输入或选择需要计算Mac码的数据！");
				}
			}
		});
		btnNewButtonCal.setBounds(90, 218, 93, 23);
		contentPane.add(btnNewButtonCal);

		comboBoxMacAlgorithm = new JComboBox();
		comboBoxMacAlgorithm.setModel(new DefaultComboBoxModel(new String[] { "HmacMD5", "HmacSHA1", "HmacSHA256", "HmacSHA384", "HmacSHA512" }));
		comboBoxMacAlgorithm.setBounds(192, 124, 86, 21);
		contentPane.add(comboBoxMacAlgorithm);

		JLabel lblNewLabel_2 = new JLabel("\u8BF7\u9009\u62E9Mac\u8BA1\u7B97\u7B97\u6CD5\uFF1A");
		lblNewLabel_2.setBounds(10, 127, 142, 15);
		contentPane.add(lblNewLabel_2);

		passwordFieldMacPassword = new JPasswordField();
		passwordFieldMacPassword.setBounds(93, 93, 303, 21);
		contentPane.add(passwordFieldMacPassword);

		JLabel lblNewLabel_3 = new JLabel("\u6D88\u606F\u9A8C\u8BC1\u53E3\u4EE4\uFF1A");
		lblNewLabel_3.setBounds(93, 69, 112, 15);
		contentPane.add(lblNewLabel_3);
	}

	public byte[] getKey(String algorithmName) throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithmName);
		byte[] password = new String(passwordFieldMacPassword.getPassword()).getBytes();
		keyGenerator.init(new SecureRandom(password));
		return keyGenerator.generateKey().getEncoded();
	}

	public static byte[] getMacCode(byte[] data, String userKey, String algorithmName) throws Exception {
		SecretKey secretKey = new SecretKeySpec(userKey.getBytes(), algorithmName);
		javax.crypto.Mac mac = javax.crypto.Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return mac.doFinal(data);
	}

	public static String calculateFileMacCode(File file, String algorithmName, String userKey) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		byte[] bs = new byte[1024];
		SecretKey secretKey = new SecretKeySpec(userKey.getBytes(), algorithmName);
		javax.crypto.Mac mac = javax.crypto.Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		int n = 0;
		while ((n = fis.read(bs)) != -1)
		{
			mac.update(bs, 0, n);
		}
		return new HexBinaryAdapter().marshal(mac.doFinal());
	}
}
