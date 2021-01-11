import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GameFieldPanel extends JPanel{

	private DrawTH drawFieldThread;
	private Image dbImage;
	private Graphics dbg;
	private Image fieldImg;
	private Player user;
	private SharedObj obj;
	private ShootTH shootThread;

	private Clip shoot;
	private FloatControl shootVol;
	
	private Clip bgm;
	private FloatControl bgmVol;
	
	private Clip count1;
	private FloatControl count1Vol;
	
	private Clip count2;
	private FloatControl count2Vol;
	
	private Clip endbgm;
	private FloatControl endbgmVol;
	
	public GameFieldPanel(SharedObj _obj) {
		setLayout(null);
		setBounds(0,0,1080,720);
		obj = _obj;
		//플레이어 객체는 재활용하므로 참조한다.
		user = new Player(525, 335);
		addKeyListener(user.getPlayerL());
		initBlock();
			try {
				shoot = AudioSystem.getClip();
				bgm = AudioSystem.getClip();
				count1 = AudioSystem.getClip();
				count2 = AudioSystem.getClip();
				endbgm = AudioSystem.getClip();
			} catch (LineUnavailableException e) {}
			shootVol = loadAudio(shoot, "./audio/shoot.wav");
			bgmVol = loadAudio(bgm, "./audio/ingamebgm.wav");
			count1Vol = loadAudio(count1, "./audio/beep1.wav");
			count2Vol = loadAudio(count2, "./audio/beep2.wav");
			endbgmVol = loadAudio(endbgm, "./audio/gameover.wav");
		}
	/**
	해당 클래스 최초 생성시 호출되며 한번 생성된 객체들은 계속 사용된다.
	 */
	public void initBlock() {
		new Block(0, 0, "up");
		new Block(0, 0, "left");
		new Block(1015, 0, "right");
		new Block(0, 690, "down");
		
		new Block(100, 200, "rock");
		new Block(900, 540, "rock");
		new Block(120, 640, "rock");
		new Block(700, 200, "rock");
		
		new Block(300, 210, "Tree");
		new Block(500, 500, "Tree");
	}
	/**
	게임 시작을 위한 상태로 필드정도를 초기화 한다.
	플레이어 관련 스레드들을 생성하고 시작한다.
	그러나 키입력을 받지 못하기 때문에 실제로 아무런 변화는 없다.
	 */
	public void initField() {
		obj.flag = true;
		user.init(525,335);
		user.start(obj);
		shootVol.setValue(obj.geteffectVol()-70);
		bgmVol.setValue(obj.getBGMVol()-70);
		count1Vol.setValue(obj.geteffectVol()-70);
		count2Vol.setValue(obj.geteffectVol()-70);
		endbgmVol.setValue(obj.getBGMVol()-70);
		repaint();
	}
	/**
	 오디오를 업로드한다.
	 * @param clip
	 * @param path
	 * @return
	 */
	private FloatControl loadAudio(Clip clip, String path) {
		AudioInputStream stream;
		try {
			stream = AudioSystem.getAudioInputStream(new File(path));
			clip.open(stream);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {} 

		return (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
	}
	/**
	n초만큼 카운트 다운을 진행한다.
	 * @param n
	 */
	public void countDown(int n)
	{
		CountTH count = new CountTH(n);
		count.start();
		try {count.join();}
		catch (InterruptedException e) {};
		
	}
	public void startTH()
	{
		shootThread = new ShootTH();
		drawFieldThread = new DrawTH();
		
		shootThread.start();
		drawFieldThread.start();
	}
	/**
	게임이 진행되는 흐름을 모두 담고 있다. 해당 메소드는 게임 진행에 필요한 다른 스레드들을 시작하도록 요청한다.
	shootThread.interrupt()를 사용하여 사이클을 완료하지 않아도 catch문을 통해 바로 run()메소드를 종료하도록 한다.
	 */
	public void gameLogic() {
		bgm.setFramePosition(0);
		bgm.start();
		Bullet.start(obj);
		startTH();
		user.join();
		setFocusable(false);
		repaint();
		bgm.stop();
		endbgm.setFramePosition(0);
		endbgm.start();
		shootThread.interrupt();
		Bullet.bulletList.clear();
	}
	/**
	실제 그리기정보는 각 클래스에서 자신에게 맞게 정의되어있다.
	이 draw메소드들은 모두 해당 메소드에서 실행된다.
	해당 정보들을 메모리에 업데이트하는 것은 시간이 걸리지만 버퍼에 있는 이미지를 불러오는 작업은 비교적 짧은 시간에 진행된다.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(dbg == null) {
			dbImage = createImage(1080,720);
			dbg = dbImage.getGraphics();
		}
		if(fieldImg == null) {
			fieldImg = new ImageIcon("./image/field 1080x720.png").getImage();
		}
		dbg.drawImage(fieldImg, 0,0,this);
		Block.draw(dbg, this);
		Bullet.draw(dbg);
		user.draw(dbg, this);
		g.drawImage(dbImage,0,0, this);
	}
	
	private class DrawTH extends Thread{
		public DrawTH() {
			this.setPriority(MIN_PRIORITY);
		}
		@Override
		public void run() {
			
			while(obj.flag) {
				synchronized(obj) {
					repaint();
				}
				try {
					Thread.sleep(16);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	private class ShootTH extends Thread{
		private int speed;
		public ShootTH() {
			speed = obj.getBulletSpeed();
		}
		public void run() {

			while(obj.flag){
				synchronized(obj) {
					new Bullet(75, 300, 5).shoot(1, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				synchronized(obj) {
					new Bullet(335, 680, 5).shoot(4, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				synchronized(obj) {
					new Bullet(680, 680, 5).shoot(4, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				synchronized(obj) {
					new Bullet(1005, 313, 5).shoot(3, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				synchronized(obj) {
					new Bullet(690, 40, 5).shoot(2, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				synchronized(obj) {
					new Bullet(310, 40, 5).shoot(2, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				
				synchronized(obj) {
					new Bullet(75, 300, 5).shoot(2, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				synchronized(obj) {
					new Bullet(335, 680, 5).shoot(1, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				synchronized(obj) {
					new Bullet(680, 680, 5).shoot(1, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				synchronized(obj) {
					new Bullet(1005, 313, 5).shoot(4, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				synchronized(obj) {
					new Bullet(690, 40, 5).shoot(3, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
				synchronized(obj) {
					new Bullet(310, 40, 5).shoot(3, speed);
					shoot.setFramePosition(0);
					shoot.start();
				}
				try {Thread.sleep(3000);}
				catch (InterruptedException e) {return;}
			}
			
		}
	}
	private class CountTH extends Thread{
		private JLabel lblcount;
		private int count;
		public CountTH(int n) {
			count = n;
			lblcount = new JLabel(""+count);
			lblcount.setFont(new Font("Visitor TT2 BRK", Font.PLAIN+Font.BOLD, 200));
			lblcount.setBounds(600, 260, 200, 200);
			lblcount.setForeground(Color.red);
			lblcount.setHorizontalAlignment(SwingConstants.CENTER);
			lblcount.setVerticalAlignment(SwingConstants.CENTER);
			add(lblcount);
			repaint();
		}
		public void run() {
			while(count > 1){
				lblcount.setText(""+count);
				count1.setFramePosition(0);
				count1.start();
				count -= 1;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
			lblcount.setText(""+count);
			count2.setFramePosition(0);
			count2.start();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			lblcount.setVisible(false);
		}
	}
}
