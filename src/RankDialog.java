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
import javax.swing.JLabel;

public class RankDialog extends JDialog{
	private SharedObj obj;
	private Image imgBack;
	private String rank[][];
	
	private JButton btnClose;
	private Font fnt;
	
	private JLabel lblName;
	private JLabel lblScore;
	private JLabel lblranking;
	
	public RankDialog(MainFrame _parent, SharedObj _obj) {
		super(_parent, "Rank", true);
		getContentPane().setBackground(new Color(68,68,68));
		getContentPane().setPreferredSize(new Dimension(400,516));
		setLayout(null);
		setResizable(false);
		setUndecorated(true);
		obj = _obj;
		fnt = new Font("Visitor TT2 BRK", Font.PLAIN, 50);
		imgBack = new ImageIcon("image/rankback 400x516.png").getImage();
		rank = obj.getRank();
		
		btnClose = new JButton("CLOSE");
		btnClose.setFont(fnt);
		btnClose.setBounds(100, 450, 200, 50);
		btnClose.setForeground(Color.white);
		btnClose.addActionListener(new ButtonListener());
		btnClose.setBorderPainted(false);
		btnClose.setContentAreaFilled(false);
		btnClose.setFocusPainted(false);
		add(btnClose);
		
		lblName = new JLabel("<html>" + rank[0][0] + "<br>" + rank[1][0] + "<br>" + rank[2][0] + "<br>" + rank[3][0] + "<br>" + rank[4][0] + "<br>" + rank[5][0] + "<br>" + rank[6][0] + "<br>" + rank[7][0] + "<br>" + rank[8][0] + "<br>" + rank[9][0] + "</html>");
		lblScore = new JLabel("<html>" + rank[0][1] + "<br>" + rank[1][1] + "<br>" + rank[2][1] + "<br>" + rank[3][1] + "<br>" + rank[4][1] + "<br>" + rank[5][1] + "<br>" + rank[6][1] + "<br>" + rank[7][1] + "<br>" + rank[8][1] + "<br>" + rank[9][1] + "</html>");
		lblranking = new JLabel("<html>1.<br>2.<br>3.<br>4.<br>5.<br>6.<br>7.<br>8.<br>9.<br>10.</html>");
		
		lblName.setBounds(100, 90, 120, 360);
		lblScore.setBounds(240, 90, 150, 360);
		lblranking.setBounds(40, 90, 50, 360);
		
		
		lblName.setForeground(Color.white);
		lblScore.setForeground(Color.white);
		lblranking.setForeground(Color.white);
		
		lblName.setFont(fnt);
		lblScore.setFont(fnt);
		lblranking.setFont(fnt);
		
		add(lblName);
		add(lblScore);
		add(lblranking);
		
		pack();
		setVisible(false);
	}
	/**
	html형식을 사용하여 적은 JLabel로 여러줄을 출력할 수 있었다.
	 */
	public void setVisible(boolean bool) {
		lblName.setText("<html>" + rank[0][0] + "<br>" + rank[1][0] + "<br>" + rank[2][0] + "<br>" + rank[3][0] + "<br>" + rank[4][0] + "<br>" + rank[5][0] + "<br>" + rank[6][0] + "<br>" + rank[7][0] + "<br>" + rank[8][0] + "<br>" + rank[9][0] + "</html>");
		lblScore.setText("<html>" + rank[0][1] + "<br>" + rank[1][1] + "<br>" + rank[2][1] + "<br>" + rank[3][1] + "<br>" + rank[4][1] + "<br>" + rank[5][1] + "<br>" + rank[6][1] + "<br>" + rank[7][1] + "<br>" + rank[8][1] + "<br>" + rank[9][1] + "</html>");
		super.setVisible(bool);
	}
	public void paint(Graphics g) {
		g.drawImage(imgBack,  0, 0, this);
		getContentPane().paintComponents(g);
	}
	private class ButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			Object b = e.getSource();					
			if (b == btnClose) {setVisible(false);}
		}
		
	}
}