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
import java.util.Observable;
import java.util.Observer;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;

public class GUIFrame extends JFrame implements Observer {

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
	private JTextField textFieldNumQueens;
	private JPanel panel_1;
	private JButton btnRandomize;
	private JPanel panel_2;
	private JLabel lblSpeed;
	private JSlider slider;
	private JPanel panel_3;
	private static final int MIN_SPEED = 2000, MAX_SPEED = 0;
	private int currentSpeed = 250;
	private JButton btnStop;
	private JPanel panel_4;
	private JLabel lblNumberOfSidewaysMoves;
	private JTextField textFieldMaxSidewaysMoves;
	private JPanel panel_5;
	private JLabel lblNewLabel;
	private JTextField textFieldIterations;
	private JLabel lblRestarts;
	private JTextField textFieldSidewaysMoves;
	private JLabel lblSidewaysMoves;
	private JTextField textFieldRestarts;
	private JLabel lblTotalSideways;
	private JTextField textFieldTotalSideways;
	private JLabel lblPairsOfAttacks;
	private JTextField textFieldNumAttacks;

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
	
	public void generateNewBoard(){
		board = new Board(numQueens);
		contentPane.remove(panelBoard);
		panelBoard = new BoardPanel(board);
		contentPane.add(panelBoard, BorderLayout.CENTER);
		hillClimber = new HillClimber(board, panelBoard);
		hillClimber.addObserver(this);
		this.updateStats();
		contentPane.revalidate(); 
		contentPane.repaint();
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
		contentPane.setLayout(new BorderLayout(5, 2));
		setContentPane(contentPane);
		
		JPanel panelSouth = new JPanel();
		contentPane.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new GridLayout(0, 1, 0, 0));
		
		panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panelSouth.add(panel_2);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		lblSpeed = new JLabel("Speed:");
		panel_2.add(lblSpeed, BorderLayout.WEST);
		
		slider = new JSlider();
		slider.setBackground(Color.WHITE);
		slider.setForeground(Color.LIGHT_GRAY);
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
		panel.setBackground(Color.WHITE);
		panel_3.add(panel);
		
		lblQueens = new JLabel("Number of Queens:");
		panel.add(lblQueens);
		
		textFieldNumQueens = new JTextField();
		textFieldNumQueens.setText(String.valueOf(numQueens));
		textFieldNumQueens.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					numQueens = Integer.parseInt(textFieldNumQueens.getText());
					int defaultSidewaysMoves = numQueens/2;
					textFieldMaxSidewaysMoves.setText(String.valueOf(defaultSidewaysMoves));
					generateNewBoard();
				}
			}
		});
		panel.add(textFieldNumQueens);
		textFieldNumQueens.setColumns(10);
		
		panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		panel_3.add(panel_4);
		
		lblNumberOfSidewaysMoves = new JLabel("Number of Sideways Moves Allowed:");
		panel_4.add(lblNumberOfSidewaysMoves);
		
		textFieldMaxSidewaysMoves = new JTextField();
		textFieldMaxSidewaysMoves.setText(String.valueOf(numQueens/2));
		panel_4.add(textFieldMaxSidewaysMoves);
		textFieldMaxSidewaysMoves.setColumns(10);
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panelSouth.add(panel_1);
		
		btnRandomize = new JButton("Randomize");
		btnRandomize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				board.resetQueens();
				hillClimber.reset();
				updateStats();
				panelBoard.repaint();
			}
		});
		panel_1.add(btnRandomize);
		
		btnSolve = new JButton("Solve It!");
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!hillClimber.isRunning()){
					int maxSidewaysMoves = Integer.parseInt(textFieldMaxSidewaysMoves.getText());
					hillClimber.setMaxSidewaysMoves(maxSidewaysMoves);
					hillClimber.climbDemHills(currentSpeed);					
				}
			}
		});
		panel_1.add(btnSolve);
		
		btnStop = new JButton("Pause");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hillClimber.stop();
				System.out.println("Paused!");
			}
		});
		panel_1.add(btnStop);
		
		JPanel panelNoth = new JPanel();
		panelNoth.setBackground(Color.WHITE);
		contentPane.add(panelNoth, BorderLayout.NORTH);
		
		lblClimbing = new JLabel("Solving N-Queens with Hill Climbing");
		lblClimbing.setFont(new Font("Arial", Font.BOLD, 30));
		panelNoth.add(lblClimbing);
		
		panelBoard = new BoardPanel(board);
		contentPane.add(panelBoard, BorderLayout.CENTER);
		
		hillClimber = new HillClimber(board, panelBoard);
		hillClimber.addObserver(this);
		
		panel_5 = new JPanel();
		panel_5.setBackground(Color.WHITE);
		contentPane.add(panel_5, BorderLayout.WEST);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		lblNewLabel = new JLabel("Iterations:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel_5.add(lblNewLabel, gbc_lblNewLabel);
		
		textFieldIterations = new JTextField();
		textFieldIterations.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldIterations.setText("0");
		GridBagConstraints gbc_textFieldIterations = new GridBagConstraints();
		gbc_textFieldIterations.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldIterations.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldIterations.gridx = 0;
		gbc_textFieldIterations.gridy = 1;
		panel_5.add(textFieldIterations, gbc_textFieldIterations);
		textFieldIterations.setColumns(5);
		textFieldIterations.setEditable(false);
		
		lblSidewaysMoves = new JLabel("Restarts:");
		GridBagConstraints gbc_lblSidewaysMoves = new GridBagConstraints();
		gbc_lblSidewaysMoves.insets = new Insets(0, 0, 5, 0);
		gbc_lblSidewaysMoves.gridx = 0;
		gbc_lblSidewaysMoves.gridy = 2;
		panel_5.add(lblSidewaysMoves, gbc_lblSidewaysMoves);
		
		textFieldRestarts = new JTextField();
		textFieldRestarts.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldRestarts.setText("0");
		GridBagConstraints gbc_textFieldRestarts = new GridBagConstraints();
		gbc_textFieldRestarts.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldRestarts.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldRestarts.gridx = 0;
		gbc_textFieldRestarts.gridy = 3;
		panel_5.add(textFieldRestarts, gbc_textFieldRestarts);
		textFieldRestarts.setColumns(5);
		textFieldRestarts.setEditable(false);
		
		lblTotalSideways = new JLabel("Total Sideways:");
		GridBagConstraints gbc_lblTotalSideways = new GridBagConstraints();
		gbc_lblTotalSideways.insets = new Insets(0, 0, 5, 0);
		gbc_lblTotalSideways.gridx = 0;
		gbc_lblTotalSideways.gridy = 4;
		panel_5.add(lblTotalSideways, gbc_lblTotalSideways);
		
		textFieldTotalSideways = new JTextField();
		textFieldTotalSideways.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldTotalSideways.setText("0");
		GridBagConstraints gbc_textFieldTotalSideways = new GridBagConstraints();
		gbc_textFieldTotalSideways.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldTotalSideways.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldTotalSideways.gridx = 0;
		gbc_textFieldTotalSideways.gridy = 5;
		panel_5.add(textFieldTotalSideways, gbc_textFieldTotalSideways);
		textFieldTotalSideways.setColumns(5);
		textFieldTotalSideways.setEditable(false);
		
		lblRestarts = new JLabel("Sideways Move:");
		GridBagConstraints gbc_lblRestarts = new GridBagConstraints();
		gbc_lblRestarts.insets = new Insets(0, 0, 5, 0);
		gbc_lblRestarts.gridx = 0;
		gbc_lblRestarts.gridy = 6;
		panel_5.add(lblRestarts, gbc_lblRestarts);
		
		textFieldSidewaysMoves = new JTextField();
		textFieldSidewaysMoves.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldSidewaysMoves.setText("0");
		GridBagConstraints gbc_textFieldSidewaysMoves = new GridBagConstraints();
		gbc_textFieldSidewaysMoves.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldSidewaysMoves.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldSidewaysMoves.gridx = 0;
		gbc_textFieldSidewaysMoves.gridy = 7;
		panel_5.add(textFieldSidewaysMoves, gbc_textFieldSidewaysMoves);
		textFieldSidewaysMoves.setColumns(5);
		textFieldSidewaysMoves.setEditable(false);
		
		lblPairsOfAttacks = new JLabel("Pairs of Attacks:");
		GridBagConstraints gbc_lblPairsOfAttacks = new GridBagConstraints();
		gbc_lblPairsOfAttacks.insets = new Insets(0, 0, 5, 0);
		gbc_lblPairsOfAttacks.gridx = 0;
		gbc_lblPairsOfAttacks.gridy = 8;
		panel_5.add(lblPairsOfAttacks, gbc_lblPairsOfAttacks);
		
		textFieldNumAttacks = new JTextField(String.valueOf(hillClimber.getCurrentNumAttacking()));
		textFieldNumAttacks.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_textFieldNumAttacks = new GridBagConstraints();
		gbc_textFieldNumAttacks.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNumAttacks.gridx = 0;
		gbc_textFieldNumAttacks.gridy = 9;
		panel_5.add(textFieldNumAttacks, gbc_textFieldNumAttacks);
		textFieldNumAttacks.setColumns(5);
		textFieldNumAttacks.setEditable(false);
	}
	
	private void updateStats(){
		textFieldIterations.setEditable(true);
		textFieldSidewaysMoves.setEditable(true);
		textFieldRestarts.setEditable(true);
		textFieldTotalSideways.setEditable(true);
		textFieldNumAttacks.setEditable(true);
		
		textFieldIterations.setText(String.valueOf(hillClimber.getNumIterations()));
		textFieldSidewaysMoves.setText(String.valueOf(hillClimber.getSidewaysMoves()));
		textFieldRestarts.setText(String.valueOf(hillClimber.getNumRestarts()));
		textFieldTotalSideways.setText(String.valueOf(hillClimber.getTotalSidewaysMoves()));
		textFieldNumAttacks.setText(String.valueOf(hillClimber.getCurrentNumAttacking()));
		
		textFieldIterations.setEditable(false);
		textFieldSidewaysMoves.setEditable(false);
		textFieldRestarts.setEditable(false);
		textFieldTotalSideways.setEditable(false);
		textFieldNumAttacks.setEditable(false);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		updateStats();
	}

}
