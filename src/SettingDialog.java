import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JSlider;


public class SettingDialog extends JDialog{
	private SharedObj obj;
	private MainFrame parent;
	private Image imgBack;
	private Font fnt;
	
	private JSlider sldSpeed;
	private JSlider sldBGMVol;
	private JSlider sldEffectVol;
	
	private JButton btnSave;
	private JButton btnDefault;
	private JButton btnCancel;
	private ButtonListener buttonL;
	
	private int bgmVol;
	private int speed;
	private int effectVol;
	
	public SettingDialog(MainFrame _parent, SharedObj _obj) {
		super(_parent, "Setting", true);
		getContentPane().setBackground(new Color(68,68,68));
		getContentPane().setPreferredSize(new Dimension(445,325));
		setLayout(null);
		setResizable(false);
		setUndecorated(true);
		
		parent = _parent;
		obj = _obj;
		fnt = new Font("Visitor TT2 BRK", Font.PLAIN, 30);
		imgBack = new ImageIcon("image/settingback 445x325.png").getImage();
		buttonL = new ButtonListener();
		bgmVol = obj.getBGMVol();
		speed = obj.getBulletSpeed();
		effectVol = obj.geteffectVol();
		
		btnSave = new JButton("SAVE");
		btnSave.setFont(fnt);
		btnSave.setBounds(20, 250, 120, 50);
		btnSave.setForeground(Color.white);
		btnSave.addActionListener(buttonL);
		btnSave.setBorderPainted(false);
		btnSave.setContentAreaFilled(false);
		btnSave.setFocusPainted(false);
		add(btnSave);
		
		btnDefault = new JButton("DEFAULT");
		btnDefault.setFont(fnt);
		btnDefault.setBounds(140, 250, 150, 50);
		btnDefault.setForeground(Color.white);
		btnDefault.addActionListener(buttonL);
		btnDefault.setBorderPainted(false);
		btnDefault.setContentAreaFilled(false);
		btnDefault.setFocusPainted(false);
		add(btnDefault);
		
		btnCancel = new JButton("CANCEL");
		btnCancel.setFont(fnt);
		btnCancel.setBounds(290, 250, 130, 50);
		btnCancel.setForeground(Color.white);
		btnCancel.addActionListener(buttonL);
		btnCancel.setBorderPainted(false);
		btnCancel.setContentAreaFilled(false);
		btnCancel.setFocusPainted(false);
		add(btnCancel);
		
		sldSpeed = new JSlider(10,20,speed);
		sldBGMVol = new JSlider(0,76,bgmVol);
		sldEffectVol = new JSlider(0,76,effectVol);
		
		sldSpeed.setBounds(230,95,170,15);
		sldBGMVol.setBounds(230,150,170,15);
		sldEffectVol.setBounds(230,205,170,15);

		sldSpeed.setBackground(new Color(68,68,68));
		sldBGMVol.setBackground(new Color(68,68,68));
		sldEffectVol.setBackground(new Color(68,68,68));
		
		add(sldSpeed);
		add(sldBGMVol);
		add(sldEffectVol);
		
		pack();
		setVisible(false);
	}
	public void paint(Graphics g) {
		g.drawImage(imgBack,  0, 0, this);
		getContentPane().paintComponents(g);
	}
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Object b = e.getSource();					
			if (b == btnSave)
			{
				speed = sldSpeed.getValue();
				bgmVol = sldBGMVol.getValue();
				effectVol = sldEffectVol.getValue();
				obj.setBulletSpeed(speed);
				obj.setBGMVol(bgmVol);
				obj.seteffectVol(effectVol);
				parent.updateVol();
				setVisible(false);
			}
			else if(b == btnDefault)
			{
				sldSpeed.setValue(15);
				sldBGMVol.setValue(40);
				sldEffectVol.setValue(40);
				speed = sldSpeed.getValue();
				bgmVol = sldBGMVol.getValue();
				effectVol = sldEffectVol.getValue();
			}
			else if(b == btnCancel)
			{
				sldSpeed.setValue(speed);
				sldBGMVol.setValue(bgmVol);
				sldEffectVol.setValue(effectVol);
				setVisible(false);
			}
		}
		
	}
}
