import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class ChessPlay {
	
	public static void main(String[] args) throws InterruptedException {
		Scanner sc = new Scanner(System.in);
		
		LetsPlay lp = new LetsPlay();
		lp.showChessBoard();
		
		while(true){
			System.out.print("from Index: ");
			int fromIndex = sc.nextInt();
			System.out.println();
			System.out.print("to Index: ");
			int toIndex = sc.nextInt();
			System.out.println();	
			lp.getChessGame().turnClick(toIndex,fromIndex );
			lp.showChessBoard();
		}
	}
}

class LetsPlay{
	
	public GameMaster playChess = new GameMaster();
	
	public void showChessBoard(){
		for(int i=1; i<=playChess.gameRule.getChessBoard().length; i++){
			System.out.print(playChess.gameRule.getChessBoard()[i-1]);
			if(i%8 == 0) System.out.println();
		}
	}
	
	public GameMaster getChessGame(){
		return playChess;
	}
}
