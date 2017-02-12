import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Board {

	protected int numQueens;
	private int board[]; // index = column, entry = row
	private HashMap<Integer, ArrayList<Integer>> attackingPairs; // contains columns attacking eachother (for drawing)
	private int numAttacking;
	private int chosenQueen;
	
	public Board(int numQueens){
		this.numQueens = numQueens;
		resetQueens();
	}
	
	public boolean isQueenAtSpace(int col, int row){
		return board[col] == row;
	}
	
	// place queens on board randomly, 1 per column
	public void resetQueens(){
		numAttacking = 0;
		chosenQueen = -1;
		board = new int[numQueens];
		for (int col = 0; col < numQueens; col++){
			int row = (int) (Math.random() * numQueens);
			board[col] = row;
		}
		initializeAttackingPairs();
	}
	
	public void setChosenQueen(int col){
		chosenQueen = col;
	}
	
	public int getQueenRow(int forCol){
		return board[forCol];
	}
	
	// returns the number of attacks after a hypothetical move
	public int evaluateMove(int atCol, int toRow){
		// total attacking - num attacking chosen queen
		int newNumAttacking = numAttacking - attackingPairs.get(atCol).size();
		for (int attackingCol = 0; attackingCol < numQueens; attackingCol++){
			// horizontal or diagonal attack
			if ((toRow == board[attackingCol]) || 
					(Math.abs(atCol - attackingCol) == Math.abs(toRow - board[attackingCol]))){
				newNumAttacking++;
			}
		}
		return newNumAttacking;
	}
	
	private void initializeAttackingPairs(){
		attackingPairs = new HashMap<Integer, ArrayList<Integer>>();
		numAttacking = 0;
		// initialize array lists
		for (int i = 0; i < numQueens; i++)
			attackingPairs.put(i, new ArrayList<Integer>());
		// calculate attacking pairs
		for (int atCol = 0; atCol < numQueens; atCol++){
			// can sweep to right
			for (int attackingCol = atCol+1; attackingCol < numQueens; attackingCol++){
				// horizontal or diagonal attack!
				if ((board[atCol] == board[attackingCol]) || 
						(Math.abs(atCol - attackingCol) == Math.abs(board[atCol] - board[attackingCol]))){
					// add attacking queens to eachothers lists
					attackingPairs.get(atCol).add(attackingCol);
					attackingPairs.get(attackingCol).add(atCol);
					numAttacking++;	// update count			
				}				
			}			
		}
	}
	
	public void makeMove(int atCol, int toRow){
		// remove current attacks from count
		numAttacking -= attackingPairs.get(atCol).size();
		// remove current attacks from moving queen
		for (int attackingCol : attackingPairs.get(atCol))
			attackingPairs.get(attackingCol).remove(Integer.valueOf(atCol));
		attackingPairs.get(atCol).clear();
		// move queen
		board[atCol] = toRow;
		// for each queen
		for (int attackingCol = 0; attackingCol < numQueens; attackingCol++){
			// that is not the moving queen
			if (attackingCol != atCol){
				// horizontal or diagonal attack!
				if ((board[atCol] == board[attackingCol]) || 
						(Math.abs(atCol - attackingCol) == Math.abs(board[atCol] - board[attackingCol]))){
					// add attacking queens to eachothers lists
					attackingPairs.get(atCol).add(attackingCol);
					attackingPairs.get(attackingCol).add(atCol);
					numAttacking++;	// update count			
				}				
			}
		}
	}
	
	public int getNumAttacking(){
		return numAttacking;
	}
	
	// paints the board and queens
	public void paintBoard(Graphics g, int width, int height){
		Graphics2D g2 = (Graphics2D) g;
		// paint board
		// brown background
		g2.setColor(new Color(201,162,128));
		g2.fillRect(0, 0, width, height);
		// black grid
		int rowSize = height/numQueens; // numQueen rows and cols
		int colSize = width/numQueens;
		g2.setColor(Color.BLACK);	
		for (int col = 1, x = colSize; col < numQueens; col++, x += colSize){
			g2.drawLine(x, 0, x, height);
		}
		for (int row = 1, y = rowSize; row < numQueens; row++, y += rowSize){
			g2.drawLine(0, y, width, y);
		}
		// paint queens (dark red)
		g2.setColor(new Color(122,51,48));
		for (int col = 0, x = 0; col < numQueens; col++, x += colSize){
			int y = board[col] * rowSize;
			g2.fillOval(x, y, colSize, rowSize);
		}
		// overlay the chosen queen
		if (chosenQueen >= 0){
			g2.setColor(new Color(165,60,57));
			g2.fillOval(chosenQueen*colSize, board[chosenQueen]*rowSize, 
					colSize, rowSize);			
		}
		// paint attacks
		g2.setColor(Color.RED);
		for (int queenCol = 0; queenCol < numQueens; queenCol++){
			ArrayList<Integer> attackingQueens = attackingPairs.get(queenCol);
			for (int attackingQueenCol : attackingQueens){
				// draw line from queen to attacking queen
				int queenRow = getQueenRow(queenCol);
				int attackingQueenRow = getQueenRow(attackingQueenCol);
				// center of starting queen
				int startX = (queenCol*colSize)+(colSize/2);
				int startY = (queenRow*rowSize)+(rowSize/2);
				g2.fillOval(startX-5, startY-5, 10, 10);
				// center of opposing queen
				int endX = (attackingQueenCol*colSize)+(colSize/2);
				int endY = (attackingQueenRow*rowSize)+(rowSize/2);
				g2.fillOval(endX-5, endY-5, 10, 10);
				g2.setStroke(new BasicStroke(3));
				g2.drawLine(startX, startY, endX, endY);
			}
		}
	}
}
