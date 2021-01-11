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
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


public class GameOverDialog extends JDialog{
	
	private MainFrame parent;
	private SharedObj obj;
	private Font fnt;
	private Image imgBack;
	
	private JLabel lblscore;
	private JLabel lblranking;
	private JLabel lblname;
	
	private JTextField txtName;
	private JButton btnOK;
	
	private int ranking;
	public GameOverDialog(MainFrame _parent, SharedObj _obj) {
		super(_parent, "GAME OVER", true);
		getContentPane().setBackground(new Color(68,68,68));
		getContentPane().setPreferredSize(new Dimension(445,325));
		setLayout(null);
		setResizable(false);
		setUndecorated(true);
		
		parent = _parent;
		obj = _obj;
		fnt = new Font("Visitor TT2 BRK", Font.PLAIN, 40);
		imgBack = new ImageIcon("./image/gameoverback 445x325.png").getImage();
		
		lblscore = new JLabel("YOUR SCORE: 0");
		lblscore.setFont(fnt);
		lblscore.setBounds(25, 70, 400, 50);
		lblscore.setForeground(Color.white);
		getContentPane().add(lblscore);
		
		lblranking = new JLabel("YOUR RANKING: UNRANKED");
		lblranking.setFont(fnt);
		lblranking.setBounds(25,120,410,50);
		lblranking.setForeground(Color.white);
		getContentPane().add(lblranking);
		
		lblname = new JLabel("YOUR NAME: ");
		lblname.setFont(fnt);
		lblname.setBounds(25,170,200,50);
		lblname.setForeground(Color.white);
		getContentPane().add(lblname);
		
		txtName = new JTextField();
		txtName.setDocument(new JTextFieldLimit(5));
		txtName.setBackground(new Color(68,68,68));
		txtName.setForeground(Color.white);
		txtName.setBorder(null);
		txtName.setBounds(215,170,120,50);
		txtName.setFont(fnt);
		txtName.setCaretColor(Color.white);
		add(txtName);
		
		btnOK = new JButton("OK");
		btnOK.setFont(new Font("Visitor TT2 BRK", Font.PLAIN, 50));
		btnOK.setBounds(172, 250, 100, 50);
		btnOK.setForeground(Color.white);
		btnOK.addActionListener(new ButtonListener());
		btnOK.setBorderPainted(false);
		btnOK.setContentAreaFilled(false);
		btnOK.setFocusPainted(false);
		add(btnOK);
		
		pack();
		setVisible(false);
	}
	public void paint(Graphics g) {
		g.drawImage(imgBack, 0, 0, this);
		getContentPane().paintComponents(g);
	}
	/**
	화면에 띄우기 전에 scorePanel에 기록된 스코어 기록과 등수정보를 불러온다.
	순위권인 경우, 텍스트필드가 활성화되어 이름을 입력할 수 있다.
	 * @param score
	 */
	public void setVisible(double score) {
		txtName.setText("");
		lblscore.setText("YOUR SCORE: " + String.format("%.2f", score));
		if(score > obj.getTopTen()) {
			lblname.setVisible(true);
			txtName.setEnabled(true);
			ranking = obj.rankSort("none", score);
			lblranking.setText("YOUR RANKING: " + String.valueOf(ranking));
		}
		else {
			ranking = 11;
			lblname.setVisible(false);
			txtName.setEnabled(false);
			lblranking.setText("YOUR RANKING: UNRANKED");
		}
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	/**
	텍스트필드에 l개의 문자만 입력받도록 처리한다.
	 */
	private class JTextFieldLimit extends PlainDocument{
		private int limit;
		public JTextFieldLimit(int l) {
				limit = l;
		}
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException{
			if(str == null) {return ;}
			if(getLength() + str.length() <= limit) {
				super.insertString(offs, str, a);
			}
		}
	}
	private class ButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			Object b = e.getSource();
			String name = txtName.getText();
			if(name.isEmpty()) {name = "none";}
			if(b == btnOK) {
				obj.setName(name, ranking);
				obj.uploadRank();
				setVisible(false);
				parent.changePanel("menu");
			}
			
		}
		
	}
}
