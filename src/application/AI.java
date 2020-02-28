package application;

public class AI {
	
	int currPlayer;
	int blackPlayer = 0;
	int maxDepth;

	
	/******************************************************************/
	/**                        min-max Tactics                       **/
	/******************************************************************/
	Action getMinMaxAction(State currState, int mDepth, int currP){
		currPlayer = currP;
		maxDepth = mDepth;
		currState.isMax = true;
		currState.depth = 0;
		Action action = new Action(-1,-1);
		int bestScore = activateMinMax(currState);
		for(int i=0; i<currState.sons.size(); i++){
			if(currState.sons.get(i).optimalScore == bestScore){
				action.rowAction = currState.sons.get(i).rowMove;
				action.colAction = currState.sons.get(i).colMove;
				currState.sons.clear();
				return action;
			}
		}
		currState.sons.clear();
		return action;
		
	}
	
	int activateMinMax(State currState){
		int score;
		if(currState.isMax){
			score = -999;
		}
		else{ // current state is min
			score = 999;
		}
		
		if((currState.depth == maxDepth) || !(currState.hasValidMoves(currPlayer))){
			currState.updateScore();
			if(currPlayer == blackPlayer){
				score = currState.blackScore;
			}
			else{  // white player
				score = currState.whiteScore;
			}
			currState.optimalScore = currState.strategicBonus(currPlayer);
			//return score;
			return currState.optimalScore;
		}
		else{ // player has valid moves
			for(int i = 0; i<State.boardSize; i++){
				for(int j = 0; j<State.boardSize; j++){
					if(currState.boardState[i][j] == -1 && currState.moveIsLegal(currPlayer, i, j)){
						State newState = new State();
						newState.copyBoardState(currState.boardState);
						newState.updateBoardState(currPlayer, i, j);
						newState.isMax = !(currState.isMax);
						newState.depth = currState.depth + 1;
						newState.rowMove = i;
						newState.colMove = j;
						currState.sons.add(newState);
						if(currState.isMax){
							score = Math.max(score, activateMinMax(newState));
						}
						else{ // current state is min
							score = Math.min(score, activateMinMax(newState));;
						}
					}
				}
			}
			currState.optimalScore = score;
		}
		if(currState.depth > 0){
			currState.sons.clear();
		}
		return score;
	}
	
	/******************************************************************/
	/**                     alpha-beta Tactics                       **/
	/******************************************************************/
	
	
	Action getAlphaBetaAction(State currState, int mDepth, int currP){
		currPlayer = currP;
		maxDepth = mDepth;
		currState.isMax = true;
		currState.depth = 0;
		int alpha = -999;
		int beta = 999;
		
		
		Action action = new Action(-1,-1);
		int bestScore = activateAlphaBeta(currState, alpha, beta);
		for(int i=0; i<currState.sons.size(); i++){
			if(currState.sons.get(i).optimalScore == bestScore){
				action.rowAction = currState.sons.get(i).rowMove;
				action.colAction = currState.sons.get(i).colMove;
				currState.sons.clear();
				return action;
			}
		}
		currState.sons.clear();
		return action;
		
	}

	
	int activateAlphaBeta(State currState, int alpha, int beta){
		int score;
		
		if(currState.isMax){
			score = -999;
		}
		else{ // current state is min
			score = 999;
		}
		
		if((currState.depth == maxDepth) || !(currState.hasValidMoves(currPlayer))){
			currState.updateScore();
			if(currPlayer == blackPlayer){
				score = currState.blackScore;
			}
			else{  // white player
				score = currState.whiteScore;
			}
			currState.optimalScore = currState.strategicBonus(currPlayer);
			//return score;
			return currState.optimalScore;
		}
		else{ // player has valid moves
			for(int i = 0; i<State.boardSize; i++){
				for(int j = 0; j<State.boardSize; j++){
					if(currState.boardState[i][j] == -1 && currState.moveIsLegal(currPlayer, i, j)){
						State newState = new State();
						newState.copyBoardState(currState.boardState);
						newState.updateBoardState(currPlayer, i, j);
						newState.isMax = !(currState.isMax);
						newState.depth = currState.depth + 1;
						newState.rowMove = i;
						newState.colMove = j;
						currState.sons.add(newState);
						
						if(currState.isMax){
							score = Math.max(score, activateAlphaBeta(newState, alpha, beta));
							if(score >= beta){
								return score;
							}
							alpha = Math.max(score, alpha);
						}
						else{ // current state is min
							score = Math.min(score, activateAlphaBeta(newState, alpha, beta));
							if(score <= alpha){
								return score;
							}
							beta = Math.min(score, beta);
						}
					}
				}
			}
			currState.optimalScore = score;
		}
		
		if(currState.depth > 0){
			currState.sons.clear();
		}
		return score;
	}
}
	