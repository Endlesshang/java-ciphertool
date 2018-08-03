package HashTool;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.MessageDigest;
import java.security.Security;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.beans.PropertyChangeEvent;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class HashTool extends JFrame {

	// 用枚举类来定义是否启用HASH的布尔参数，当触发了打勾的鼠标事件时把相应的布尔代数赋值为真。
	private JPanel contentPane;
	private JTextField textFieldMD5;
	private JTextField textFieldMessage;
	private JTextField textFieldSHA3_224;
	private JCheckBox CheckBoxSHA3_224;
	private JCheckBox CheckBoxMD5;
	private boolean MD5 = false;
	private boolean SHA1 = false;
	private boolean SHA224 = false;
	private boolean SHA256 = false;
	private boolean SHA384 = false;
	private boolean SHA512 = false;
	private boolean SHA3_224 = false;
	private boolean SHA3_256 = false;
	private boolean SHA3_384 = false;
	private boolean SHA3_512 = false;

	private boolean[] HashArray = { MD5, SHA1, SHA224, SHA256, SHA384, SHA512, SHA3_224, SHA3_256, SHA3_384, SHA3_512 };
	private boolean[] Hashboolean = new boolean[10];
	private String[] HashName = { "MD5", "SHA1", "SHA224", "SHA256", "SHA384", "SHA512", "SHA3-224", "SHA3-256", "SHA3-384", "SHA3-512" };
	private JTextField textFieldSHA1;
	private JTextField textFieldSHA224;
	private JTextField textFieldSHA256;
	private JTextField textFieldSHA384;
	private JTextField textFieldSHA512;
	private JTextField textFieldSHA3_256;
	private JTextField textFieldSHA3_384;
	private JTextField textFieldSHA3_512;
	private JCheckBox CheckBoxSHA224;
	private JCheckBox CheckBoxSHA256;
	private JCheckBox CheckBoxSHA384;
	private JCheckBox CheckBoxSHA512;
	private JCheckBox CheckBoxSHA3;
	private JCheckBox CheckBoxSHA3_1;
	private JCheckBox CheckBoxSHA3_2;
	private JButton btnFileChoose;
	private JFileChooser chooser;
	private File file;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HashTool frame = new HashTool();
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
	public HashTool() {
		for (boolean b : Hashboolean) {
			b = false;
		}
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\dell\\Desktop\\QQ\u622A\u56FE20170515100747.jpg"));
		setTitle("HashCalc");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 763, 458);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("DataType");
		lblNewLabel.setBounds(10, 10, 54, 15);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Data:");
		lblNewLabel_1.setBounds(129, 10, 54, 15);
		contentPane.add(lblNewLabel_1);

		textFieldMessage = new JTextField();
		textFieldMessage.setBounds(129, 28, 512, 21);
		contentPane.add(textFieldMessage);
		textFieldMessage.setColumns(10);

		JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (comboBox.getSelectedItem().toString() == "file") {
					btnFileChoose.setEnabled(true);
					textFieldMessage.setEditable(false);
				}
				if (comboBox.getSelectedItem().toString() == "string") {
					btnFileChoose.setEnabled(false);
					textFieldMessage.setEditable(true);
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "string", "file" }));
		comboBox.setBounds(10, 28, 68, 21);
		contentPane.add(comboBox);

		JCheckBox chckbxNewCheckBox = new JCheckBox("HMAC");
		chckbxNewCheckBox.setEnabled(false);
		chckbxNewCheckBox.setBounds(6, 55, 103, 23);
		contentPane.add(chckbxNewCheckBox);

		CheckBoxMD5 = new JCheckBox("MD5");
		CheckBoxMD5.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Hashboolean[0] = CheckBoxMD5.isSelected();
			}
		});
		CheckBoxMD5.setBounds(6, 80, 72, 23);
		contentPane.add(CheckBoxMD5);

		JButton btnCalculate = new JButton("Calculator");
		btnCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				long startTime = System.currentTimeMillis();
				if (textFieldMessage.getText() != null) {
					if (comboBox.getSelectedItem().toString() == "string") {
						int i = 0;
						String pString = textFieldMessage.getText();
						for (i = 0; i < 9; i++) {
							if (Hashboolean[i] == true) {
								OrderArray(i, pString);
							}
						}
					} else if (comboBox.getSelectedItem().toString() == "file") 
					{
						int i = 0;
						String pString = textFieldMessage.getText();
						for (i = 0; i < 9; i++) 
						{
							try {
								long start1Time = System.currentTimeMillis();
								if (Hashboolean[i] == true) {
									fileOrderArray(i, pString);
									long end1Time = System.currentTimeMillis();
									System.out.println(HashName[i] + "运行" + (end1Time - start1Time) + "ms");
								}
							} catch (Exception e)
							{
								e.printStackTrace();
							}
						}
						long endTime = System.currentTimeMillis();
						JOptionPane.showMessageDialog(null, "计算hash值所使用的时间为：" + (endTime - startTime) + "ms");
					}
				} else {
					JOptionPane.showMessageDialog(null, "请输入hash码数据！");
				}
			}
		});
		btnCalculate.setBounds(341, 386, 93, 23);
		contentPane.add(btnCalculate);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnClose.setBounds(522, 386, 68, 23);
		contentPane.add(btnClose);

		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textFieldMD5.setText("");
				textFieldSHA1.setText("");
				textFieldSHA224.setText("");
				textFieldSHA256.setText("");
				textFieldSHA384.setText("");
				textFieldSHA512.setText("");
				textFieldSHA3_224.setText("");
				textFieldSHA3_256.setText("");
				textFieldSHA3_384.setText("");
				textFieldSHA3_512.setText("");
				textFieldMessage.setText("");
				CheckBoxMD5.setSelected(false);
				CheckBoxSHA224.setSelected(false);
				file = null;
			}
		});
		btnReset.setBounds(444, 386, 68, 23);
		contentPane.add(btnReset);

		textFieldMD5 = new JTextField();
		textFieldMD5.setEditable(false);
		textFieldMD5.setBounds(129, 81, 512, 21);
		contentPane.add(textFieldMD5);
		textFieldMD5.setColumns(10);

		CheckBoxSHA3_224 = new JCheckBox("SHA3-224");
		CheckBoxSHA3_224.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Hashboolean[6] = CheckBoxSHA3_224.isSelected();
			}
		});
		CheckBoxSHA3_224.setBounds(6, 230, 99, 23);
		contentPane.add(CheckBoxSHA3_224);

		textFieldSHA3_224 = new JTextField();
		textFieldSHA3_224.setEditable(false);
		textFieldSHA3_224.setBounds(129, 231, 512, 21);
		contentPane.add(textFieldSHA3_224);
		textFieldSHA3_224.setColumns(10);

		JCheckBox CheckBoxSHA1 = new JCheckBox("SHA1");
		CheckBoxSHA1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Hashboolean[1] = CheckBoxSHA1.isSelected();
			}
		});
		CheckBoxSHA1.setBounds(6, 105, 103, 23);
		contentPane.add(CheckBoxSHA1);

		textFieldSHA1 = new JTextField();
		textFieldSHA1.setEditable(false);
		textFieldSHA1.setBounds(129, 106, 512, 21);
		contentPane.add(textFieldSHA1);
		textFieldSHA1.setColumns(10);

		CheckBoxSHA224 = new JCheckBox("SHA224");
		CheckBoxSHA224.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Hashboolean[2] = CheckBoxSHA224.isSelected();
			}
		});
		CheckBoxSHA224.setBounds(6, 130, 103, 23);
		contentPane.add(CheckBoxSHA224);

		textFieldSHA224 = new JTextField();
		textFieldSHA224.setEditable(false);
		textFieldSHA224.setBounds(129, 131, 512, 21);
		contentPane.add(textFieldSHA224);
		textFieldSHA224.setColumns(10);

		CheckBoxSHA256 = new JCheckBox("SHA256");
		CheckBoxSHA256.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Hashboolean[3] = CheckBoxSHA256.isSelected();
			}
		});
		CheckBoxSHA256.setBounds(6, 155, 103, 23);
		contentPane.add(CheckBoxSHA256);

		textFieldSHA256 = new JTextField();
		textFieldSHA256.setEditable(false);
		textFieldSHA256.setBounds(129, 156, 512, 21);
		contentPane.add(textFieldSHA256);
		textFieldSHA256.setColumns(10);

		CheckBoxSHA384 = new JCheckBox("SHA384");
		CheckBoxSHA384.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Hashboolean[4] = CheckBoxSHA384.isSelected();
			}
		});
		CheckBoxSHA384.setBounds(6, 180, 103, 23);
		contentPane.add(CheckBoxSHA384);

		textFieldSHA384 = new JTextField();
		textFieldSHA384.setEditable(false);
		textFieldSHA384.setBounds(129, 181, 512, 21);
		contentPane.add(textFieldSHA384);
		textFieldSHA384.setColumns(10);

		CheckBoxSHA512 = new JCheckBox("SHA512");
		CheckBoxSHA512.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Hashboolean[5] = CheckBoxSHA512.isSelected();
			}
		});
		CheckBoxSHA512.setBounds(6, 205, 103, 23);
		contentPane.add(CheckBoxSHA512);

		textFieldSHA512 = new JTextField();
		textFieldSHA512.setEditable(false);
		textFieldSHA512.setBounds(129, 206, 512, 21);
		contentPane.add(textFieldSHA512);
		textFieldSHA512.setColumns(10);

		CheckBoxSHA3 = new JCheckBox("SHA3-256");
		CheckBoxSHA3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Hashboolean[7] = CheckBoxSHA3.isSelected();
			}
		});
		CheckBoxSHA3.setBounds(6, 255, 103, 23);
		contentPane.add(CheckBoxSHA3);

		textFieldSHA3_256 = new JTextField();
		textFieldSHA3_256.setEditable(false);
		textFieldSHA3_256.setBounds(129, 256, 512, 21);
		contentPane.add(textFieldSHA3_256);
		textFieldSHA3_256.setColumns(10);

		CheckBoxSHA3_1 = new JCheckBox("SHA3-384");
		CheckBoxSHA3_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Hashboolean[8] = CheckBoxSHA3_1.isSelected();
			}
		});
		CheckBoxSHA3_1.setBounds(6, 280, 103, 23);
		contentPane.add(CheckBoxSHA3_1);

		textFieldSHA3_384 = new JTextField();
		textFieldSHA3_384.setEditable(false);
		textFieldSHA3_384.setBounds(129, 281, 512, 21);
		contentPane.add(textFieldSHA3_384);
		textFieldSHA3_384.setColumns(10);

		CheckBoxSHA3_2 = new JCheckBox("SHA3-512");
		CheckBoxSHA3_2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Hashboolean[9] = CheckBoxSHA3_2.isSelected();
			}
		});
		CheckBoxSHA3_2.setBounds(6, 305, 103, 23);
		contentPane.add(CheckBoxSHA3_2);

		textFieldSHA3_512 = new JTextField();
		textFieldSHA3_512.setEditable(false);
		textFieldSHA3_512.setBounds(129, 306, 512, 21);
		contentPane.add(textFieldSHA3_512);
		textFieldSHA3_512.setColumns(10);

		btnFileChoose = new JButton("Broser");
		btnFileChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooser = new JFileChooser("D:/");
				int value = chooser.showOpenDialog(HashTool.this);
				if (value == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile();
					textFieldMessage.setText(file.getAbsolutePath());
				}
			}
		});
		btnFileChoose.setEnabled(false);
		btnFileChoose.setBounds(651, 27, 68, 23);
		contentPane.add(btnFileChoose);
	}

	public static String HashCode(String plainText, String cipherName) {
		try {
			//声明消息摘要对象，指定为形参对应的算法
			MessageDigest mDigest = MessageDigest.getInstance(cipherName);
			
			//把消息输入到消息摘要对象中
			mDigest.update(plainText.getBytes());
			
			//用消息摘要对象计算hash值
			byte[] digest = mDigest.digest();
			return Hex.toHexString(digest).toUpperCase();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return plainText;
	}

	public void OrderArray(int i, String pString) {
		switch (i) {
		case 0:
			textFieldMD5.setText(HashCode(pString, "MD5"));
			break;
		case 1:
			textFieldSHA1.setText(HashCode(pString, "SHA1"));
			break;
		case 2:
			textFieldSHA224.setText(HashCode(pString, "SHA224"));
			break;
		case 3:
			textFieldSHA256.setText(HashCode(pString, "SHA256"));
			break;
		case 4:
			textFieldSHA384.setText(HashCode(pString, "SHA384"));
			break;
		case 5:
			textFieldSHA512.setText(HashCode(pString, "SHA512"));
			break;
		case 6:
			textFieldSHA3_224.setText(HashCode(pString, "SHA3-224"));
			break;
		case 7:
			textFieldSHA3_256.setText(HashCode(pString, "SHA3-256"));
			break;
		case 8:
			textFieldSHA3_384.setText(HashCode(pString, "SHA3-384"));
			break;
		case 9:
			textFieldSHA3_512.setText(HashCode(pString, "SHA3-512"));
			break;
		default:
			break;
		}
	}

	public void fileOrderArray(int i, String pString) throws Exception {
		switch (i) {
		case 0:
			textFieldMD5.setText(JudgeToHash.FileEncrypt(file, "MD5"));
			break;
		case 1:
			textFieldSHA1.setText(JudgeToHash.FileEncrypt(file, "SHA1"));
			break;
		case 2:
			textFieldSHA224.setText(JudgeToHash.FileEncrypt(file, "SHA224"));
			break;
		case 3:
			textFieldSHA256.setText(JudgeToHash.FileEncrypt(file, "SHA256"));
			break;
		case 4:
			textFieldSHA384.setText(JudgeToHash.FileEncrypt(file, "SHA384"));
			break;
		case 5:
			textFieldSHA512.setText(JudgeToHash.FileEncrypt(file, "SHA512"));
			break;
		case 6:
			textFieldSHA3_224.setText(JudgeToHash.FileEncrypt(file, "SHA3-224"));
			break;
		case 7:
			textFieldSHA3_256.setText(JudgeToHash.FileEncrypt(file, "SHA3-256"));
			break;
		case 8:
			textFieldSHA3_384.setText(JudgeToHash.FileEncrypt(file, "SHA3-384"));
			break;
		case 9:
			textFieldSHA3_512.setText(JudgeToHash.FileEncrypt(file, "SHA3-512"));
			break;
		default:
			break;
		}
	}
}
