import java.util.ArrayList;
import java.util.Random;


public class AI {
	
	private char WHITEPAWN = GameRule.WHITEPAWN;
	private char BLACKPAWN = GameRule.BLACKPAWN;
    private Random random = new Random();
	
	public MinimaxScore minimax( char player, GameRule initGameRule, int depth, int limit,int alpha, int beta){
		
		int[] whitePlayer = initGameRule.getWhitePawnLoc().clone();
		int[] blackPlayer = initGameRule.getBlackPawnLoc().clone();
		char[] newBoard = initGameRule.getChessBoard().clone();
		GameRule gameRule = new GameRule(newBoard,whitePlayer,blackPlayer);
		 
		// cek apakah sudah terminate
		if(gameRule.checkWin(BLACKPAWN)){
			MinimaxScore minimaxScore = new MinimaxScore(13-depth);
			return minimaxScore;
		} else if (gameRule.checkWin(WHITEPAWN)){
			MinimaxScore minimaxScore = new MinimaxScore(-13+depth);
			return minimaxScore;
		}
        
        if(depth >= limit) {
            // hitung jumlah black pawn
            int blen = 0;
            for (int i = 0; i < 8; i++) {
                if (gameRule.getBlackPawnLoc()[i] != -1) {
                    blen++;
                }
            }
            // hitung jumlah white pawn
            int wlen = 0;
            for (int i = 0; i < 8; i++) {
                if (gameRule.getWhitePawnLoc()[i] != -1) {
                    wlen++;
                }
            }
            // hitung best location
            int best = gameRule.getBestLoc(player);
            if (player == WHITEPAWN) {
                best = 63-best;
            }
            best = (best/8)-1;
            // assign score
            MinimaxScore minimaxScore = new MinimaxScore(blen-wlen+best);
            
			return minimaxScore;
        }
        
        ArrayList<int[]> possibleMove = new ArrayList<int[]>();												
		// gather all possible moves
		possibleMove = findPossibleMove(player, gameRule);
		
		ArrayList<MinimaxScore> moves = new ArrayList<MinimaxScore>(); 										
		for(int i = 0; i < possibleMove.size(); i++){
			MinimaxScore move = new MinimaxScore();
			int[] whitePlayerr = initGameRule.getWhitePawnLoc().clone();
            int[] blackPlayerr = initGameRule.getBlackPawnLoc().clone();
            char[] newBoardr = initGameRule.getChessBoard().clone();
			GameRule copyOfGameRule = new GameRule(newBoardr,whitePlayerr,blackPlayerr);
			
			move.movement[0] = possibleMove.get(i)[0];
			move.movement[1] = possibleMove.get(i)[1];
            // do the aforementioned move
			gameRule.turn(move.movement , player);
			
            // recursive command
			if(player == BLACKPAWN){
				MinimaxScore result = minimax(WHITEPAWN, gameRule, depth, limit, alpha, beta);
				move.score = result.score;
				if(move.score > beta) {
                    return move; // ubah '>=' menjadi '>' agar bisa random
                }
				alpha = Math.max(alpha, move.score);
			} else {
				MinimaxScore result = minimax(BLACKPAWN, gameRule, depth+1, limit, alpha, beta);
				move.score = result.score;
				if(move.score < alpha) {
                    return move; // ubah '<=' menjadi '<' agar bisa random
                }
				beta = Math.min(beta, move.score);
			}
			gameRule = copyOfGameRule;
			
			moves.add(move);
		}
		int bestMove = -1;
        // find best moves
		if(player == BLACKPAWN){
			int bestScore = Integer.MIN_VALUE;
			for(int i=0; i<moves.size(); i++){
				if(moves.get(i).score > bestScore){
					bestScore = moves.get(i).score;
					bestMove = i;
				}
                /*else if (moves.get(i).score == bestScore){
                    int rand = random.nextInt(8);
                    if (rand == 1) {
                        bestScore = moves.get(i).score;
                        bestMove = i;
                    }
				}*/
			}
		} else {
			int bestScore = Integer.MAX_VALUE;
			for(int i=0; i<moves.size(); i++){
				if(moves.get(i).score < bestScore){
					bestScore = moves.get(i).score;
					bestMove = i;
				}
                /*else if (moves.get(i).score == bestScore){
                    int rand = random.nextInt(8);
                    if (rand == 1) {
                        bestScore = moves.get(i).score;
                        bestMove = i;
                    }
				}*/
			}
		}
		try{
			return moves.get(bestMove);
		} catch(ArrayIndexOutOfBoundsException e){
			MinimaxScore noPossibleMove = new MinimaxScore(-12+depth);
			return noPossibleMove;
		}
		
	}
	
	/**
	 * Menyimpan sebuah array dengan indeks 0 sebagai tujuan dan 1 sebagai asal
	 * @param playersPawn
	 * @return
	 */
	public ArrayList<int[]> findPossibleMove(char player, GameRule gameRule){
		ArrayList<int[]> possibleMove = new ArrayList<int[]>();
		int modifier = 1;
		int[] pawnLocation;
		if(player == WHITEPAWN){
			pawnLocation = gameRule.getWhitePawnLoc();
			modifier = -1;
		} else {
			pawnLocation = gameRule.getBlackPawnLoc();
		}
		
		for(int i=0; i<pawnLocation.length; i++){
			int j=7;
			while(pawnLocation[i]>=0 && pawnLocation[i] < GameRule.BOARD_LENGTH && j<=9){
				if(gameRule.movementRule(player, pawnLocation[i], pawnLocation[i]+j*modifier)){
					int[] movement = {pawnLocation[i]+j*modifier, pawnLocation[i]};
					possibleMove.add(movement);
				}
				j++;
			}
		}
		return possibleMove;
	}
}

class MinimaxScore {
	
	public int[] movement = new int[2];
	public int score;
	
	public MinimaxScore(){
		this.score = 0;
	}
	
	public MinimaxScore(int score){
		this.score = score;
	}
	
	public MinimaxScore(int toIndex, int fromIndex ,int score){
		this.score = score;
		this.movement[0] = toIndex;
		this.movement[1] = fromIndex;
	}
	
}