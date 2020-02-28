package application;

import java.util.Vector;

public class State {
	int[][] boardState = new int[boardSize][boardSize]; //-1 = free slot, 0 = black, 1 = white
	Vector<State> sons = new Vector<State>();
	static int boardSize = 10;
	
	boolean isMax = true; // false = min, true = max
	int depth;   // 0 for root node
	
	int optimalScore;
	int blackScore;
	int whiteScore;
	int rowMove;
	int colMove;

	public State(){
		resetState();	
	}
	
	public State(boolean isNodeMax, int d){
		isMax = isNodeMax;
		depth = d;
	}

	// copy constructor
	public State(State s){
		//board = s.board;
		for(int i = 0; i < State.boardSize; i++){
			for(int j = 0; j < State.boardSize; j++){
				boardState[i][j] = s.boardState[i][j];
			}
		}
		
		isMax = s.isMax;// 0 = min, 1 = max
		depth = s.depth;   // 0 for root node
		
		optimalScore = s.optimalScore;
		blackScore = s.blackScore;
		whiteScore = s.whiteScore;
		rowMove = s.rowMove;
		colMove = s.colMove;

	}
	
	// returns 1 if the move is a legal move for a given player(0=black, 1=white)
	boolean moveIsLegal(int player, int row, int col){
		boolean legalMove = false;
		if(boardState[row][col] != -1){
			return legalMove;
		}
		
		while(!legalMove){
			// check left row
			for(int i = row-1; i>=0; i--){
				if(boardState[i][col] == -1){
					break;
				}
				if(boardState[i][col] == player){
					if(i != row-1){
						legalMove = true;
					}
					break;
				}
			}
			
			// check right row
			for(int i = row+1; i<boardSize; i++){
				if(boardState[i][col] == -1){
					break;
				}
				if(boardState[i][col] == player){
					if(i != row+1){
						legalMove = true;
					}
					break;
				}
			}
			
			// check upper col
			for(int i = col-1; i>=0; i--){
				if(boardState[row][i] == -1){
					break;
				}
				if(boardState[row][i] == player){
					if(i != col-1){
						legalMove = true;
					}
					break;
				}
			}
			
			// check lower col
			for(int i = col+1; i<boardSize; i++){
				if(boardState[row][i] == -1){
					break;
				}
				if(boardState[row][i] == player){
					if(i != col+1){
						legalMove = true;
					}
					break;
				}
			}
			
			// check upper left diagonal
			for(int i = row-1, j = col-1; i>=0 && j>=0; i--, j--){
				if(boardState[i][j] == -1){
					break;
				}
				if(boardState[i][j] == player){
					if(i != row-1 && j != col-1){
						legalMove = true;
					}
					break;
				}
			}
			
			// check upper right diagonal
			for(int i = row-1, j = col+1; i>=0 && j<boardSize; i--, j++){
				if(boardState[i][j] == -1){
					break;
				}
				if(boardState[i][j] == player){
					if(i != row-1 && j != col+1){
						legalMove = true;
					}
					break;
				}
			}
			
			
			// check lower right diagonal
			for(int i = row+1, j = col+1; i<boardSize && j<boardSize; i++, j++){
				if(boardState[i][j] == -1){
					break;
				}
				if(boardState[i][j] == player){
					if(i!= row+1 && j!= col+1){
						legalMove = true;
					}
					break;
				}
			}
			
			// check lower left diagonal
			for(int i = row+1, j = col-1; i<boardSize && j>=0; i++, j--){
				if(boardState[i][j] == -1){
					break;
				}
				if(boardState[i][j] == player){
					if(i != row+1 && j != col-1){
						legalMove = true;
					}
					break;
				}
			}
			break;
		}
		return legalMove;
	}

	

	boolean hasValidMoves(int playerColor){
		boolean validMoves = false;
		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				if(boardState[i][j] == -1){
					validMoves = moveIsLegal(playerColor, i, j);
					if(validMoves){
						return validMoves;
					}
				}
			}
		}
		return validMoves;
	}

	
	// updates the board state according to row and col indices and a player's color
	void updateBoardState(int player, int row, int col){
		boardState[row][col] = player;
		rowMove = row;
		colMove = col;
		
		// left row
		for(int i = row-1; i>=0; i--){
			if(boardState[i][col] == -1){
				break;
			}
			if(boardState[i][col] == player){
				if(i != row-1){
					while(i != row){
						boardState[i][col] = player;
						i++;
					}
				}
				break;
			}
		}
		
		// right row
		for(int i = row+1; i<boardSize; i++){
			if(boardState[i][col] == -1){
				break;
			}
			if(boardState[i][col] == player){
				if(i != row+1){
					while(i != row){
						boardState[i][col] = player;
						i--;
					}
				}
				break;
			}
		}
		
		// upper col
		for(int i = col-1; i>=0; i--){
			if(boardState[row][i] == -1){
				break;
			}
			if(boardState[row][i] == player){
				if(i != col-1){
					while(i != col){
						boardState[row][i] = player;
						i++;
					}
				}
				break;
			}
		}
		
		// lower col
		for(int i = col+1; i<boardSize; i++){
			if(boardState[row][i] == -1){
				break;
			}
			if(boardState[row][i] == player){
				if(i != col+1){
					while(i != col){
						boardState[row][i] = player;
						i--;
					}
				}
				break;
			}
		}
		
		// upper left diagonal
		for(int i = row-1, j = col-1; i>=0 && j>=0; i--, j--){
			if(boardState[i][j] == -1){
				break;
			}
			if(boardState[i][j] == player){
				if(i != row-1 && j != col-1){
					while(i != row && j != col){
						boardState[i][j] = player;
						i++;
						j++;
					}
				}
				break;
			}
		}
		
		// upper right diagonal
		for(int i = row-1, j = col+1; i>=0 && j<boardSize; i--, j++){
			if(boardState[i][j] == -1){
				break;
			}
			if(boardState[i][j] == player){
				if(i != row-1 && j != col+1){
					while(i != row && j != col){
						boardState[i][j] = player;
						i++;
						j--;
					}
				}
				break;
			}
		}
		
		
		// lower right diagonal
		for(int i = row+1, j = col+1; i<boardSize && j<boardSize; i++, j++){
			if(boardState[i][j] == -1){
				break;
			}
			if(boardState[i][j] == player){
				if(i!= row+1 && j!= col+1){
					while(i != row && j != col){
						boardState[i][j] = player;
						i--;
						j--;
					}
				}
				break;
			}
		}
		
		// lower left diagonal
		for(int i = row+1, j = col-1; i<boardSize && j>=0; i++, j--){
			if(boardState[i][j] == -1){
				break;
			}
			if(boardState[i][j] == player){
				if(i != row+1 && j != col-1){
					while(i != row && j != col){
						boardState[i][j] = player;
						i--;
						j++;
					}
				}
				break;
			}
		}
		
		// update board state score for white and black players
		updateScore();
	}

	
	// updates the score for the black and the white players
	void updateScore(){
		int bScore = 0;
		int wScore = 0;
		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				if(boardState[i][j] == 1){
					wScore++;
				}
				else if(boardState[i][j] == 0){
					bScore++;
				}
				
			}
		}
		blackScore = bScore;
		whiteScore = wScore;
	}
	
	
	void resetState(){
		isMax = true;
		sons.clear();
		for(int row=0; row<boardSize; row++){
 			for(int col=0; col<boardSize; col++){
 				boardState[row][col] = -1;
 			}
 		}

 		// empty = -1
 		// black = 0
 		// white = 1
		
		int midRow = boardSize/2;
		int midCol = boardSize/2;
		
		// init board starting mode
 		boardState[midRow-1][midCol] = 1;
 		boardState[midRow][midCol-1] = 1;
 		boardState[midRow-1][midCol-1] = 0;
 		boardState[midRow][midCol] = 0;
 		
 		// initialize score
 		blackScore = 2;
 		whiteScore = 2;
 		
 		optimalScore = -1;
 		
 		rowMove = -1; // -1 for the first time
 		colMove = -1; // -1 for the first time
 		
	}

	
	void copyBoardState(int[][] bState){
		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				boardState[i][j] = bState[i][j];
			}
		}
	}
	
	int strategicBonus(int player){
		int score; // score with strategic bonus
		int otherPlayer = (player+1)%2;
		
		// get current board score of player
		if(player == 0){ // black player
			score = blackScore;
		}
		else{
			score = whiteScore;
		}
		
		
		if(score == 0){ // if the player has no players
			score = -999; 
		}
		
		// bonus if player has a corner, penalty if other player has a corner
		if(boardState[0][0] == player){
			score +=10;
		}
		else if(boardState[0][0] == otherPlayer){
			score -=10;
		}
		if(boardState[0][boardSize-1] == player){
			score +=10;
		}
		
		else if(boardState[0][boardSize-1] == otherPlayer){
			score -=10;
		}
		
		if(boardState[boardSize-1][0] == player){
			score +=10;
		}
		else if(boardState[boardSize-1][0] == otherPlayer){
			score -=10;
		}
		
		if(boardState[boardSize-1][boardSize-1] == player){
			score +=10;
		}
		else if(boardState[boardSize-1][boardSize-1] == otherPlayer){
			score -=10;
		}

		// at the beginning of the game bonus is for corners only;
		if(blackScore+whiteScore > (boardSize*boardSize)/ 6){
			score += strategicRowEdgeBonus(player, otherPlayer, 0);
			score += strategicRowEdgeBonus(player, otherPlayer, boardSize-1);
			score += strategicColEdgeBonus(player, otherPlayer, 0);
			score += strategicColEdgeBonus(player, otherPlayer, boardSize-1);
		}
		
		
		return score;
	}


	int strategicRowEdgeBonus(int player, int otherPlayer, int EdgeRow){
		//edge state flags. if all false - larger and smaller indices are empty
		boolean smallerIsPlayer = false; // indicates if in smaller index there's a player's tile
		boolean largerIsPlayer = false;  // indicates if in larger index there's a player's tile
		boolean smallerIsOtherPlayer = false;  // indicates if in smaller index there's the other player's tile
		boolean largerIsOtherPlayer = false; // indicates if in smaller index there's the other player's tile

		int score = 0;
		
		
		int i = 1;
		while(i<boardSize-1){
			if(boardState[EdgeRow][i] == player){
				// setting edge state flags to false
				smallerIsPlayer = false;
				largerIsPlayer = false;
				smallerIsOtherPlayer = false;
				largerIsOtherPlayer = false;
				
				
				int j = i-1;
				while(j>0 && boardState[EdgeRow][j] == player){
					j--;
				}
				if(boardState[EdgeRow][j] == player){
					smallerIsPlayer = true;
				}
				else if(boardState[EdgeRow][j] == otherPlayer){
					smallerIsOtherPlayer = true;
				}
				
				j = i+1;
				// check right tiles of the upper edge
				while(j<boardSize-1 && boardState[EdgeRow][j] == player){
					j++;
				}
				if(boardState[EdgeRow][j] == player){
					largerIsPlayer = true;
				}
				else if(boardState[EdgeRow][j] == otherPlayer){
					largerIsOtherPlayer = true;
				}
				
				
				//calculate bonus
				if(smallerIsOtherPlayer && largerIsOtherPlayer){
						// medium bonus - the player is between two rival tiles
						score += 2;
				}
				else if((smallerIsOtherPlayer && largerIsPlayer) || (smallerIsPlayer && largerIsOtherPlayer) ){
						// small bonus - edge catching 
						score += 1;
				}
				else if((smallerIsPlayer && largerIsPlayer) ||
						(smallerIsPlayer && !largerIsOtherPlayer) ||
						(!smallerIsOtherPlayer && largerIsPlayer)){
					// bonus - edge catching with possible attack options
					score += 6;		
				}
				else if((smallerIsOtherPlayer && !largerIsPlayer && !largerIsOtherPlayer) ||
						(!smallerIsOtherPlayer && !smallerIsPlayer && largerIsOtherPlayer)){
					if(isMax){
						//bonus - edge open to eat opponent
						score +=2; 
					}
					else{
						//penalty - edge open opponent to eat
						score -=4; 
					}	
				}
				i = j;
			}	
			i++;
		}
		return score;
	}
	
	int strategicColEdgeBonus(int player, int otherPlayer, int EdgeCol){
		//edge state flags. if all false - larger and smaller indices are empty
		boolean smallerIsPlayer = false; // indicates if in smaller index there's a player's tile
		boolean largerIsPlayer = false;  // indicates if in larger index there's a player's tile
		boolean smallerIsOtherPlayer = false;  // indicates if in smaller index there's the other player's tile
		boolean largerIsOtherPlayer = false; // indicates if in smaller index there's the other player's tile
		
		int score = 0;
		
		
		int i = 1;
		while(i<boardSize-1){
			if(boardState[i][EdgeCol] == player){
				// setting edge state flags to false
				smallerIsPlayer = false;
				largerIsPlayer = false;
				smallerIsOtherPlayer = false;
				largerIsOtherPlayer = false;
				
				
				int j = i-1;
				while(j>0 && boardState[j][EdgeCol] == player){
					j--;
				}
				if(boardState[j][EdgeCol] == player){
					smallerIsPlayer = true;
				}
				else if(boardState[j][EdgeCol] == otherPlayer){
					smallerIsOtherPlayer = true;
				}
				
				j = i+1;
				// check right tiles of the upper edge
				while(j<boardSize-1 && boardState[j][EdgeCol] == player){
					j++;
				}
				if(boardState[j][EdgeCol] == player){
					largerIsPlayer = true;
				}
				else if(boardState[j][EdgeCol] == otherPlayer){
					largerIsOtherPlayer = true;
				}
				
				
				//calculate bonus
				if(smallerIsOtherPlayer && largerIsOtherPlayer){
						// medium bonus - the player is between two rival tiles
						score += 2;
				}
				else if((smallerIsOtherPlayer && largerIsPlayer) || (smallerIsPlayer && largerIsOtherPlayer) ){
						// small bonus - edge catching 
						score += 1;
				}
				else if((smallerIsPlayer && largerIsPlayer) ||
						(smallerIsPlayer && !largerIsOtherPlayer) ||
						(!smallerIsOtherPlayer && largerIsPlayer)){
					// bonus - edge catching with possible attack options
					score += 6;		
				}
				else if((smallerIsOtherPlayer && !largerIsPlayer && !largerIsOtherPlayer) ||
						(!smallerIsOtherPlayer && !smallerIsPlayer && largerIsOtherPlayer)){
					if(isMax){
						//bonus - edge open to eat opponent
						score +=2; 
					}
					else{
						//penalty - edge open opponent to eat
						score -=4; 
					}	
				}
				i = j;
			}	
			i++;
		}
		return score;
	}
		

}
