import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

public class Bullet {
	private int x, y;
	private int dx, dy;
	private int dir;
	private int radius;
	public static ArrayList<Bullet> bulletList = new ArrayList<>();
	public Bullet(int _x, int _y, int _radius) {
		x = _x;
		y = _y;
		radius = _radius;
		dx = 0;
		dy = 0;
		dir = 0;
		bulletList.add(this);
		
	}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getRadius() {return radius;}
	/**
	@param _dir Bullet의 방향성
	0: STOP
	1: UP&RIGHT 2: RIGHT&DOWN 3: DOWN&LEFT 4: LEFT&UP
	@param speed 10이상 30이하의 정수
	 */
	public int getDir() {return dir;}
	public void setDir(int _dir) {dir = _dir;}
	/**
	 지정된 속도와 방향에서 랜덤한 각도로 총알을 발사한다.
	 즉, 총알에 속도를 부여한다.
	 * @param _dir 발사방향 
	 * @param speed 총알의 속도
	 */
	public void shoot(int _dir, int speed) {
		Random random = new Random();
		this.dir = _dir;
		if(speed < 10) {
			speed = 10;
		}
		else if(speed > 30) {
			speed = 30;
		}
		if(_dir == 1) {
			dx = random.nextInt(speed - 2) + 1;
			dy = (-1)*(int)Math.sqrt(speed*speed - dx*dx);
		} //UP&RIGHT
		else if(_dir == 2) {
			dx = random.nextInt(speed - 2) + 1;
			dy = (int)Math.sqrt(speed*speed - dx*dx);
		} //RIGHT&DOWN
		else if(_dir == 3) {
			dx = -1 * (random.nextInt(speed - 2) + 1);
			dy = (int)Math.sqrt(speed*speed - dx*dx);
		} //DOWN&LEFT
		else if(_dir == 4) {
			dx = -1 * (random.nextInt(speed - 2) + 1);
			dy = (-1)*(int)Math.sqrt(speed*speed - dx*dx);
		} //LEFT&UP
		else {
			dx = 0;
			dy = 0;
		} //STOP
		return;
	}
	/**
	해당 메소드는 static 메소드로서 리스트에 존재하는 모든 객체에 대한 그리기 작업을 진행한다. 
	 * @param g
	 */
	public static void draw(Graphics g) {
		g.setColor(new Color(225, 215, 0));
		for(Bullet B : bulletList) {
			g.fillOval(B.getX() - B.getRadius(), B.getY() - B.getRadius(), 2 * B.getRadius(), 2 * B.getRadius());
		}
	}

	public static void start(SharedObj obj) {
		new BulletTH(obj).start();
		return;
	}
	/**
	Block객체와의 충돌을 확인하고 충돌했을 경우, 총알의 위치를 약간 이동시킨다.
	이때 방향을 변경하지는 않고 변경해야하는 방향을 반환한다.
	적용된 충돌개념은 이러하다. 원의 중심과 직사각형의 테두리의 최단거리가 반지름보다 작으면 두 객체는 충돌하였다.
	 * @param B 충돌 체크 대상 블록 
	 * @return 변해야하는 방향
	 */
	private int checkCollide(Block B) {
		int rx = B.getX();
		int ry = B.getY();
		int rw = B.getWidth();
		int rh = B.getHeight();
		int testx = x;
		int testy = y;
		double distance = 0;
		int tempdir = dir;
		if(dir == 0) {
			return 0;
		}
		//대략적인 검사로 두 객체의 거리가 충분하면 세밀한 계산을 생략한다.
		if(x < rx-radius || x > rx+rw+radius || y < ry-radius || y > ry+rh+radius) {
			return dir;
		}
		
		
		
		if(x <= rx) {
			testx = rx;
		}
		else if(x >= rx + rw) {
			testx = rx + rw;
		}
		else {
			testx = x;
		}
		
		if(y <= ry) {
			testy = ry;
		}
		else if(y >= ry + rh){
			testy = ry + rh;
		}
		else {
			testy = y;
		}
		
		distance = Math.sqrt((x - testx) * (x - testx) + (y - testy) * (y - testy));

		if(distance > (double)radius && !(testx == x && testy == y)) {
			return dir;
		}

		else if(distance == 0) {
			x = x - dx;
			y = y - dy;
			if(x <= rx) {
				testx = rx;
			}
			else if(x >= rx + rw) {
				testx = rx + rw;
			}
			else {
				testx = x;
			}
			
			if(y <= ry) {
				testy = ry;
			}
			else if(y >= ry + rh){
				testy = ry + rh;
			}
			else {
				testy = y;
			}
			
			distance = Math.sqrt((x - testx) * (x - testx) + (y - testy) * (y - testy));
		}
		
		if(x < testx) {
			x = testx - (testx - x)*(radius)/(int)distance;
		}
		else if(x > testx) {
			x = testx + (x - testx)*(radius)/(int)distance;
		}
		
		if(y < testy) {
			y = testy - (testy - y)*(radius)/(int)distance;
		}
		else if(y > testy) {
			y = testy + (y - testy)*(radius)/(int)distance;
		}
		
		if(dir == 1) {
			if(testy == y) {
				tempdir = 4;
			}
			else if(testx == x) {
				tempdir = 2;
			}
			else {
				if((y-testy)/(x -testx) < (-1) * dy/dx) {
					tempdir = 4;
				}
				else if((y-testy)/(x - testx) > (-1) * dy/dx){
					tempdir = 2;
				}
				else {
					tempdir = 3;
				} // ==
			}
		}
		else if(dir == 2) {
			if(testy == y) {
				tempdir = 3;
			}
			else if(testx == x) {
				tempdir = 1;
			}
			else {
				if((testy-y)/(testx-x) < dy/dx) {
					tempdir = 3;
				}
				else if((testy-y)/(testx-x) > dy/dx){
					tempdir = 1;
				}
				else {
					tempdir = 4;
				} // ==
			}
		}
		else if(dir == 3) {
			if(testy == y) {
				tempdir = 2;
			}
			else if(testx == x) {
				tempdir = 4;
			}
			else {
				if((testy-y)/(x-testx) < (-1) * dy/dx) {
					tempdir = 2;
				}
				else if((testy-y)/(x-testx) > (-1) * dy/dx){
					tempdir = 4;
				}
				else {
					tempdir = 1;
				} // ==
			}
		}
		else{
			if(testy == y) {
				tempdir = 1;
			}
			else if(testx == x) {
				tempdir = 3;
			}
			else {
				if((y-testy)/(x-testx) < dy/dx) {
					tempdir = 1;
				}
				else if((y-testy)/(x-testx) > dy/dx){
					tempdir = 3;
				}
				else {
					tempdir = 2;
				} // ==
			}
		}//dir == 4

		return tempdir;

	}
	/**
	현재 dir값에 맞게 dx, dy값을 변경한다.
	 */
	private void updateDxDy() {
		if(dir == 1) {
			if(dx < 0) {
				dx = dx*(-1);
			}
			if(dy > 0) {
				dy = dy*(-1);
			}
		}
		else if(dir == 2) {
			if(dx < 0) {
				dx = dx*(-1);
			}
			if(dy < 0) {
				dy = dy*(-1);
			}
		}
		else if(dir == 3) {
			if(dx > 0) {
				dx = dx*(-1);
			}
			if(dy < 0) {
				dy = dy*(-1);
			}
		}
		else if(dir == 4) {
			if(dx > 0) {
				dx = dx*(-1);
			}
			if(dy > 0) {
				dy = dy*(-1);
			}
		}
		else {
			dx = 0;
			dy = 0;
		}
		return ;
	}
	private void move() {
		x = dx + x;
		y = dy + y;
	}
	/**
	obj인자는 MainFrame의 SharedObj객체이다.
	while문은 플레이어 스레드에 의해 변경될 것이다.
	 */
	private static class BulletTH extends Thread{
		private SharedObj obj;
		public BulletTH(SharedObj _obj){
			obj = _obj;
			this.setPriority(MAX_PRIORITY);
		}
		public void run() {
			int tempdir;
			int check;
			
			while (obj.flag) {
				synchronized(obj) {
					for (Bullet B : bulletList) {
						tempdir = B.getDir();
						B.move();
						for (Block b : Block.blockList) {
							check = B.checkCollide(b);
							if(check != B.getDir()) {
								if(tempdir != check && tempdir != B.getDir()) {
									tempdir = (B.getDir()+1)%4+1;
								}
								else {
									tempdir = check;
								}
							}
						}
						B.setDir(tempdir);
						B.updateDxDy();
					}
				}//이렇게 복잡한 과정을 거치는 이유는 총알이 여러개의 블록과 동시에 충돌했을 경우에 적절히 반응하도록 하기 위해서이다.
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					
					return;
				}
			}
		
		}
	}
	
}

