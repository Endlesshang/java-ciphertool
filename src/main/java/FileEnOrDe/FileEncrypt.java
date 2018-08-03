package FileEnOrDe;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.omg.CORBA.portable.ValueBase;

import javax.swing.JTextField;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPasswordField;

public class FileEncrypt extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldFilePath;
	private File file;
	private String FileOutputName;
	private JComboBox comboBoxCipherSize;
	private JPasswordField passwordFieldPassword;
	private JPasswordField passwordFieldPasswordAgain;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FileEncrypt frame = new FileEncrypt();
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
	public FileEncrypt() {
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 623, 415);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textFieldFilePath = new JTextField();
		textFieldFilePath.setEditable(false);
		textFieldFilePath.setBounds(27, 49, 284, 21);
		contentPane.add(textFieldFilePath);
		textFieldFilePath.setColumns(10);
		
		JButton btnNewButton = new JButton("...");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser chooser = new JFileChooser("D:/");
				int value = chooser.showOpenDialog(FileEncrypt.this);
				if (value == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					textFieldFilePath.setText(file.getAbsolutePath());
				}
			}
		});
		btnNewButton.setBounds(331, 48, 93, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("\u9009\u62E9\u6587\u4EF6\u8DEF\u5F84\uFF1A");
		lblNewLabel.setBounds(27, 24, 130, 15);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("\u8BF7\u8F93\u5165\u52A0\u5BC6\u5BC6\u7801\uFF1A");
		lblNewLabel_1.setBounds(27, 85, 121, 15);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("\u8BF7\u518D\u6B21\u8F93\u51FA\u52A0\u5BC6\u5BC6\u7801\uFF1A");
		lblNewLabel_2.setBounds(28, 145, 128, 15);
		contentPane.add(lblNewLabel_2);
		
		JButton btnNewButton_1 = new JButton("\u52A0\u5BC6");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//创建长整形变量保存操作起始时间
				long tb = System.currentTimeMillis();
				String fileName = textFieldFilePath.getText();
				
				//校验文件路径是否为空
				if (fileName == null) {
					JOptionPane.showInternalMessageDialog(null, "文件路径不能为空，请重新选择");
				}
				char[] password = passwordFieldPassword.getPassword();
				char[] passwordAgain = passwordFieldPasswordAgain.getPassword();
				
				//校验两次输入的密码是否一致
				try {
					if (Arrays.equals(password, passwordAgain)) {
						FileInputStream fis = new FileInputStream(fileName);
						
						//在文件末尾加入加密后缀
						String encryptedFileName = fileName + ".enc";
						FileOutputName = encryptedFileName;
						FileOutputStream fos = new FileOutputStream(encryptedFileName);
						MessageDigest mDigest = MessageDigest.getInstance("SHA-512");
						
						//把用户输入的密钥转换为hash值以满足AES长加密长度的需要
						byte[] keyValue = mDigest.digest(new String(password).getBytes());
						
						//创建初始的密钥并先指定为128比特长度
						SecretKeySpec spec = new SecretKeySpec(keyValue, 0, 16, "AES");
						
						//从下拉菜单中获取用户指定的密钥长度并进行重载
						if (comboBoxCipherSize.getSelectedItem().toString() == "128") {
							spec = new SecretKeySpec(keyValue, 0, 16, "AES");
						}
						else if (comboBoxCipherSize.getSelectedItem().toString() == "192") {
							spec = new SecretKeySpec(keyValue, 0, 24, "AES");
						}
						else if (comboBoxCipherSize.getSelectedItem().toString() == "256") {
							spec = new SecretKeySpec(keyValue, 0, 32, "AES");
						}
						
						//使用随机方法来来构建随机的初始向量
						byte[] ivValue = new byte[16];
						Random random = new Random(System.currentTimeMillis());
						random.nextBytes(ivValue);
						IvParameterSpec iv = new IvParameterSpec(ivValue);
						
						//创建cipher对象并指定为AES算法，CFB运行模式以及PKCS5填充方案
						Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
						
						//指定cipher对象并初始化为加密模式
						cipher.init(Cipher.ENCRYPT_MODE, spec, iv);
						
						//把文件输入流封装为文件加密输入流
						CipherInputStream cis = new CipherInputStream(fis, cipher);
						
						//
						fos.write("hanghanghanghang".getBytes());
						fos.write(ivValue);
						
						//要区分是密钥输入错误还是密钥长度错误，要把密钥所计算的hash值写入文件，解密时读取密钥看看对不对
						fos.write(keyValue);
						
						byte[] bs = new byte[64];
						int n = 0;
						while ((n = cis.read(bs)) != -1) {
							fos.write(bs, 0, n);
						}
						long te = System.currentTimeMillis();
						JOptionPane.showMessageDialog(null, "加密成功，加密文件保存路径为：" + encryptedFileName + "，加密时间为：" + (te - tb) + "ms");

						fos.close();
						cis.close();
					}
				else {
					JOptionPane.showMessageDialog(null, "两次口令出入不一致，请重新输入");
					
				}
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(101, 218, 93, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("\u8FD4\u56DE");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeWindows();
			}
		});
		btnNewButton_2.setBounds(243, 219, 93, 23);
		contentPane.add(btnNewButton_2);
		
		comboBoxCipherSize = new JComboBox();
		comboBoxCipherSize.setModel(new DefaultComboBoxModel(new String[] {"128", "192", "256"}));
		comboBoxCipherSize.setBounds(331, 110, 58, 21);
		contentPane.add(comboBoxCipherSize);
		
		passwordFieldPassword = new JPasswordField();
		passwordFieldPassword.setBounds(27, 114, 284, 21);
		contentPane.add(passwordFieldPassword);
		
		passwordFieldPasswordAgain = new JPasswordField();
		passwordFieldPasswordAgain.setBounds(27, 174, 284, 21);
		contentPane.add(passwordFieldPasswordAgain);
		
	}
	public String getFileOutputName() {
		return FileOutputName;
	}
	public void closeWindows() {
		this.dispose();
	}
}
