import java.awt.Graphics;

import javax.swing.JPanel;

public class BoardPanel extends JPanel {

	private static final long serialVersionUID = 938703160453488137L;
	private Board board;
	/**
	 * Create the panel.
	 */
	public BoardPanel(Board board) {
		this.board = board;
		repaint();
	}
	
	public void paintComponent(Graphics g){
		board.paintBoard(g, this.getWidth(), this.getHeight());
	}

}
