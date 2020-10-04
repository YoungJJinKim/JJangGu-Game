package p111;

import java.awt.Image;
import java.util.Random;

public class Enemy {
	Image img; //이미지 참조변수
	int x, y; //이미지 중심 좌표
	int w, h; //이미지 절반폭, 절반높이
	int dy; //적군의 변화량
	int width, height; //화면(panel)의 사이즈
	//본인 객체가 죽었는지 여부!
	boolean isDead = false;
	public Enemy(Image imgEnemy, int width, int height) {
		this.width = width;
		this.height = height;
		//멤버변수 값 초기화..
		img = imgEnemy.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		w = 32; //이미지 절반넓이
		h = 32;
		Random rnd = new Random();
		x = rnd.nextInt(width - w * 2) + w; //w ~ width - w
		y = -h;
		dy =+ rnd.nextInt(15) + 1;//떨어지는 속도 랜덤하게
	}
	void move() { //Enemy의 움직이는 기능 메소드
		y += dy;
		//만약 화면 밑으로 나가버리면 객체 없애기
		if( y > height + h ) { //ArrayList에서 제거
			isDead = true; //죽음 표식! 
		}
	}
}