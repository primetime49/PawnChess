public class GameMaster {
	public GameRule gameRule;
	char WHITEPAWN = GameRule.WHITEPAWN;
	char BLACKPAWN = GameRule.BLACKPAWN;
	AI ai;
	
	public GameMaster(){
		gameRule = new GameRule();
		ai = new AI();

	}
	
	public void turnClick(int toIndex, int fromIndex){
		int[] movement = {toIndex, fromIndex};
		if(toIndex != fromIndex && gameRule.turn(movement, WHITEPAWN) && !gameRule.checkWin(WHITEPAWN)){
			gameRule.turn(bestMovement(), BLACKPAWN);
		}
	}
		
	public int[] bestMovement(){
		MinimaxScore findMovement = ai.minimax(BLACKPAWN, gameRule, 0 ,3, Integer.MIN_VALUE, Integer.MAX_VALUE);
		System.out.println(findMovement.movement[0]);
		System.out.println(findMovement.movement[1]);
		showChessBoard();
		
		return findMovement.movement;
	}
	
	public void showChessBoard(){
		for(int i=1; i<=gameRule.getChessBoard().length; i++){
			System.out.print(gameRule.getChessBoard()[i-1]);
			if(i%8 == 0) System.out.println();
		}
	}
}