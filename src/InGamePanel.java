import java.awt.Dimension;
import javax.swing.JPanel;

public class InGamePanel extends JPanel implements Runnable{
	private MainFrame parent;
	private SharedObj obj;
	private GameFieldPanel fieldP;
	private ScorePanel scoreP;
	private GameOverDialog endD;
	
	public InGamePanel(MainFrame _parent, SharedObj _obj){
		setPreferredSize(new Dimension(1080+300,720));
		setLayout(null);
		parent = _parent;
		obj = _obj;
		endD = new GameOverDialog(parent, obj);
		fieldP = new GameFieldPanel(obj);
		scoreP = new ScorePanel();
		add(fieldP);
		add(scoreP);

	}
	public void start() {
		new Thread(this).start();
	}
	/**
	실제 게임은 프로그램 시작시 실행되는 스레드가 아닌 다른 메소드에서 진행된다.
	그 스레드가 사용할 정보는 모두 InGamePanel에 존재하므로 해당 클래스에서 정의하도록 한다.
	 */
	@Override
	public void run() {
		fieldP.setFocusable(false);
		fieldP.initField();
		scoreP.initScore();
		fieldP.countDown(5);
		fieldP.setFocusable(true);
		fieldP.requestFocus();
		scoreP.start(obj.getTopTen());
		fieldP.gameLogic();
		endD.setVisible(scoreP.stop());
	}

}
