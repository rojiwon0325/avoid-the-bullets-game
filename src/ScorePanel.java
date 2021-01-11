import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ScorePanel extends JPanel{
	private JLabel lblTitle;
	private JLabel lblScore;
	private JLabel lblInRank;
	private double topten;
	private Timer timer;
	private double score;
	private Font fnt;
	
	public ScorePanel() {
		setBounds(1080, 0, 300, 720);
		setLayout(new GridLayout(4,1));
		setBackground(Color.black);
		fnt = new Font("Visitor TT1 BRK", Font.BOLD, 50);

		lblTitle = new JLabel("MY SCORE", SwingConstants.CENTER);
		lblTitle.setFont(fnt);
		lblTitle.setForeground(Color.white);
		
		
		lblScore = new JLabel("0", SwingConstants.CENTER);
		lblScore.setFont(fnt);
		lblScore.setForeground(Color.white);
		
		lblInRank = new JLabel("In Rank!", SwingConstants.CENTER);
		lblInRank.setFont(fnt);
		lblInRank.setForeground(Color.RED);
		
		add(lblTitle);
		add(lblScore);
		add(lblInRank);
		initScore();
	}
	/**
	게임 시작 직전 스코어를 초기상태로 설정한다.
	 */
	public void initScore() {
		score = 0;
		lblScore.setText(String.format("%.2f", score));
		lblInRank.setVisible(false);
		repaint();
	}
	public void start(double top) {
		topten = top;
		timer = new Timer();
		timer.scheduleAtFixedRate(new Task(), 0, 10);
	}
	public double stop() {
		if(timer != null) {
			timer.cancel();
		}
		return score;
	}
	public double getScore() {return score;}
	
	private class Task extends TimerTask{

			@Override
		public void run() {
			score += 0.01;
			lblScore.setText(String.format("%.2f", score));
			if(topten < score) {lblInRank.setVisible(true);}
			
		}

	}

}
