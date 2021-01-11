import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public class MainFrame extends JFrame{
	private SharedObj obj;
	private InGamePanel gameP;
	private MainMenuPanel menuP;

	private Clip bgm;
	private FloatControl bgmVol;
	private AudioInputStream stream;
	
	public MainFrame() {
		super("AvoidTheBullets");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		launchFont(1);
		launchFont(2);
		obj = new SharedObj();
		gameP = new InGamePanel(this, obj);
		menuP = new MainMenuPanel(this, obj);
		setContentPane(menuP);
		pack();
		setLocationRelativeTo(null);
		
		try {
			bgm = AudioSystem.getClip();
			stream = AudioSystem.getAudioInputStream(new File("./audio/introbgm.wav"));
			bgm.open(stream);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {} 

		 bgmVol = (FloatControl)bgm.getControl(FloatControl.Type.MASTER_GAIN);
		 bgm.loop(-1);
		 bgmVol.setValue(obj.getBGMVol()-70);
	}
	public void updateVol() {
		bgmVol.setValue(obj.getBGMVol()-70);
	}
	/**
	화면전환 기능을 제공한다.
	 * @param name
	 */
	public void changePanel(String name) {
		if(name == "game") {
			bgm.stop();
			setContentPane(gameP);
			pack();
			setLocationRelativeTo(null);
			gameP.start();
		}
		else if(name == "menu") {
			bgm.setFramePosition(0);
			bgm.loop(-1);
			setContentPane(menuP);
			pack();
			setLocationRelativeTo(null);
		}
	}
	/**
	font파일에 있는 폰트파일을 내장폰트처럼 사용할 수 있도록 서비스한다.
	 * @param n
	 */
	public void launchFont(int n) {
		Font fnt;
		try {
			fnt = Font.createFont(Font.TRUETYPE_FONT, new File("font/visitor"+n+".ttf"));
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(fnt);
		} catch (FontFormatException | IOException e) {
			fnt = null;
			e.printStackTrace();
		}
	}
}
