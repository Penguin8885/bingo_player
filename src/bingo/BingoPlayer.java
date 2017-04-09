package bingo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class BingoPlayer extends JFrame {
	private static final int windowWidth = 1300;
	private static final int windowHeight = 800;
	private static final int mainPanelWidth = (int)(windowWidth * 0.9);
	private static final int mainPanelHeight = (int)(windowHeight * 0.9);

	private ArrayList<Integer> numList = new ArrayList<Integer>();
	HashMap<Integer, BingoCard> bcMap = null;

	/* コンストラクタ */
	public BingoPlayer(String bingoNumbersFilePath, int serialMin, int serialMax){
		setTitle("Bingo Player");
		setBounds(20, 20, windowWidth, windowHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		bcMap = loadBingoCardCSV(bingoNumbersFilePath, serialMin, serialMax); //CSV読み込み

		/* 右側 */
		SerialListPanel bingoSerialPanel = new SerialListPanel("BINGO", (int)(mainPanelWidth*0.24), (int)(mainPanelHeight*0.79), 4, 25);
		SerialListPanel reachSerialPanel = new SerialListPanel("REACH", (int)(mainPanelWidth*0.36), (int)(mainPanelHeight*0.79), 6, 25);
		NumberInputPanel serialInputPanel = new NumberInputPanel("シリアル検索", (int)(mainPanelWidth*0.33));
		serialInputPanel.setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int input = 0;
				try{
					input = serialInputPanel.getNumber();
				}
				catch(Exception ee){
					JOptionPane.showMessageDialog(BingoPlayer.this, "入力値が異常です");
					return;
				}
				if(BingoCard.isOutOfRangeInSerial(input)){
					JOptionPane.showMessageDialog(BingoPlayer.this, "入力値が範囲外です");
				}
				else if(bcMap.containsKey(input) == false){
					JOptionPane.showMessageDialog(BingoPlayer.this, "そのデータは存在しません");
				}
				else{
					JFrame viewer = new BingoCardViewer(bcMap.get(input));
					viewer.setVisible(true);
				}
			}
		});

		//右側のパネルに入れる
		JPanel rightBottomPanel = new JPanel();
		rightBottomPanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.65), (int)(mainPanelHeight*0.8)));
		rightBottomPanel.add(bingoSerialPanel, BorderLayout.WEST);
		rightBottomPanel.add(reachSerialPanel, BorderLayout.EAST);
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.65), mainPanelHeight));
		rightPanel.add(serialInputPanel, BorderLayout.NORTH);
		rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);


		/* 左側 */
		HitNumbersPanel hitNumbersPanel = new HitNumbersPanel((int)(mainPanelWidth*0.33), (int)(mainPanelHeight*0.5));
		NumberInputPanel hitNumberInputPanel = new NumberInputPanel("ビンゴ番号", (int)(mainPanelWidth*0.33));
		hitNumberInputPanel.setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int input = 0;
				try{
					input = hitNumberInputPanel.getNumber();
				}
				catch(Exception ee){
					JOptionPane.showMessageDialog(BingoPlayer.this, "入力値が異常です");
					return;
				}
				if(BingoCard.isOutOfRangeInNum(input)){
					JOptionPane.showMessageDialog(BingoPlayer.this, "入力値が範囲外です");
				}
				else if(numList.contains(input)){
					JOptionPane.showMessageDialog(BingoPlayer.this, "既に入力された値です");
				}
				else{
					numList.add(input);
					for(int serial : bcMap.keySet()){
						bcMap.get(serial).add(input);
					}
					hitNumbersPanel.addHitNumber(input);
					bingoSerialPanel.update(BingoCard.BINGO);
					reachSerialPanel.update(BingoCard.REACH);
				}
			}
		});
		ExtendedButtom clearButton = new ExtendedButtom("すべてクリア", (int)(mainPanelWidth*0.15),(int)(mainPanelHeight*0.15));
		clearButton.setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				while(numList.size() > 0){
					int delNum = numList.get(numList.size()-1);
					for(int serial : bcMap.keySet()){
						bcMap.get(serial).remove(delNum);
					}
					hitNumbersPanel.removeHitNumber();
					numList.remove(numList.indexOf(delNum));
				}
				bingoSerialPanel.update(BingoCard.BINGO);
				reachSerialPanel.update(BingoCard.REACH);
			}
		});
		ExtendedButtom undoButton = new ExtendedButtom("一つ戻る", (int)(mainPanelWidth*0.15),(int)(mainPanelHeight*0.15));
		undoButton.setActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(numList.size() == 0) return;
				int delNum = numList.get(numList.size()-1);
				for(int serial : bcMap.keySet()){
					bcMap.get(serial).remove(delNum);
				}
				hitNumbersPanel.removeHitNumber();
				numList.remove(numList.indexOf(delNum));
				bingoSerialPanel.update(BingoCard.BINGO);
				reachSerialPanel.update(BingoCard.REACH);
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.33),(int)(mainPanelHeight*0.15)));
		buttonPanel.add(clearButton, BorderLayout.WEST);
		buttonPanel.add(undoButton, BorderLayout.EAST);

		//左側のパネルに入れる
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.33), mainPanelHeight));
		leftPanel.add(hitNumberInputPanel, BorderLayout.NORTH);
		leftPanel.add(hitNumbersPanel, BorderLayout.CENTER);
		leftPanel.add(buttonPanel, BorderLayout.SOUTH);

		/* フレームに入れる */
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(mainPanelWidth, mainPanelHeight));
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(rightPanel, BorderLayout.EAST);
		getContentPane().add(mainPanel);
	}

	/* CSV読み込み */
	private HashMap<Integer, BingoCard> loadBingoCardCSV(String bingoNumbersFilePath, int serialMin, int serialMax){
		BingoCardCSV bcCSV = null;
		try{
			bcCSV = new BingoCardCSV(bingoNumbersFilePath, serialMin, serialMax);
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(BingoPlayer.this, e.getMessage());
			System.exit(1);
		}
		return bcCSV.getBingoCards();
	}



	class NumberInputPanel extends JPanel {
		private JFormattedTextField numberTextField = null;

		public NumberInputPanel(String numberName, int width){
			setPreferredSize(new Dimension(width, 40));
			setBorder(new LineBorder(Color.BLACK));

			/* ラベル */
			JLabel numberInputTextFieldLabel = new JLabel(numberName);
			numberInputTextFieldLabel.setPreferredSize(new Dimension(width/3, 25));
			numberInputTextFieldLabel.setHorizontalAlignment(JLabel.CENTER);
			numberInputTextFieldLabel.setVerticalAlignment(JLabel.CENTER);

			/* 入力フォーム */
			NumberFormat nf = NumberFormat.getIntegerInstance();
			nf.setGroupingUsed(false);
			numberTextField = new JFormattedTextField(nf);
			numberTextField.setText("");
			numberTextField.setColumns(15);
			numberTextField.setPreferredSize(new Dimension(width/3, 25));
			numberTextField.addFocusListener(new FocusListener(){
				public void focusGained(FocusEvent e) {
					numberTextField.setText("");
				}
				public void focusLost(FocusEvent e) {}
			});

			add(numberInputTextFieldLabel, BorderLayout.WEST);
			add(numberTextField, BorderLayout.EAST);
		}

		public void setActionListener(ActionListener actionListener){
			numberTextField.addActionListener(actionListener);
		}
		public int getNumber(){
			int input = Integer.parseInt(numberTextField.getText());
			numberTextField.setText("");
			return input;
		}
	}

	class HitNumbersPanel extends JPanel {
		private JLabel[] hitNumberLabels = new JLabel[75];
		private Color[] panelColor = {Color.WHITE, Color.PINK, Color.MAGENTA, Color.YELLOW, Color.ORANGE, Color.CYAN, Color.GREEN, Color.LIGHT_GRAY};

		public HitNumbersPanel(int width, int height){
			setPreferredSize(new Dimension(width, height));
			setLayout(new GridLayout(8, 10));

			for(int i = 0; i < hitNumberLabels.length; i++){
				hitNumberLabels[i] = new JLabel("");
				hitNumberLabels[i].setPreferredSize(new Dimension(getSize().width/5, getSize().height/8));
				hitNumberLabels[i].setFont(new Font("Arial", Font.PLAIN, 20));
				hitNumberLabels[i].setHorizontalAlignment(JLabel.CENTER);
				hitNumberLabels[i].setVerticalAlignment(JLabel.CENTER);
				hitNumberLabels[i].setBorder(new LineBorder(Color.BLACK));
				add(hitNumberLabels[i]);
			}
		}

		public void addHitNumber(int num){
			hitNumberLabels[numList.size()-1].setText(Integer.toString(num));
			hitNumberLabels[numList.size()-1].setBackground(panelColor[num/10]);
			hitNumberLabels[numList.size()-1].setOpaque(true);
		}
		public void removeHitNumber(){
			hitNumberLabels[numList.size()-1].setText("");
			hitNumberLabels[numList.size()-1].setOpaque(false);
		}
	}

	class SerialListPanel extends JPanel {
		private ArrayList<Integer> serialList = new ArrayList<Integer>();
		private String listName = null;
		private JLabel listLabel = null;
		private JLabel[] serialLabels = null;

		public SerialListPanel(String listName, int width, int height, int sizeX, int sizeY){
			serialLabels = new JLabel[sizeX*sizeY];
			setPreferredSize(new Dimension(width, height));

			/* ラベル */
			this.listName = listName;
			listLabel = new JLabel(listName+" : 0");
			listLabel.setPreferredSize(new Dimension(width, 20));

			/* シリアルリスト */
			JPanel serialListPanel = new JPanel();
			serialListPanel.setPreferredSize(new Dimension(width, height-30));
			serialListPanel.setLayout(new GridLayout(sizeY, sizeX));
			for(int i = 0; i < serialLabels.length; i++){
				serialLabels[i] = new JLabel("");
				serialLabels[i].setPreferredSize(new Dimension(getSize().width/4, getSize().height/30));
				serialLabels[i].setFont(new Font("Arial", Font.PLAIN, 20));
				serialLabels[i].setHorizontalAlignment(JLabel.CENTER);
				serialLabels[i].setVerticalAlignment(JLabel.CENTER);
				serialLabels[i].setBorder(new LineBorder(Color.BLACK));
				serialListPanel.add(serialLabels[i]);
			}

			add(listLabel, BorderLayout.NORTH);
			add(serialListPanel, BorderLayout.SOUTH);
		}

		public void update(int stateNum){
			for(int serial : bcMap.keySet()){
				int state = bcMap.get(serial).getState();
				if(state == stateNum){
					if(!serialList.contains(serial))
						serialList.add(serial);
				}
				else{
					if(serialList.contains(serial))
						serialList.remove(serialList.indexOf(serial));
				}
			}
			Collections.sort(serialList);
			int i;
			for(i = 0; i < serialLabels.length && i < serialList.size(); i++){
				serialLabels[i].setText(Integer.toString(serialList.get(i)));
			}
			for(; i < serialLabels.length; i++){
				serialLabels[i].setText("");
			}
			listLabel.setText(listName+" : "+serialList.size());
		}
	}

	class ExtendedButtom extends JPanel {
		private JButton button = null;

		public ExtendedButtom(String name, int width, int height){
			setPreferredSize(new Dimension(width, height));
			button = new JButton(name);
			button.setPreferredSize(new Dimension(150,30));
			add(button, BorderLayout.CENTER);
		}

		public void setActionListener(ActionListener actionListener){
			button.addActionListener(actionListener);
		}
	}
}
