import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenuPanel extends JPanel {
	private Font fnt;
	private MainFrame parent;
	private SharedObj obj;
	
	private Image imgBack;
	private JButton btnStart;
	private JButton btnSetting;
	private JButton btnRank;
	private JButton btnExit;
	private ButtonListener buttonL;
	
	private RankDialog Rank;
	private SettingDialog Setting;
	
	private FloatControl Vol;
	
	public MainMenuPanel(MainFrame _parent, SharedObj _obj) {
		setPreferredSize(new Dimension(600,720));
		setLayout(null);
		setBackground(new Color(90,104,17));
		
		fnt = new Font("Visitor TT2 BRK", Font.BOLD, 35);
		parent = _parent;
		obj = _obj;
		imgBack = new ImageIcon("./image/menuback 600x720.png").getImage();
		buttonL = new ButtonListener();
		Rank = new RankDialog(parent, obj);
		Setting = new SettingDialog(parent, obj);
		
		btnStart = new JButton("START");
		btnStart.setBounds(235, 360, 140, 50);
		btnStart.setFont(fnt);
		btnStart.setForeground(Color.white);
		btnStart.setContentAreaFilled(false);
		btnStart.setBorderPainted(false);
		btnStart.setFocusPainted(false);
		btnStart.addMouseListener(buttonL);
		add(btnStart);
		
		btnSetting = new JButton("SETTING");
		btnSetting.setBounds(220, 450, 170, 50);
		btnSetting.setFont(fnt);
		btnSetting.setForeground(Color.white);
		btnSetting.setContentAreaFilled(false);
		btnSetting.setBorderPainted(false);
		btnSetting.setFocusPainted(false);
		btnSetting.addMouseListener(buttonL);
		add(btnSetting);
		
		btnRank = new JButton("RANK");
		btnRank.setBounds(235, 545, 140, 50);
		btnRank.setFont(fnt);
		btnRank.setForeground(Color.white);
		btnRank.setContentAreaFilled(false);
		btnRank.setBorderPainted(false);
		btnRank.setFocusPainted(false);
		btnRank.addMouseListener(buttonL);
		add(btnRank);
		
		btnExit = new JButton("EXIT");
		btnExit.setBounds(235, 635, 140, 50);
		btnExit.setFont(fnt);
		btnExit.setForeground(Color.white);
		btnExit.setContentAreaFilled(false);
		btnExit.setBorderPainted(false);
		btnExit.setFocusPainted(false);
		btnExit.addMouseListener(buttonL);
		add(btnExit);
	}
	/**
	호버링효과와 함께 사용된다. 일회용 Clip객체를 생성하여 실행하기 때문에 오디오작업이 중첩되어 진행될 수 있다.
	이렇게 구현하여 하나의 클립을 이용하는 것보다 좀 더 자연스러운 효과음을 구현하였다.
	 */
	public void getSound() {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream stream1 = AudioSystem.getAudioInputStream(new File("./audio/button.wav"));
			clip.open(stream1);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {}
		Vol = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
		Vol.setValue(obj.geteffectVol()-70);
		clip.start();
	}
	public void paint(Graphics g) {
		g.drawImage(imgBack, 0, 0, this);
		paintComponents(g);
	}

	private class ButtonListener implements MouseListener{

		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseClicked(MouseEvent e) {
			Object b = e.getSource();
			if		(b == btnStart) {
				parent.changePanel("game");
				btnStart.setForeground(Color.white);
			}
			else if	(b == btnSetting) {
				Setting.setLocationRelativeTo(parent);
				Setting.setVisible(true);
			}
			else if	(b == btnRank) {
				Rank.setLocationRelativeTo(parent);
				Rank.setVisible(true);
			} 			
			else if	(b == btnExit) {System.exit(0);}
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			Object b = e.getSource();
			if		(b == btnStart) {
				getSound();
				btnStart.setForeground(Color.gray);
			}
			else if	(b == btnSetting) {
				getSound();
				btnSetting.setForeground(Color.gray);
			}
			else if	(b == btnRank) {
				getSound();
				btnRank.setForeground(Color.gray);
			} 			
			else if	(b == btnExit) {
				getSound();
				btnExit.setForeground(Color.gray);
			}
		}
		@Override
		public void mouseExited(MouseEvent e) {
			Object b = e.getSource();
			if		(b == btnStart) {btnStart.setForeground(Color.white);}
			else if	(b == btnSetting) {btnSetting.setForeground(Color.white);}
			else if	(b == btnRank) {btnRank.setForeground(Color.white);} 			
			else if	(b == btnExit) {btnExit.setForeground(Color.white);}
		}
	}
}
