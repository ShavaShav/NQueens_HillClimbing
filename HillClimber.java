import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.Timer;

// Solves N-Queens problem by hill climbing with random restarts and sideways moves allowed
public class HillClimber extends Observable {
	private Timer timer;
	private Board board;
	private BoardPanel panel;
	private int currentNumAttacking;
	private int numRestarts, numIterations;
	private int sidewaysMoves = 0, maxSideways, totalSidewaysMoves = 0;
	private boolean isRunning;
	// constructor
	public HillClimber(Board board, BoardPanel panel){
		this.board = board;
		this.panel = panel;
		this.maxSideways = board.numQueens/2;
		isRunning=false;
		reset();
	}
	
	public void setMaxSidewaysMoves(int sidewaysMoves){
		this.maxSideways = sidewaysMoves;
	}
	
	public int getNumRestarts(){ return numRestarts; }
	public int getNumIterations(){ return numIterations; }
	public int getSidewaysMoves(){ return sidewaysMoves; }
	public int getTotalSidewaysMoves(){ return totalSidewaysMoves; }
	public int getCurrentNumAttacking(){ return currentNumAttacking; }
	public boolean isRunning(){ return isRunning; }
	
	// return true when goal has been reached
	public void climbDemHills(int speed){
		timer = new Timer(speed, new ActionListener(){
	        public void actionPerformed(ActionEvent evt) {
            	if (generateNextNeighbour()){ // get next better state
            		numIterations++;
            		notifyChanged();
            		panel.repaint();
            		//System.out.print(board.getNumPairsAttacking() + " -> ");
            	} else { // no better neighbours
            		//System.out.println(board.getNumPairsAttacking() + " -> Local Maximum.");
            		// solved
            		if (currentNumAttacking == 0){
            			board.setChosenQueen(-1); // to stop drawing overlay
            			timer.stop();
            			isRunning = false;
            			notifyChanged();
            			panel.repaint();
            			reset();
            		} else {
            			// Random restart 
            			//System.out.println("Randomly restarting...");
            			numRestarts++;
            			notifyChanged();
            			board.resetQueens();
            			currentNumAttacking = board.getNumAttacking();
            			panel.repaint();
            		}
            	}
	        }
	    });
	    timer.start();
		isRunning = true;
	}
	
	private void notifyChanged(){
		this.setChanged();
		this.notifyObservers();
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
					// evaluate move  to potential spot
					int numAttackingPairs = board.evaluateMove(col, row);
					if (numAttackingPairs < min){ // first min neighbour
						min = numAttackingPairs; // set to new min
						minNeighbours = new ArrayList<Pair>();;
						minNeighbours.add(new Pair(col, row));
					} else if (numAttackingPairs == min){// other min neighbours
						minNeighbours.add(new Pair(col, row));
					}
				}
			}
		}
		
		// pick a random min neighbour
		int randI = (int) (Math.random() * minNeighbours.size());
		Pair neighbourMove = minNeighbours.get(randI);
		int minCol = neighbourMove.first;
		int minRow = neighbourMove.second;
		// alter state of board with new neighbour (take action)
		if (min < currentNumAttacking){
			// smaller neighbour
			sidewaysMoves = 0; // reset sideways moves		
		} else if (min == currentNumAttacking && sidewaysMoves < maxSideways) { 
			// sideways move
			sidewaysMoves++;
			totalSidewaysMoves++;
		} else {
			return false; // no smaller neighbour
		}
		board.setChosenQueen(minCol);
		board.makeMove(minCol,minRow);
		// recalculate current number of attacking queens/ make hashmap
		currentNumAttacking = board.getNumAttacking();
		notifyChanged();
		return true;
	}
	
	public void setDelay(int delay){
		if (timer != null)
			timer.setDelay(delay);
	}

	public void stop(){
		isRunning=false;
		timer.stop();
	}
	
	
	public void reset(){
		totalSidewaysMoves = 0;
		numRestarts = 0;
		numIterations = 0;
		currentNumAttacking = board.getNumAttacking();
	}
	
	private class Pair{
		protected int first, second;
		
		public Pair(int first, int second){
			this.first = first;
			this.second = second;
		}
	}
}
