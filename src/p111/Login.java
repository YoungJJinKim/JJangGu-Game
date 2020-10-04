package p111;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class Login extends JFrame implements ActionListener, MouseListener {
	public Db db;
	private String id;
	private String pass;
	private int score;
	private JTextField tf;
	private JPasswordField pf;
	private JButton login;
	JLabel loginText = new JLabel();
	private boolean isLogin = false;

	 
	public Login() {
		db = new Db();
		
		JPanel idPanel = new JPanel();
		JPanel passPanel = new JPanel();
		tf = new JTextField(12);
		pf = new JPasswordField(10);
		loginText.setForeground(Color.RED);

		JLabel idLabel = new JLabel("ID : ");
		JLabel passLabel = new JLabel("PASSWORD : ");
		login = new JButton("LOGIN");
		login.addActionListener(this);

		idPanel.add(idLabel);
		idPanel.add(tf);

		passPanel.add(passLabel);
		passPanel.add(pf);

		this.add(idPanel);
		this.add(passPanel);
		this.add(login);
		this.add(loginText);
		login.addMouseListener(this);
		setLayout(new FlowLayout());

		setTitle("LOGIN");
		setSize(300, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
//		id=tf.getText();
		char[] pass_1 = pf.getPassword();
		String pass = new String(pass_1,0,pass_1.length);
		if(db.login(tf.getText(),pass)==1) {
			System.out.println("로그인 성공");
			new MainFrame(); // 여기가 프레임 전환 역할
			dispose();
		}else {
			System.out.println("로그인 실패");
			JOptionPane.showMessageDialog(null, "로그인 정보를 확인해 주세요");
		}
		
//		db.record(id, pass);
//		if (e.getSource() == login) {
//			try {
//				if (id.equals(tf.getText()) && pass.equals(pf.getText()))
//					isLogin = true;
//				else
//					isLogin = false;
//				if (isLogin) {
//					loginText.setText("로그인되었습니다. 김영진님 안녕하세요.");
//				} else {
//					loginText.setText("ID 또는 password가 잘못되었습니다.");
//				}
//			} catch (Exception e1) {
//				System.out.println("false");
//			}
//		}
	}
	public class MyWinExit extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0); // 윈도 종료
		}
	}
	public void mouseClicked(MouseEvent e) {
		
		
	}
	public static void main(String[] args) {
		Login my = new Login();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}
