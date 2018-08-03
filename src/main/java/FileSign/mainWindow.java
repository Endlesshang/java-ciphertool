package FileSign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class mainWindow extends JFrame {

	private JPanel contentPane;
	private byte[] mainSign;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindow frame = new mainWindow();
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
	public mainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("\u7B7E\u540D\u751F\u6210");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Sign sign = new Sign();
				sign.setVisible(true);
				sign.setSize(new Dimension(700, 400));
				sign.addWindowListener(new WindowAdapter() {
					public void windowClosing (WindowEvent e){
						super.windowClosing(e);
						mainSign = sign.getSign();
						System.out.println(mainSign.length);
					}
				});
			}
		});
		btnNewButton.setBounds(64, 120, 93, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("\u7B7E\u540D\u9A8C\u8BC1");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VerSign verSign = new VerSign();
				verSign.setSign(mainSign);
				verSign.setVisible(true);
				verSign.setSize(new Dimension(700, 400));
			}
		});
		btnNewButton_1.setBounds(285, 120, 93, 23);
		contentPane.add(btnNewButton_1);
	}
}
