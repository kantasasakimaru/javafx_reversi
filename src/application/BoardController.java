package application;

import java.net.URL;
import java.util.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.management.StandardEmitterMBean;

public class BoardController implements Initializable{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="GridP"
    private GridPane gridP; // Value injected by FXMLLoader

    @FXML // fx:id="msgBox"
    private TextArea msgBox; // Value injected by FXMLLoader

    @FXML // fx:id="BlackPlayerType"
    private TextField BlackPlayerType; // Value injected by FXMLLoader

    @FXML // fx:id="WhitePlayerType"
    private TextField WhitePlayerType; // Value injected by FXMLLoader

    @FXML // fx:id="BlackPlayerDepth"
    private TextField BlackPlayerDepth; // Value injected by FXMLLoader

    @FXML // fx:id="blackScore"
    private Text blackScore; // Value injected by FXMLLoader

    @FXML // fx:id="whiteScore"
    private Text whiteScore; // Value injected by FXMLLoader

    @FXML
    private Spinner<Integer> blackDepth;

    @FXML
    private Spinner<Integer> whiteDepth;

    @FXML
    private ComboBox<String> blackType;

    @FXML
    private ComboBox<String> whiteType;

    @FXML
    private Label blackLabel;

    @FXML
    private Label whiteLabel;

    @FXML
    private CheckBox playContinuously;

    boolean gameFinished;
    
    int boardSize = 10;
    Circle[][] circBoard = new Circle[10][10];
    State currState; // the current board state;
	State previousOneStepAgoState;
    Paint emptySlotPaint = Paint.valueOf("#005826");
    int currPlayer; // black = 0 white = 1
    int blackPlayer = 0;
    String msgStr = "";
    AI computerMoves = new AI(); // black player first
    Timeline gameTimer; 
    ComboBox<String>[] playerType = new ComboBox[2];
    Spinner<Integer>[] playerDepth = new Spinner[2];
    private Deque<State> deque = new ArrayDeque<>();
    BoardState boardState = new BoardState();
	int depth;
	int maxDepth;

//    public BoardController(BoardState state, State currState) {
//    	this.state = state;
//    	this.currState = currState;
//	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		playerType[0] = blackType; //black player/min-max/alpha-beta
		playerType[1] = whiteType; //white player/min-max/alpha-beta
		playerDepth[0] = blackDepth;
		playerDepth[1] = whiteDepth;

		//initialize game timer for continuous game
		gameTimer = new Timeline(new KeyFrame(
		        Duration.millis(1000),
		        ae -> checkAndPlay()));
		gameTimer.setCycleCount(Animation.INDEFINITE);
		gameTimer.play();

		currState = new State();
		for(int i=0; i<boardSize; i++){
    		for(int j=0; j<boardSize; j++){
    			Circle circ = new Circle(18.0, emptySlotPaint);
    			circBoard[i][j] = circ;
    			circBoard[i][j].addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
		        {
		            @Override
		            public void handle(MouseEvent event) {

						previousOneStepAgoState = activateMinMax(currState);

//		            	boardState.setState(currState);

						deque.push(previousOneStepAgoState);
		            	//TODO: something

		            	Node source = (Node)event.getSource() ;
		            	Integer rowIndex = GridPane.getRowIndex(source);
		            	Integer colIndex = GridPane.getColumnIndex(source);
		            	if(currPlayer == blackPlayer){
		        			if(!(blackType.getValue().equalsIgnoreCase("player"))){
		        			   msgStr += "Computer's turn. please click on 'Play AI move'\n";
		        			   msgBox.setText(msgStr);
		        			   return;
		        			}
		        		 }
		        		else{
		        			if(!(whiteType.getValue().equalsIgnoreCase("player"))){
		        				   msgStr += "Computer's turn. please click on 'Play AI move'\n";
		        				   msgBox.setText(msgStr);
		        				   return;
		        				}
		        		}
		            	playTurn(rowIndex, colIndex);
		            }
		        });

				gridP.add(circBoard[i][j], j, i);
    			GridPane.setConstraints(circBoard[i][j], j, i, 1, 1, HPos.CENTER, VPos.CENTER);
    		}
    	}
		initNewGame();
	}

	State activateMinMax(State currState){
		State newState = new State();

		// player has valid moves
			for(int i = 0; i<State.boardSize; i++){
				for(int j = 0; j<State.boardSize; j++){

						newState.copyBoardState(currState.boardState);
						newState.isMax = !(currState.isMax);
						newState.depth = currState.depth + 1;
						newState.rowMove = i;
						newState.colMove = j;
				}
			}
		return newState;

	}


	void checkAndPlay(){
		// play AI computer move if continuous game is selected and the current 
		// player is a computer
		if(playContinuously.isSelected()){
			if(!playerType[currPlayer].getValue().equalsIgnoreCase("player")){
				playAiHandler(null);
			}
		}
	}
	
	public void playTurn(int row, int col){

    	if(gameFinished){
    		return;
    	}
    	int nextPlayer = (currPlayer + 1)%2;
    	//check if move is legal
		if(currState.moveIsLegal(currPlayer, row, col)){
			//update board state and GUI
			currState.updateBoardState(currPlayer, row, col);			
			updateBoardGui();
			
			// If the next player has legal moves - switch turns;
			if(nextPlayerHasMoves(nextPlayer)){
				
				if(currPlayer == blackPlayer){
					blackLabel.setFont(Font.font("system", FontWeight.NORMAL, 12));
		    		whiteLabel.setFont(Font.font("system", FontWeight.BOLD, 12));
		    	}
		    	else{
		    		whiteLabel.setFont(Font.font("system", FontWeight.NORMAL, 12));
		    		blackLabel.setFont(Font.font("system", FontWeight.BOLD, 12));
		    	}
				// change Player's turn
				currPlayer = nextPlayer;
			};
		}
		else{
			msgStr += "illegal move, try again \n";
			msgBox.setText(msgStr);
		}
	}
	
	public void updateBoardGui(){

		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				if(currState.boardState[i][j] == 0){ // black piece
					circBoard[i][j].setFill(Color.BLACK);
				}
				else if(currState.boardState[i][j] == 1){ // white piece
					circBoard[i][j].setFill(Color.WHITE);
				}
				else{ // empty slot
					circBoard[i][j].setFill(emptySlotPaint);
				}
			}
		}

		blackScore.setText(Integer.toString(currState.blackScore));
		whiteScore.setText(Integer.toString(currState.whiteScore));
	}

	@FXML
	void GoBackOneStep() {

		currState =  deque.pop();

		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				if(currState.boardState[i][j] == 0){ // black piece
					circBoard[i][j].setFill(Color.BLACK);
				}
				else if(currState.boardState[i][j] == 1){ // white piece
					circBoard[i][j].setFill(Color.WHITE);
				}
				else{ // empty slot
					circBoard[i][j].setFill(emptySlotPaint);
				}
			}
		}

		blackScore.setText(Integer.toString(currState.blackScore));
		whiteScore.setText(Integer.toString(currState.whiteScore));

	}

	@FXML
	void passHandler() {
		int nextPlayer = (currPlayer + 1)%2;
		currPlayer = nextPlayer;
	}

	@FXML
    void newGameHandler(ActionEvent event) {
		 initNewGame();
    }

    @FXML
    void playAiHandler(ActionEvent event) {
    	if(gameFinished){
    		return;
    	}
    	
    	int nextPlayer = (currPlayer+1)%2;
    	Action action = new Action(-1, -1);

    	//check player type (player/computer min-max / computer alpha-beta)
    	switch(playerType[currPlayer].getValue()){
	    	case  "Min-Max":
	    		action = computerMoves.getMinMaxAction(currState, playerDepth[currPlayer].getValue(), currPlayer);
	    		break;
	    	case "Alpha-Beta":
	    		action = computerMoves.getAlphaBetaAction(currState, playerDepth[currPlayer].getValue(), currPlayer);	
	    		break;
	    	case "Player":
	    		msgStr += "Player's turn. please select a move \n";
	    		msgBox.setText(msgStr);
	    		return;
    	}

    	if((action.rowAction != -1) && (action.colAction != -1)){
    		playTurn(action.rowAction, action.colAction);
    	}
    	else{ // player has no legal moves
    		String TempMsg = "";
    		if(currPlayer == 0){
    			TempMsg += "Black";
    		}
    		else{
    			TempMsg += "White";
    		}
    		TempMsg += " player has no legal moves. \n";
    		
    		// if the next player has no legal moves
    		if(!currState.hasValidMoves(nextPlayer)){
    			endGame();
    		}
    		else{ // nextPlayerHasLegalMove
    			// print message and switch turn
    			msgStr += TempMsg;
    			msgBox.setText(msgStr);
    			
    			if(currPlayer == blackPlayer){
    				blackLabel.setFont(Font.font("system", FontWeight.NORMAL, 12));
    	    		whiteLabel.setFont(Font.font("system", FontWeight.BOLD, 12));
    	    	}
    	    	else{
    	    		whiteLabel.setFont(Font.font("system", FontWeight.NORMAL, 12));
    	    		blackLabel.setFont(Font.font("system", FontWeight.BOLD, 12));
    	    	}
    			
    			//switch turn;
    			currPlayer = nextPlayer;
    			
    		}
    		
    	}
    	
    }
    
    public void initNewGame(){
    	currState.resetState();
    	playContinuously.setSelected(false);
    	blackLabel.setFont(Font.font("system", FontWeight.BOLD, 12));
		ObservableList<String> typeOptions = FXCollections.observableArrayList("Player" , "Min-Max", "Alpha-Beta");
		blackType.setItems(typeOptions);
		blackType.setValue("Player");
		whiteType.setItems(typeOptions);
		whiteType.setValue("Player");
		msgStr = "";
		msgBox.setText("");
		blackDepth.getValueFactory().setValue(1);
		whiteDepth.getValueFactory().setValue(1);
		gameFinished = false;
		gameTimer.stop();
		gameTimer.play();
		whiteLabel.setFont(Font.font("system", FontWeight.NORMAL, 12));
		blackLabel.setFont(Font.font("system", FontWeight.BOLD, 12));
		
		currPlayer = blackPlayer; // initialized as black player first
		
		updateBoardGui();
    }
    	

    boolean nextPlayerHasMoves(int player){
    	boolean playerHasMoves = true;
    	int otherPlayer = (player+1) %2;
    	
    	// check if player has legal moves
    	if(!currState.hasValidMoves(player)){
    		playerHasMoves = false;
    		//check if other player has legal moves
    		if(!currState.hasValidMoves(otherPlayer)){
    			//no more legal moves
    			endGame();
    		}
    		else{ // player has no legal moves but the other player can play
	    		if(player == blackPlayer){
	    			msgStr += "Black ";
	    		}
	    		else{
	    			msgStr += "White";
	    		}
	    		//print message to message box
	    		msgStr += " player has no legal moves\n";
				msgBox.setText(msgStr);

    		}
    	}
    	
    	return playerHasMoves;
    }
    
    
    void endGame(){
    	msgStr += "No more legal moves. \n THE GAME HAS ENDED \n";
		if(Integer.parseInt(blackScore.getText()) > Integer.parseInt(whiteScore.getText())){
			msgStr += "Black player is the winner";
		}
		else if(Integer.parseInt(blackScore.getText()) < Integer.parseInt(whiteScore.getText())){
			msgStr += "White player is the winner";
		}
		else{
			msgStr += "It's a tie";
		}
		if(gameFinished == false){
			msgBox.setText(msgStr);
			gameFinished = true;
		}
		gameTimer.stop();
    }


    
}
