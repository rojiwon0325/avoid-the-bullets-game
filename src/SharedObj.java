import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SharedObj {
	public boolean flag;
	
	private int bulletSpeed;
	private int bgmVol;
	private int effectVol;
	private String rank[][];
	
	private File fscorelist;
	private File fnamelist;
	
	public SharedObj() {
		flag = false;
		bgmVol = 40;
		effectVol = 40;
		bulletSpeed = 15;
		fscorelist = new File("./database/scorelist.txt");
		fnamelist = new File("./database/namelist.txt");
		initRank();
	}
	/**
	 파일에 접근하여 rank를 초기화한다. 만약 파일이 없는 경우, rank리스트를 기본값으로 설정한다.
	 */
	public void initRank() {
		int i = 0;
		FileReader frscore;
		FileReader frname;
		BufferedReader brscore;
		BufferedReader brname;
		rank = new String[11][2];
		try {
			frscore = new FileReader(fscorelist);
			frname = new FileReader(fnamelist);
			brscore = new BufferedReader(frscore);
			brname = new BufferedReader(frname);
			while(i < 10) {
				rank[i][0] = brname.readLine();
				rank[i][1] = brscore.readLine();
				i++;
			}
			rank[10][0] = "none";
			rank[10][1] = "0";
			brscore.close();
			brname.close();
		} catch (NullPointerException | IOException e) {
			while(i < 10) {
				rank[i][0] = "none";
				rank[i][1] = "0";
				i++;
			}
			rank[10][0] = "none";
			rank[10][1] = "0";
			return;
		}

		return;
	}
	/**
	파일에 rank정보를 업로드한다. 파일이 없으면 생성한다.
	 */
	public void uploadRank() {
		FileWriter fwscore;
		FileWriter fwname;
		BufferedWriter bwscore;
		BufferedWriter bwname;
		try {
			fnamelist.createNewFile();
			fscorelist.createNewFile();
			fwname = new FileWriter(fnamelist);
			fwscore = new FileWriter(fscorelist);
			bwname = new BufferedWriter(fwname);
			bwscore = new BufferedWriter(fwscore);
			bwname.flush();
			bwscore.flush();
			for(int i = 0; i < 10 ; i++)
			{
				bwname.append(rank[i][0]+"\n");
				bwscore.append(rank[i][1]+"\n");
			}
			bwscore.close();
			bwname.close();
		} catch (IOException e) {}
		return;
	}
	
	public String[][] getRank(){return rank;}
	public double getTopTen() {return Double.parseDouble(rank[9][1]);}
	public void setName(String name, int _rank) {
		rank[_rank-1][0] = name;
	}
	/**
	rank리스트에 대해 삽입정렬을 진행한다.
	 * @param name
	 * @param score
	 * @return
	 */
	public int rankSort(String name, double score) {
		int i;
		for(i = 10 ; i > 0 ; i--) {
			if(Double.parseDouble(rank[i-1][1]) < score)
			{
				rank[i][0] = rank[i-1][0];
				rank[i][1] = rank[i-1][1];
			}
			else {break;}
		}
		rank[i][0] = name;
		rank[i][1] = String.format("%.2f", score);
		return i+1;
	}
	public int getBulletSpeed() {return bulletSpeed;}
	public void setBulletSpeed(int speed) {bulletSpeed = speed;}

	public int getBGMVol() {return bgmVol;}
	public void setBGMVol(int vol) {bgmVol = vol;}

	public int geteffectVol() {return effectVol;}
	public void seteffectVol(int vol) {effectVol = vol;}
	
}
