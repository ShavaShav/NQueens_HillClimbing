import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Board {

	protected int numQueens;
	private boolean queenAtSpace[][];
	private HashMap<Integer, ArrayList<Integer>> attackingPairs; // contains columns attacking eachother (for drawing)
	
	public Board(int numQueens){
		this.numQueens = numQueens;
		resetQueens();
	}
	
	public boolean isQueenAtSpace(int col, int row){
		return queenAtSpace[col][row];
	}
	
	// place queens on board randomly, 1 per column
	public void resetQueens(){
		queenAtSpace = new boolean[numQueens][numQueens];
		for (int i = 0; i < numQueens; i++){
			int row = (int) (Math.random() * numQueens);
			queenAtSpace[i][row] = true;
		}
		calculateAttackingPairs();
	}
	
	// move queen at column to a new row
	public void moveQueen(int atCol, int toRow){
		int queenRow = getQueenRow(atCol);
		queenAtSpace[atCol][queenRow] = false;
		queenAtSpace[atCol][toRow] = true;
	}
	
	public int getQueenRow(int forCol){
		for (int i = 0; i < numQueens; i++){
			if (queenAtSpace[forCol][i])
				return i;
		}
		return -1;
	}
	
	public int calculateAttackingPairs(){
		int currentNumAttacking = 0;
		attackingPairs = new HashMap<Integer, ArrayList<Integer>>();
		for (int i = 0; i < numQueens; i++){
			attackingPairs.put(i, new ArrayList<Integer>());
		}
		for (int col = 0; col < numQueens - 1; col++){ // dont need to check last column
			for (int row = 0; row < numQueens; row++){
				// can essentially "sweep" to the right, dont need to recheck queens
				if (queenAtSpace[col][row]){
					// search to direct right
					for (int queenCol = col+1; queenCol < numQueens; queenCol++){
						if (queenAtSpace[queenCol][row]){
							attackingPairs.get(col).add(queenCol);
							currentNumAttacking++;
						}
					}
					// search up right diagonal
					for (int queenCol = col+1, queenRow = row-1; queenCol < numQueens && queenRow >= 0; queenCol++, queenRow--){
						if (queenAtSpace[queenCol][queenRow]){
							// add to eachothers list of attackers
							attackingPairs.get(col).add(queenCol);
							currentNumAttacking++;
						}
				
					}
					// search down right diagonal
					for (int queenCol = col+1, queenRow = row+1; queenCol < numQueens && queenRow < numQueens; queenCol++, queenRow++){
						if (queenAtSpace[queenCol][queenRow]){
							attackingPairs.get(col).add(queenCol);
							currentNumAttacking++;
						}
					}
					// stop searching column
					row = numQueens;
				}
			}
		}
		return currentNumAttacking;
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
			for (int row = 0, y = 0; row < numQueens; row++, y += rowSize){
				if (queenAtSpace[col][row]){
					g2.fillOval(x, y, colSize, rowSize);
				}
			}
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
