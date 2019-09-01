import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/**
 * Catatan:
 * hubungkan ke GUI
 * ubah GUI jadi pakai index linear 0 - 63
 * 
 * movement rule tidak dapat dipakai untuk rekusrsi
 * 
 * untuk debugging, diubah : BOARD_LENGTH; blackGoal; whitePawnLoc
 * 
 * @author Muhammad Faisal first of his name from house Rangkuti
 *
 */

public class GameRule {
	public static int BOARD_LENGTH = 64;
	public static int[] whiteGoal = {0,1,2,3,4,5,6,7};
	public static int[] blackGoal = {56,57,58,59,60,61,62,63};
	
	private static int W_MOVE = -8;
	private static int B_MOVE = 8;
	
	private static int W_LEFT_EAT = -9;
	private static int W_RIGHT_EAT = -7;
	private static int B_LEFT_EAT = 7;
	private static int B_RIGHT_EAT = 9;
	
	public static char WHITEPAWN = 'w';
	public static char BLACKPAWN = 'b';
	
	private char[] chessBoard = new char[BOARD_LENGTH];
	private int[] whitePawnLoc = {-1,-1,-1,-1,-1,-1,-1,-1};
	private int[] blackPawnLoc = {-1,-1,-1,-1,-1,-1,-1,-1};
	
	public GameRule() throws ArrayIndexOutOfBoundsException{
		
		
		//inisiasi posisi bidak
		for(int i = 0; i < 8; i++){
			chessBoard[whitePawnLoc[i]] = WHITEPAWN;
			chessBoard[blackPawnLoc[i]] = BLACKPAWN;
		}
		
	}
	
	
	public GameRule(char[] chessBoard ,int[] whitePawnLocation, int[] blackPawnLocation){
		this.chessBoard = chessBoard;
		this.whitePawnLoc = whitePawnLocation;
		this.blackPawnLoc = blackPawnLocation;
		
	}
    
    public GameRule(PapanCatur papan) {
        int k = 0;
        int m = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (papan.getKotak(i,j).isPawned() && papan.getKotak(i,j).getPawn().isBlack()) {
                    blackPawnLoc[k] = (i*8)+j;
                    chessBoard[blackPawnLoc[k]] = BLACKPAWN;
                    k++;
                } else if (papan.getKotak(i,j).isPawned() && !papan.getKotak(i,j).getPawn().isBlack()) {
                    whitePawnLoc[m] = (i*8)+j;
                    chessBoard[whitePawnLoc[m]] = WHITEPAWN;
                    m++;
                }
            }
        }
    }
	
	
	/**
	 * method melakukan giliran
	 * @param pawnMovement menyimpan nilai indeks 0 untuk tujuan dan 1 untuk asal dari bidak
	 * @param player
	 * @return validitas giliran player
	 */
	public boolean turn(int[] pawnMovement, char player){
		
		
		//lakukan movement
		boolean turnValidity = move(player, pawnMovement[1], pawnMovement[0]);
		
		boolean gameOver = checkWin(player);
		if (gameOver){
			//TODO: tampilkan bahwa pemain telah menang
			declareWinner(player);

		}
		return turnValidity;
	}
	
	
	/**
	 * Mengecek apakah sudah menang atau belum
	 * @param player
	 * @param whitePawnLoc
	 * @param blackPawnLoc
	 * @return
	 */
	public boolean checkWin(char player){
		for(int i=0 ; i<8; i++){
			if(player == WHITEPAWN && (whitePawnLoc[i] <= whiteGoal[whiteGoal.length-1] && whitePawnLoc[i] >= whiteGoal[0] )
					|| blackPawnLoc[i] >= blackGoal[0] && blackPawnLoc[i]<= blackGoal[blackGoal.length-1]){
				//System.out.println("a");
				//tampilkan menang di GUI
				declareWinner(player);
				
				return true;
			}
		}
		return false;
	}
    
    public int getBestLoc(char player) {
        int best = -1;
        if (player == WHITEPAWN) {
            best = 100;
        } else {
            best = -2;
        }
        for (int i = 0; i < 8; i++) {
            if (player == WHITEPAWN) {
                //min
                if (whitePawnLoc[i] < best && whitePawnLoc[i] != -1) {
                    best = whitePawnLoc[i];
                }
            } else {
                //max
                if (blackPawnLoc[i] > best) {
                    best = blackPawnLoc[i];
                }
            }
        }
        if (best < 0 || best > 64) {
            best = 0;
        }
        return best;
    }


	/**
	 * Menghapus bidak yang telah dimakan
	 * @param idKotak
	 * @param player
	 * @param board
	 */
	public void pawnEaten(int idKotak, char player) {
		try{
			if(player == WHITEPAWN){
				whitePawnLoc[searchIndex(idKotak, whitePawnLoc)] = -1;
			} else {
				blackPawnLoc[searchIndex(idKotak, blackPawnLoc)] = -1;
			}
			chessBoard[idKotak] = 0;
			
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println(e.getMessage());
		}
		
		
	}
	
	
	/**
	 * Menghapus bidak yang telah dimakan
	 * @param idKotak
	 * @param player
	 * @param board
	 */
	public int[] pawnEaten(int idKotak, int[] pawnLoc) {
		try{
			pawnLoc[searchIndex(idKotak, pawnLoc)] = -1;
			
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println(e.getMessage());
		}
		
		return pawnLoc;
		
	}
	
	
	/**
	 * Melakukan perpindahan bidak. Simpan posisi baru bidak
	 * @param player
	 * @param fromIndex
	 * @param toIndex
	 * @return validitas dari gerakan
	 */
	public boolean move(char player, int fromIndex, int toIndex){
		boolean movementValidity = false;
        
		if(movementValidity = movementRule(player, fromIndex, toIndex)){
			
			char pawn = chessBoard[toIndex];
			if(pawn == WHITEPAWN || pawn == BLACKPAWN){
				pawnEaten(toIndex, pawn);
			}						
			
			if(player == WHITEPAWN){
				whitePawnLoc[searchIndex(fromIndex, whitePawnLoc)] = toIndex;
			} else {
				blackPawnLoc[searchIndex(fromIndex, blackPawnLoc)] = toIndex;
			}
			
			chessBoard[toIndex] = chessBoard[fromIndex];
			chessBoard[fromIndex] = 0;
		}
		return movementValidity;
	}
	
	/**
	 * Mengecek validitas pergerakan
	 * @param player
	 * @param fromIndex
	 * @param toIndex
	 * @return validitas dari gerakan
	 */
	public boolean movementRule(char player, int fromIndex, int toIndex){
        
		if(toIndex >= BOARD_LENGTH || toIndex < 0 || fromIndex < 0|| fromIndex >= BOARD_LENGTH) {
            return false;
        }
        
		int movement = toIndex - fromIndex; 
		
		if(player == WHITEPAWN && player == chessBoard[fromIndex] && movement < 0){
			if(movement == W_MOVE && chessBoard[toIndex] == 0|| (movement == W_LEFT_EAT && fromIndex%8 != 0 || movement == W_RIGHT_EAT 
					&& fromIndex%8 != 7) && chessBoard[toIndex] == BLACKPAWN){
				return true;
			}
			
		} 
        
        else if(player == BLACKPAWN && player == chessBoard[fromIndex] && movement > 0){
            
			if(movement == B_MOVE && chessBoard[toIndex] == 0|| (movement == B_LEFT_EAT && fromIndex%8 != 0 || movement == B_RIGHT_EAT 
					&& fromIndex%8 != 7) && chessBoard[toIndex] == WHITEPAWN){
				return true;
			}
		}
        
		return false;
	}

	
	//TODO: tampilkan di GUI bahwa player adalah pemenang
	/**
	 * Method untuk menampilkan pemenang
	 * @param player
	 */
	public void declareWinner(char player){
		
	}
	
	
	public char[] getChessBoard(){
		return chessBoard;
	}
	
	public void setChessBoard(char[] newChessBoard){
		this.chessBoard = newChessBoard;
	}
	
	public int[] getWhitePawnLoc(){
		return whitePawnLoc;
	}
	
	public void setWhitePawnLocation(int[] newWhitePawnLocation){
		this.whitePawnLoc = newWhitePawnLocation;
	}
	
	public int[] getBlackPawnLoc(){
		return blackPawnLoc;
	}
	
	public void setBlackPawnLocation(int[] newBlackPawnLocation){
		this.blackPawnLoc = newBlackPawnLocation;
	}
	
	/**
	 * Mengiterasi bidak-bidak pemain untuk diambil bidak yang berada diposisi yang diinput
	 * @param idKotak
	 * @param playersPawn
	 * @return indeks bidak yang berada pada posisi di papan. -1 jika tidak ditemukan bidak 
	 * pada posisi tersebut
	 */
	public static int searchIndex(int idKotak ,int[] playersPawn){
		for(int i=0 ; i<playersPawn.length ; i++){
			if(playersPawn[i] == idKotak){
				return i;
			}
		}
		return -1;
	}
	
}