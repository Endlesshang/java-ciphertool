package FileEnOrDe;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.security.MessageDigest;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPasswordField;

public class FileDecrypt extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldFilePath;
	private File file;
	private String outFilePath;
	private JTextField textFieldFileSave;
	private boolean isSelectedSave;
	private boolean isSelectedopen;
	private JComboBox comboBoxDeCipherSize;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileDecrypt frame = new FileDecrypt();
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
	public FileDecrypt() {
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u8BF7\u9009\u62E9\u88AB\u52A0\u5BC6\u7684\u6587\u4EF6\uFF1A");
		lblNewLabel.setBounds(27, 22, 141, 15);
		contentPane.add(lblNewLabel);
		
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		textFieldFilePath.setBounds(28, 53, 257, 21);
		contentPane.add(textFieldFilePath);
		textFieldFilePath.setColumns(10);
		
		JButton btnNewButton = new JButton("...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser("D:/");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Allowed file", "enc");
				chooser.setFileFilter(filter);
				int value = chooser.showOpenDialog(FileDecrypt.this);
				if (value == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					textFieldFilePath.setText(file.getAbsolutePath());
				}
			}
		});
		btnNewButton.setBounds(334, 52, 93, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel("\u8BF7\u8F93\u5165\u89E3\u5BC6\u5BC6\u7801\uFF1A");
		lblNewLabel_1.setBounds(27, 157, 146, 15);
		contentPane.add(lblNewLabel_1);
		
		JButton btnNewButtonDecrypt = new JButton("\u89E3\u5BC6\u6587\u4EF6");
		btnNewButtonDecrypt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textFieldFilePath.getText() != null && textFieldFileSave.getText() != null) {
					try {
						FileDeFu(file, new String(passwordField.getPassword()), new File(outFilePath));
					} catch (Exception e2) {
						// TODO: handle exception
						e2.printStackTrace();
					}
				}
				else {
					JOptionPane.showInternalMessageDialog(null, "被加密文件或者解密文件存放目录未选择");
				}
			}
		});
		btnNewButtonDecrypt.setBounds(115, 213, 93, 23);
		contentPane.add(btnNewButtonDecrypt);
		
		JButton btnNewButton_2 = new JButton("\u8FD4\u56DE");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeWindows();
			}
		});
		btnNewButton_2.setBounds(249, 213, 93, 23);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel_2 = new JLabel("\u8BF7\u9009\u62E9\u89E3\u5BC6\u6587\u4EF6\u5B58\u653E\u76EE\u5F55\uFF1A");
		lblNewLabel_2.setBounds(27, 84, 206, 15);
		contentPane.add(lblNewLabel_2);
		
		textFieldFileSave = new JTextField();
		textFieldFileSave.setEditable(false);
		textFieldFileSave.setBounds(27, 122, 258, 21);
		contentPane.add(textFieldFileSave);
		textFieldFileSave.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("...");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser("D:/");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int value = chooser.showSaveDialog(FileDecrypt.this);
				outFilePath = chooser.getSelectedFile() + "/" + file.getName();
				int fileLength = outFilePath.length();
				outFilePath = outFilePath.substring(0, fileLength - 4);
				if (value == JFileChooser.APPROVE_OPTION) {
					textFieldFileSave.setText(outFilePath);
				}
			}
		});
		btnNewButton_1.setBounds(334, 121, 93, 23);
		contentPane.add(btnNewButton_1);
		
		comboBoxDeCipherSize = new JComboBox();
		comboBoxDeCipherSize.setModel(new DefaultComboBoxModel(new String[] {"128", "192", "256"}));
		comboBoxDeCipherSize.setBounds(334, 182, 93, 21);
		contentPane.add(comboBoxDeCipherSize);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(27, 182, 258, 21);
		contentPane.add(passwordField);
	}
	public void FileDeFu(File file, String password, File outFile) {
		try {
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(outFile);
			MessageDigest mDigest = MessageDigest.getInstance("SHA-512");
			byte[] keyValue = mDigest.digest(password.getBytes());
			byte[] fileIdentifier = new byte[16];
			SecretKeySpec spec = new SecretKeySpec(keyValue, 0, 16, "AES");
			if (comboBoxDeCipherSize.getSelectedItem().toString() == "128") {
				spec = new SecretKeySpec(keyValue, 0, 16, "AES");
			}
			else if (comboBoxDeCipherSize.getSelectedItem().toString() == "192") {
				spec = new SecretKeySpec(keyValue, 0, 24, "AES");
			}
			else if (comboBoxDeCipherSize.getSelectedItem().toString() == "256") {
				spec = new SecretKeySpec(keyValue, 0, 32, "AES");
			}
			fis.read(fileIdentifier);
			if (new String(fileIdentifier).equals("hanghanghanghang")) {
				byte[] ivValue = new byte[16];
				fis.read(ivValue);
				IvParameterSpec iv = new IvParameterSpec(ivValue);
				
				//在读完个人标识和初始向量之后，就读取文件中的正确解密密钥
				byte[] correctPassword = new byte[64];
				fis.read(correctPassword);
				
				//如果用户输入的密钥与文件的解密密钥相匹配，则继续解密，否则输出窗口提示密钥不正确
				MessageDigest pDigest = MessageDigest.getInstance("SHA-512");
				byte[] userPassword = password.getBytes();
				userPassword = pDigest.digest(userPassword);
				
				CipherInputStream cis = null;
				if (new String(userPassword).equals(
						new String(correctPassword))) {
					Cipher cipher = Cipher.getInstance(
							"AES/CFB/PKCS5Padding");
					cipher.init(Cipher.DECRYPT_MODE, spec,
							iv);
					cis = new CipherInputStream(fis,
							cipher);
					byte[] bs = new byte[64];
					int n = 0;
					try {
						while ((n = cis.read(bs)) != -1) {
							fos.write(bs, 0, n);
						}
						JOptionPane.showMessageDialog(null,
								"解密成功");
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"所选密钥长度错误！");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "所输入的密钥错误！");
				}
				fos.close();
				cis.close();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "所解密文件的个人标识不是谢杭");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public void closeWindows() {
		this.dispose();
	}
}
