import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.Timer;

// Solves N-Queens problem by hill climbing with random restarts and sideways moves allowed
public class HillClimber {
	private Timer timer;
	private Board board;
	private BoardPanel panel;
	private int currentNumAttacking;
	private int numRestarts, numIterations;
	private static final int MAX_SIDEWAYS = 20;
	private int sidewaysMoves = 0;
	
	// constructor
	public HillClimber(Board board, BoardPanel panel){
		reset();
		this.board = board;
		this.panel = panel;
	}
	
	// return true when goal has been reached
	public void climbDemHills(int speed){
		timer = new Timer(speed, new ActionListener(){
	        public void actionPerformed(ActionEvent evt) {
            	if (generateNextNeighbour()){ // get next better state
            		numIterations++;
            		panel.repaint();
            		//System.out.print(board.getNumPairsAttacking() + " -> ");
            	} else { // no better neighbours
            		//System.out.println(board.getNumPairsAttacking() + " -> Local Maximum.");
            		// solved
            		if (currentNumAttacking == 0){
            			timer.stop();
            			String result = "Solved after taking " + numRestarts +
            					" random restarts, averaging " + numIterations/(numRestarts+1) +
            					" iterations per restart.";
            			panel.repaint();
            			reset();
            			JOptionPane.showMessageDialog(null, result);
            		} else {
            			// Random restart 
            			//System.out.println("Randomly restarting...");
            			numRestarts++;
            			board.resetQueens();
            			panel.repaint();
            		}
            	}
	        }
	    });
	    timer.start();
	}
	
	//finds the next neighbour with a smaller amount of 
	// attacking pairs and alters board. returns false if no smaller neighbour
	public boolean generateNextNeighbour(){
		// calculate heuristic values (# of attacking queens) for all possible moves
		ArrayList<Pair> minNeighbours = new ArrayList<Pair>();;
		int min = Integer.MAX_VALUE;
		for (int col = 0; col < board.numQueens; col++){
			// store the original state for column, don't recalculate for it
			int ogRow = board.getQueenRow(col);
			for (int row = 0; row < board.numQueens; row++){
				if (row != ogRow){
					// move queen to potential spot and calculate number of attacking pairs
					board.moveQueen(col, row);
					int numAttackingPairs = board.calculateAttackingPairs();
					if (numAttackingPairs < min){ // first min neighbour
						min = numAttackingPairs; // set to new min
						minNeighbours = new ArrayList<Pair>();;
						minNeighbours.add(new Pair(col, row));
					} else if (numAttackingPairs == min){// other min neighbours
						minNeighbours.add(new Pair(col, row));
					}
				}
			}
			//move back to original state
			board.moveQueen(col, ogRow);
		}
		// calculate current number of attacking queens
		currentNumAttacking = board.calculateAttackingPairs();
		
		// pick a random min neighbour
		int randI = (int) (Math.random() * minNeighbours.size());
		Pair neighbourMove = minNeighbours.get(randI);
		int minCol = neighbourMove.first;
		int minRow = neighbourMove.second;
		
		// alter state of board with new neighbour (take action)
		if (min < currentNumAttacking){
			board.moveQueen(minCol, minRow); // smaller neighbour
			sidewaysMoves = 0; // reset sideways moves
			return true;
		} else if (min == currentNumAttacking && sidewaysMoves < MAX_SIDEWAYS) { // sideways move
			board.moveQueen(minCol,minRow);
			sidewaysMoves++;
			return true;
		} else {
			return false; // no smaller neighbour
		}
	}
	
	public void setDelay(int delay){
		if (timer != null)
			timer.setDelay(delay);
	}

	public void stop(){
		timer.stop();
	}
	
	
	public void reset(){
		numRestarts = 0;
		numIterations = 0;
	}
	
	private class Pair{
		protected int first, second;
		
		public Pair(int first, int second){
			this.first = first;
			this.second = second;
		}
	}
}
