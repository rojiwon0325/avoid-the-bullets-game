import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

public class Player {
	private int x, y;
	private int dx, dy;
	private double ax, ay;
	private final int WIDTH = 30, HEIGHT = 50;
	private final int SW = 14, SH = 24;
	private int i,j;
	private Image img;
	private PlayerL keyL;
	private MoveTH moveThread;
	private DrawDynamicTH drawDynamic;
	private boolean AliveFlag;

	public Player(int _x, int _y) {
		keyL = new PlayerL();
		init(_x, _y);
	}
	/**
	플레이어 위치, 생존정보, 이미지, 키 입력정보를 초기화한다.
	키 입력정보를 초기화하지 않으면 이전에 입력했던 키 정보가 눌린체 진행된다.
	 * @param _x
	 * @param _y
	 */
	public void init(int _x, int _y) {
		x = _x;
		y = _y;
		dx = 0;
		dy = 0;
		ax = 0;
		ay = 0;
		i = 2;
		j = 1;
		AliveFlag = true;
		keyL.init();
		img = new ImageIcon("./image/soldier 42x96.png").getImage();
		
	}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return WIDTH;}
	public int getHeight() {return HEIGHT;}
	public PlayerL getPlayerL() {return keyL;}
	/**
	죽었을 때, 마지막 진행방향에 따라 다른 형태의 이미지를 불러온다.
	 * @param g
	 * @param Observer
	 */
	public void draw(Graphics g, ImageObserver Observer) {
		if(!AliveFlag) {
			if(i == 0) {
				g.drawImage(img, x, y, x + WIDTH, y + HEIGHT, 0, 0, 14, 23, Observer);
			}
			else if(i == 1) {
				g.drawImage(img,x + WIDTH/2 - HEIGHT/2+5,y + HEIGHT - WIDTH,x + WIDTH + HEIGHT/2-10, y + HEIGHT, 0, 23, 23, 23 + 16, Observer);
			}
			else if(i == 3) {
				g.drawImage(img,x + WIDTH/2 - HEIGHT/2+5,y + HEIGHT - WIDTH,x + WIDTH + HEIGHT/2-10, y + HEIGHT,0, 23*2 + 16 +1, 23, 23*2 + 16*2, Observer);
			}
			else {
				g.drawImage(img, x, y, x + WIDTH, y + HEIGHT, 0, 23 + 16, 14, 23 + 16 + 23, Observer);
				
			}
		}
		else {
			g.drawImage(img, x, y, x + WIDTH, y + HEIGHT, SW*j,SH*i,SW*(j+1),SH*(i+1), Observer);
	
		}
	}
	public void start(SharedObj obj) {
		moveThread = new MoveTH(obj);
		drawDynamic = new DrawDynamicTH(obj);

		moveThread.start();
		drawDynamic.start();

		return;
	}
	public void join() {
		try {
			moveThread.join();
			drawDynamic.interrupt();
		} catch (InterruptedException e) {}
	}
	/**
	충돌했는지 확인만 한다.
	충돌한 경우 게임이 종료되기 때문에 방향전환과 같은 복잡한 작업을 진행할 필요 없다.
	 * @param B
	 * @return
	 */
 	private boolean isCollide(Bullet B) {
		int bx = B.getX();
		int by = B.getY();
		int br = B.getRadius();
		
		if(bx < x-br || bx > x+WIDTH+br || by < y-br || by > y+HEIGHT+br) {
			return false;
		}//근처에 없으면 바로 종료한다.
		
		else if(bx < x && by < y) {
			if(br*br > (x-bx)*(x-bx)+(y-by)*(y-by) ) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(bx > x+WIDTH && by < y) {
			if(br*br > (x+WIDTH-bx)*(x+WIDTH-bx)+(y-by)*(y-by) ) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(bx < x && by > y+HEIGHT) {
			if(br*br > (x-bx)*(x-bx)+(y+HEIGHT-by)*(y+HEIGHT-by) ) {
				return true;
			}
			else {
				return false;
			}
		}
		else if(bx < x+WIDTH && by > y+HEIGHT) {
			if(br*br > (x+WIDTH-bx)*(x+WIDTH-bx)+(y+HEIGHT-by)*(y+HEIGHT-by) ) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return true;
		}
	}
 	/**
 	플레이어가 Block과 충돌한 경우 적절한 반응을 유도한다.
 	 * @param B
 	 */
 	private void checkCollide(Block B) {
 		int bx = B.getX();
 		int by = B.getY();
 		int bw = B.getWidth();
 		int bh = B.getHeight();
 		
 		if(x + WIDTH < bx || x > bx + bw || y + HEIGHT < by || y > by + bh) {
 			return;
 		}
 		else if(x >= bx && x + WIDTH <= bx + bw && y >= by && y + HEIGHT <= by + bh) {
 			if(ax < 0) {
 				x = bx+bw;
 			}
 			else if(ax > 0) {
 				x = bx - WIDTH;
 			}
 			
 			if(ay < 0) {
 				y = by+bh;
 	
 			}
 			else if(ay > 0) {
 				y = by - HEIGHT;
 			}
 		}// Block의 내부에 있을 경우 
 		else {
 			if(x < bx && x + WIDTH >= bx && ax > 0) {
 				if(x + WIDTH - bx <= ax) {
 					x = bx - WIDTH-1;
 				}
 			}
 			else if(x <= bx + bw && x + WIDTH > bx + bw && ax < 0) {
 				if(bx + bw - x <= -ax) {
 					x = bx+bw+1;
 				}
 				
 			}
 			if(y < by && y + HEIGHT >= by && ay > 0) {
 				
 				if(y + HEIGHT - by <= ay) {
 					y = by - HEIGHT-1;
 				}
 			}
 			else if(y <= by + bh && y + HEIGHT > by + bh && ay < 0) {
 				if(by + bh - y <= -ay) {
 					y = by+bh+1;
 				}
 			}
 		}
 	}
 	/**
 	키입력에 따라 변경된 dx, dy값에 점근적으로 다가가는 ax, ay값을 통해 위치를 변경한다.
 	이러한 가속도 개념을 사용하여 좀더 자연스러운 움직임을 구현하였다.
 	 */
 	private void move() {
 		double v = 1;
 		if(dx == 0) {
 			ax = 0;
 		}
 		else if(dx > 0 && dx > ax) {
 			ax = ax+v;
 		}
 		else if(dx < 0 && dx < ax) {
 			ax = ax-v;
 		}
 		if(dy == 0) {
 			ay = 0;
 		}
 		else if(dy > 0 && dy > ay) {
 			ay = ay+v;
 		}
 		else if(dy < 0 && dy < ay) {
 			ay = ay-v;
 		}
 		x = x + (int)ax;
 		y = y + (int)ay;
 	}
 	
 	private class PlayerL implements KeyListener{
 		private boolean key_UP;
 		private boolean key_DOWN;
 		private boolean key_LEFT;
 		private boolean key_RIGHT;
 		
 		public PlayerL() {
 			init();
 		}
 		public void init() {
 			key_UP = false;
 	 		key_DOWN = false;
 	 		key_LEFT = false;
 	 		key_RIGHT = false;
 		}
 		public void updateDxDy() {
 			if(key_UP && !key_DOWN) {
				dy = -3;
				i = 0;
			}
			else if(key_DOWN && !key_UP) {
				dy = 3;
				i = 2;
			}
			else {
				dy = 0;
			}
			if(key_LEFT && !key_RIGHT) {
				dx = -3;
				i = 3;
			}
			else if(key_RIGHT && !key_LEFT) {
				dx = 3;
				i = 1;
			}
			else {
				dx = 0;
			}
			//j = 0;
			if(Math.abs(dx)+Math.abs(dy) > 4) {
				if(dx > 0) {
					dx = 2;
				}
				else {
					dx = -2;
				}
				if(dy > 0) {
					dy = 2;
				}
				else {
					dy = -2;
				}
			}
			else if(Math.abs(dx)+Math.abs(dy) == 0) {
					j = 1;
			}
 		}
		@Override
		public void keyPressed(KeyEvent e) {
			int keycode = e.getKeyCode();
			if(keycode == KeyEvent.VK_UP) {
				key_UP = true;
				synchronized(drawDynamic) {
					drawDynamic.notify();
				}
			}
			else if(keycode == KeyEvent.VK_DOWN) {
				key_DOWN = true;
				synchronized(drawDynamic) {
					drawDynamic.notify();
				}

			}
			else if(keycode == KeyEvent.VK_LEFT) {
				key_LEFT = true;
				synchronized(drawDynamic) {
					drawDynamic.notify();
				}

				
			}
			else if(keycode == KeyEvent.VK_RIGHT) {
				key_RIGHT = true;
				synchronized(drawDynamic) {
					drawDynamic.notify();
				}

				
			}
			updateDxDy();
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int keycode = e.getKeyCode();
			if(keycode == KeyEvent.VK_UP) {
				key_UP = false;
			}
			else if(keycode == KeyEvent.VK_DOWN) {
				key_DOWN = false;
			}
			else if(keycode == KeyEvent.VK_LEFT) {
				key_LEFT = false;
			}
			else if(keycode == KeyEvent.VK_RIGHT) {
				key_RIGHT = false;
			}
			updateDxDy();
		}

		@Override
		public void keyTyped(KeyEvent e) {}
 		
 	}
 	/**
 	해당 스레드는 obj.flag를 바꾸는 유일한 스레드로서 다른 스레드들의 사이클을 종료시킨다.
 	또한 죽은뒤 잘못된 이미지 변경을 막기위해 drawdynamic.interrupt()를 실행한다.
 	 */
 	private class MoveTH extends Thread{
 		private SharedObj obj;
 		public MoveTH(SharedObj _obj) {
 			obj = _obj;
 			this.setPriority(NORM_PRIORITY);
 			
 		}
		public void run() {
			
			while(obj.flag) {
				synchronized(obj) {
					move();
					for(Block B : Block.blockList) {
						checkCollide(B);
					}
					for(Bullet B : Bullet.bulletList) {
						if(isCollide(B)) {
							drawDynamic.interrupt();
							AliveFlag = false;
							img = new ImageIcon("./image/die 23x78.png").getImage();
							obj.flag = false;
							return;
						}
					}
					
				}
				
				try {Thread.sleep(16);}
				catch (InterruptedException e) {}
			}
			
		}
	}
 	/**
 	주기적으로 데이터 j를 수정한다. 플레이어가 정지했을때는 block상태로 대기한다.
 	주기적인 j값의 수정은 결과적으로 움직이는 모션을 제공한다.
 	이는 draw()를 보면 알 수 있다.
 	 */
	private class DrawDynamicTH extends Thread{
		private SharedObj obj;
 		public DrawDynamicTH(SharedObj _obj) {
 			obj = _obj;
 		}
		public void run() {
			while(obj.flag) {
				synchronized(this) {
					try {wait();}
					catch (InterruptedException e) {return;}
				}
				do{
					j = j == 0 ? 2 : 0;
					try {Thread.sleep(500);}
					catch (InterruptedException e) {return;}
				}while(j != 1);
				
			}
			
		}
	}
	
}
