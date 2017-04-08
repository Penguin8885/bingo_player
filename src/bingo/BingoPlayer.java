package bingo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class BingoPlayer extends JFrame {
	private static final int windowWidth = 800;
	private static final int windowHeight = 500;


	private JFrame thisPointer;

	private ArrayList<Integer> numList;

	BingoCardCSV bcCSV;

	public BingoPlayer(String bingoNumbersFileName){
		thisPointer = this;

		this.setTitle("Bingo Player");
		this.setBounds(200, 100, windowWidth, windowHeight);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* CSV読み込み */
		try{
			bcCSV = new BingoCardCSV(bingoNumbersFileName);
		}
		catch(IOException e){
			JOptionPane.showMessageDialog(thisPointer, e.getMessage());
			System.exit(1);
		}
		HashMap<Integer, BingoCard> bcMap = bcCSV.getBingoCards();

		/* メインパネル */
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(windowWidth, windowHeight));

		////////////////////
		/* ナンバーパネル */
		////////////////////
		JPanel numberPanel = new JPanel();
		numberPanel.setPreferredSize(new Dimension(windowWidth * 5 / 11, windowHeight));

		numList = new ArrayList<Integer>();


		/* 数字を入力するパネル */
		JPanel numberInputPanel = new JPanel();
		numberInputPanel.setPreferredSize(new Dimension(windowWidth * 5 / 11, windowHeight / 14));
		numberInputPanel.setBorder(new LineBorder(Color.BLACK));

		JLabel numberInputTextFieldLabel = new JLabel("Input Number");
		numberInputTextFieldLabel.setPreferredSize(new Dimension(windowWidth * 5 / 33, windowHeight / 18));
		numberInputTextFieldLabel.setHorizontalAlignment(JLabel.CENTER);
		numberInputTextFieldLabel.setVerticalAlignment(JLabel.CENTER);

		NumberFormat nf = NumberFormat.getInstance();
		JFormattedTextField newNumTextField = new JFormattedTextField(nf);
		newNumTextField.setColumns(15);
		newNumTextField.setPreferredSize(new Dimension(windowWidth * 5 / 33, windowHeight / 18));

		numberInputPanel.add(numberInputTextFieldLabel, BorderLayout.WEST);
		numberInputPanel.add(newNumTextField, BorderLayout.EAST);


		/* 入力された数字を表示するパネル */
		JPanel hitNumberPanel = new JPanel();
		hitNumberPanel.setPreferredSize(new Dimension(windowWidth * 5 / 11, windowHeight * 5 / 7));
		hitNumberPanel.setLayout(new GridLayout(8, 10));
		hitNumberPanel.setBorder(new LineBorder(Color.BLACK));

		JLabel[] hitNumberLabels = new JLabel[BingoCard.MAX_NUM - BingoCard.MIN_NUM + 1];
		for(int i = 0; i < hitNumberLabels.length; i++){
			hitNumberLabels[i] = new JLabel("");
			hitNumberLabels[i].setPreferredSize(new Dimension(windowWidth / 20, windowHeight * 3 / 28));
			hitNumberLabels[i].setFont(new Font("Arial", Font.PLAIN, 20));
			hitNumberLabels[i].setHorizontalAlignment(JLabel.CENTER);
			hitNumberLabels[i].setVerticalAlignment(JLabel.CENTER);
			hitNumberLabels[i].setBorder(new LineBorder(Color.BLACK));
		}

		for(int i = 0; i < hitNumberLabels.length; i++){
			hitNumberPanel.add(hitNumberLabels[i]);
		}


		/* 入力処理 */
		newNumTextField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int newNum;

				try{
					newNum = Integer.parseInt(newNumTextField.getText());
				}
				catch(NumberFormatException nfe){
					newNum = BingoCard.MIN_NUM - 1;
				}

				boolean outOfRange = BingoCard.isOutOfRangeInNum(newNum);
				boolean inputtedNum = false;
				for(int i = 0; i < numList.size(); i++){
					if(numList.get(i) == newNum){
						inputtedNum = true;
						break;
					}
				}
				if(outOfRange || inputtedNum){
					JOptionPane.showMessageDialog(thisPointer, "Input Number Error");
				}
				else{
					numList.add(newNum);
					for(Integer serial : bcMap.keySet()){
						bcMap.get(serial).add(newNum);
					}
					hitNumberLabels[numList.size() - 1].setText(Integer.toString(numList.get(numList.size() - 1)));
				}
				newNumTextField.setText("");
			}
		});

		/* コンポーネントの追加 */
		numberPanel.add(numberInputPanel, BorderLayout.NORTH);
		numberPanel.add(hitNumberPanel, BorderLayout.SOUTH);



		////////////////////
		/* シリアルパネル */
		////////////////////
		JPanel serialPanel = new JPanel();
		serialPanel.setPreferredSize(new Dimension(windowWidth * 5 / 11, windowHeight));


		/* シリアル検索パネル */
		JPanel serialSearchPanel = new JPanel();
		serialSearchPanel.setPreferredSize(new Dimension(windowWidth * 5 / 11, windowHeight / 14));
		serialSearchPanel.setBorder(new LineBorder(Color.BLACK));

		JLabel serialSearchTextFieldLabel = new JLabel("Serial Serach");
		serialSearchTextFieldLabel.setPreferredSize(new Dimension(windowWidth * 5 / 33, windowHeight / 18));
		serialSearchTextFieldLabel.setHorizontalAlignment(JLabel.CENTER);
		serialSearchTextFieldLabel.setVerticalAlignment(JLabel.CENTER);

		JFormattedTextField serialSearchTextField = new JFormattedTextField(nf);
		serialSearchTextField.setColumns(15);
		serialSearchTextField.setPreferredSize(new Dimension(windowWidth * 5 / 33, windowHeight / 18));

		serialSearchPanel.add(serialSearchTextFieldLabel, BorderLayout.WEST);
		serialSearchPanel.add(serialSearchTextField, BorderLayout.EAST);


		/* ビンゴリストパネル */
		JPanel bingoSerialPanel = new JPanel();
		bingoSerialPanel.setPreferredSize(new Dimension(windowWidth * 5 / 11, windowHeight * 5 / 16));
		bingoSerialPanel.setBorder(new LineBorder(Color.BLACK));

		JLabel bingoListLabel = new JLabel(" Bingo List");
		bingoListLabel.setPreferredSize(new Dimension(windowWidth * 5 / 11, windowHeight / 30));

		JPanel bingoSerialListPanel = new JPanel();
		bingoSerialListPanel.setPreferredSize(new Dimension(windowWidth * 11 / 25, windowHeight * 256 / 1000));

		bingoSerialPanel.add(bingoListLabel, BorderLayout.NORTH);
		bingoSerialPanel.add(bingoSerialListPanel, BorderLayout.SOUTH);


		/* リーチリストパネル */
		JPanel reachSerialPanel = new JPanel();
		reachSerialPanel.setPreferredSize(new Dimension(windowWidth * 5 / 11, windowHeight * 7 / 15));
		reachSerialPanel.setBorder(new LineBorder(Color.BLACK));

		JLabel reachListLabel = new JLabel(" Reach List");
		reachListLabel.setPreferredSize(new Dimension(windowWidth * 5 / 11, windowHeight / 30));

		JPanel reachSerialListPanel = new JPanel();
		reachSerialListPanel.setPreferredSize(new Dimension(windowWidth * 11 / 25, windowHeight * 41 / 100));

		reachSerialPanel.add(reachListLabel, BorderLayout.NORTH);
		reachSerialPanel.add(reachSerialListPanel, BorderLayout.SOUTH);


		/* 入力処理 */
		serialSearchTextField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int inputSerial;

				try{
					inputSerial = Integer.parseInt(serialSearchTextField.getText());
				}
				catch(NumberFormatException nfe){
					inputSerial = BingoCard.MIN_SERIAL - 1;
				}

				boolean outOfRange = BingoCard.isOutOfRangeInSerial(inputSerial);
				if(outOfRange){
					JOptionPane.showMessageDialog(thisPointer, "Input Serial Error");
				}
				else{
					JFrame viewer = new BingoCardViewer(bcMap.get(inputSerial));
					viewer.setVisible(true);
				}
				serialSearchTextField.setText("");
			}
		});


		/* コンポーネントの追加 */
		serialPanel.add(serialSearchPanel);
		serialPanel.add(bingoSerialPanel);
		serialPanel.add(reachSerialPanel);


		/* フレームのペインに追加 */
		Container contentPane = this.getContentPane();
		mainPanel.add(numberPanel, BorderLayout.WEST);
		mainPanel.add(serialPanel, BorderLayout.EAST);
		contentPane.add(mainPanel);
	}
}
