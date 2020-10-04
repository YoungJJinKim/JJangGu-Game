package p111;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.*;
import javax.swing.*;

public class MainFrame extends JFrame {
	public Db db;
	int score; // 점수
	GamePanel panel;

	GameThread gThread;

	public MainFrame() {
		db = new Db();
		JButton btnExit = new JButton("Exit");
		setTitle("피하기 게임");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(500, 100, 700, 900);
		setResizable(false);
		panel = new GamePanel();
		JButton btn = new JButton("EXIT");
		//db = new Db();
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				System.exit(0);
			}
		});
		add(panel, BorderLayout.CENTER);
		setVisible(true);
		// 게임 진행시키는 스레드 객체 생성 및 스타트
		gThread = new GameThread();
		gThread.start(); // run() 메소드 자동실행!!
		// 프레임에 키보드 입력에 반응하는 keyListner 등록
		addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// 눌러진 키가 무엇인지 알아내기
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_LEFT:
					panel.dx = 0; // 원랜 getsetter 만들어야함
					break;
				case KeyEvent.VK_RIGHT:
					panel.dx = 0;
					break;
				}
				btnExit.setBounds(600, 10, 97, 23);
				panel.add(btnExit);
				setFocusable(true);
				// 방향키 2개 구분
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// 눌러진 키가 무엇인지 알아내기
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_LEFT:
					panel.dx = -8; // 원랜 getsetter 만들어야함
					break;
				case KeyEvent.VK_RIGHT:
					panel.dx = 8;
					break;
				}
				// 방향키 2개 구분
			}
		});
	}// 생성자
		///////////////////////////////////////////////////////

	class GamePanel extends JPanel { // 게임화면 그려낼 Panel
		// 화면에 보여질 이미지 객체 참조변수 - 멤버변수
		Image imgBack, imgPlayer, imgEnemy;
		int width, height;// 패널 사이즈 가져오기
		int x, y, w, h;// xy : 플레이어의 중심 좌표 / wh : 이미지 절반폭;
		int dx = 0, dy = 0;// 플레이어 이미지의 이동속도, 이동방향
		// 적군 객체 참조변수, 여러마리일수있으므로 ArrayList(유동적 배열) 활용
		ArrayList<Enemy> enemies = new ArrayList<Enemy>();
		

		public GamePanel() {
			// GUI 관련 프로그램의 편의를 위해 만들어진 도구상자(Toolkit) 객체
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			// 생성자에서 사이즈를 구하려하면 무조건 0임, 아직 패널이 안붙어서 사이즈를 모르기때문
			// width = getWidth();
			// height = getHeight();
			imgPlayer = toolkit.getImage("D:\\aaa.jpg");// 플레이어 이미지 객체
			imgBack = toolkit.getImage("D:\\DDuck.jpg");// 배경 이미지
			imgEnemy = toolkit.getImage("D:\\zzanga.png");// 적군 이미지 객체
		}// 생성자
			// 화면에 보여질때 보여질 내용물 작업을 수행하는 메소드 : 자동 실행(콜백 메소드)

		@Override
		protected void paintComponent(Graphics g) {
			// 화면에 보여질 작업 코딩
			
			if (width == 0 || height == 0) { // 처음 호출시엔 느려서 안보이다 이후 보임
				width = getWidth();
				height = getHeight();
				// 리사이징
				imgBack = imgBack.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				imgPlayer = imgPlayer.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
				x = width / 2;// 플레이어의 좌표 계산
				y = height - 50;
				w = 64;
				h = 10;
			}
			
			// 이곳에 화가객체가 있으므로 그림 그리는 작업은 무조건 여기서
			g.drawImage(imgBack, 0, 0, this);// 배경 그리기
			for (Enemy t : enemies) {
				g.drawImage(t.img, t.x - t.w, t.y - t.h, this);
			}
			g.drawImage(imgPlayer, x - w, y - h, this);// 플레이어
			g.setFont(new Font(null, Font.BOLD, 20));// 점수 표시하기
			g.drawString("Score : " +score, 15, 30);
			
			//(db.login(tf.getText(),pass)==1)
			// 여러장면 만들기 위해 일정시간마다 다시 그리기(re painting)
		}// paintComponent method

		void move() { // 플레이어 움직이기(좌표 변경)
			
			// 적군들 움직이기
			// 중간에 배열의 개수 변경될 여지가 있다면
			// 맨 마지막 요소부터 거꾸로 0번 요소까지 역으로 처리해야함.
			for (int i = enemies.size() - 1; i >= 0; i--) {
				Enemy t = enemies.get(i);
				t.move();
				if (t.isDead) // ArrayList에서 제거
					enemies.remove(i);
			}
			x += dx;
			y += dy;
			// 플레이어 좌표가 화면 밖으로 나가지 않도록
			if (x < w)
				x = w;
			if (x > width - w)
				x = width - w;
			if (y < h)
				y = h;
			if (y > height - h)
				y = height - h;
		}

		void makeEnemy() { // 적군 생성 메소드
			if (width == 0 || height == 0)
				return;
			Random rnd = new Random();// 50번에 한번꼴로 만들기
			int n = rnd.nextInt(15);
			if (n == 0) {
				enemies.add(new Enemy(imgEnemy, width, height));
			}
		}// makeenemy
			// 충돌체크 작업 계산 메소드

		void checkCollision() { // 플레이어와 적군의 충돌
			for (Enemy t : enemies) {
				int left = t.x - t.w;
				int right = t.x + t.w;
				int top = t.y - t.h;
				int bottom = t.y + t.h;
				if (x >= left && x <= right && y >= top && y <= bottom) {
					t.isDead = true; // 충돌했음
					score += 5;
				}
			}
		}
	}// GamePanel class
		///////////////////////////////////////////////////////
		// 일정 시간마다 개엠화면을 갱신시키는 작업 수행하는 별도 스레드 클래스

	class GameThread extends Thread {
		@Override
		public void run() {
			while (true) {
				// 적군 객체 만들어내는 기능 메소드 호출
				panel.makeEnemy();
				// GamePanel의 플레이어 좌표 변경
				// panel.x += -1;// 객체의 멤버값 변경은
				// panel.y += -5;/// 그 객체가 스스로 하도록 하는것이 OOP이 기본 모티브
				panel.move();
				panel.checkCollision();// 충돌 체크 기능 호출
				panel.repaint();// GamePanel의 화면 갱신
				try { // 너무 빨리 돌아서 천천히 돌도록
					sleep(25);
				} catch (InterruptedException e) {
				}
				
			}
		}
	}
}
