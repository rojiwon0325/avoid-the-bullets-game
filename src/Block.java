import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Block {
	private int x, y;
	private int width, height;
	private Image img;
	private int flag;
	public static ArrayList<Block> blockList = new ArrayList<>();
	public Block(int _x, int _y, String _img) {
		x = _x;
		y = _y;
		flag = 0;
		if(_img == "left") {
			img = new ImageIcon("./image/left 100(65)x720.png").getImage();
			width = 65;
			height = 720;
			flag = 1;
		}
		else if(_img == "right") {
			img = new ImageIcon("./image/right 100(65)x720.png").getImage();
			width = 65;
			height = 720;
			flag = 3;
		}
		else if(_img == "up") {
			img = new ImageIcon("./image/up 1080x40(30).png").getImage();
			width = 1080;
			height = 30;
			flag = 4;
		}
		else if(_img == "down") {
			img = new ImageIcon("./image/down 1080x40(30).png").getImage();
			width = 1080;
			height = 30;
			flag = 2;
		}
		else if(_img == "Tree") {
			img = new ImageIcon("./image/Tree 65x93.png").getImage();
			width = 65;
			height = 93;
			flag = 5;
		}
		else if(_img == "rock") {
			img = new ImageIcon("./image/Rock 29x31.png").getImage();
			width = 29;
			height = 31;
			flag = 5;
		}
		else {
			img = null;
			width = 0;
			height = 0;
		}
		blockList.add(this);
	}
	public int getX() {return x;}
	public int getY() {return y;}
	public int getWidth() {return width;}
	public int getHeight() {return height;}
	public Image getImage() {return img;}
	public int getFlag() {return flag;}
	public static void draw(Graphics g, ImageObserver Observer) {
		for(Block B : blockList) {
			if(B.getFlag() == 1)
			{
				g.drawImage(B.getImage(), B.getX(), B.getY(), 100, 720, Observer);
			}
			else if(B.getFlag() == 2)
			{
				g.drawImage(B.getImage(), B.getX(), B.getY()-10, 1080, 40, Observer);
			}
			else if(B.getFlag() == 3)
			{
				g.drawImage(B.getImage(), B.getX()-35, B.getY(), 100, 720, Observer);
			}
			else if(B.getFlag() == 4)
			{
				g.drawImage(B.getImage(), B.getX(), B.getY(), 1080, 40, Observer);
			}
			else if(B.getFlag() == 5)
			{
				g.drawImage(B.getImage(), B.getX(), B.getY(), B.getWidth(), B.getHeight(),Observer);
			}
			else
			{
				g.fillRect(B.getX(), B.getY(), B.getWidth(), B.getHeight());
			}
		}
	}
}
