import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTextField;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;

import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;

public class GUIFrame extends JFrame {

	private static final long serialVersionUID = 1497871834264018557L;
	private JPanel contentPane;
	private Board board;
	private BoardPanel panelBoard;
	private int numQueens;
	private HillClimber hillClimber;
	private JLabel lblClimbing;
	private JButton btnSolve;
	private JPanel panel;
	private JLabel lblQueens;
	private JTextField textField;
	private JPanel panel_1;
	private JButton btnRandomize;
	private JPanel panel_2;
	private JLabel lblSpeed;
	private JSlider slider;
	private JPanel panel_3;
	private static final int MIN_SPEED = 2000, MAX_SPEED = 0;
	private int currentSpeed = 250;
	private JButton btnStop;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIFrame frame = new GUIFrame();
					frame.setSize(900,900);
					frame.setTitle("N Queens Problem");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUIFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		// generate an 8 queens problem by default
		numQueens = 8;
		board = new Board(numQueens);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panelSouth = new JPanel();
		contentPane.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new GridLayout(2, 2, 0, 0));
		
		panel_2 = new JPanel();
		panelSouth.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		lblSpeed = new JLabel("Speed:");
		panel_2.add(lblSpeed, BorderLayout.WEST);
		
		slider = new JSlider();
		slider.setMinimum(MAX_SPEED);
		slider.setMaximum(MIN_SPEED);
		slider.setValue(MIN_SPEED-currentSpeed);
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			        int val = (int)source.getValue();
			        currentSpeed = MIN_SPEED-val;
			        System.out.println(currentSpeed);
			        hillClimber.setDelay(currentSpeed);
			    }
			}	
		});
		panel_2.add(slider, BorderLayout.CENTER);
		
		panel_3 = new JPanel();
		panelSouth.add(panel_3);
		panel_3.setLayout(new GridLayout(0, 2, 0, 0));
		
		panel = new JPanel();
		panel_3.add(panel);
		
		lblQueens = new JLabel("Number of Queens:");
		panel.add(lblQueens);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					numQueens = Integer.parseInt(textField.getText());
					board = new Board(numQueens);
					contentPane.remove(panelBoard);
					panelBoard = new BoardPanel(board);
					contentPane.add(panelBoard, BorderLayout.CENTER);
					hillClimber = new HillClimber(board, panelBoard);
					contentPane.revalidate(); 
					contentPane.repaint();
				}
			}
		});
		panel.add(textField);
		textField.setColumns(10);
		
		panel_1 = new JPanel();
		panel_3.add(panel_1);
		
		btnRandomize = new JButton("Randomize");
		btnRandomize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				board.resetQueens();
				panelBoard.repaint();
			}
		});
		panel_1.add(btnRandomize);
		
		btnSolve = new JButton("Solve It!");
		panel_1.add(btnSolve);
		
		btnStop = new JButton("Pause");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hillClimber.stop();
				System.out.println("Paused!");
			}
		});
		panel_1.add(btnStop);
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				hillClimber.climbDemHills(currentSpeed);
			}
		});
		
		JPanel panelNoth = new JPanel();
		panelNoth.setBackground(Color.WHITE);
		contentPane.add(panelNoth, BorderLayout.NORTH);
		
		lblClimbing = new JLabel("Solving N-Queens with Hill Climbing");
		lblClimbing.setFont(new Font("Arial", Font.BOLD, 30));
		panelNoth.add(lblClimbing);
		
		panelBoard = new BoardPanel(board);
		contentPane.add(panelBoard, BorderLayout.CENTER);
		
		hillClimber = new HillClimber(board, panelBoard);
	}

}
