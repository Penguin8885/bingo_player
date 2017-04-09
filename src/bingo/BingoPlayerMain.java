package bingo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class BingoPlayerMain extends JFrame{
	private static final int windowWidth = 500;
	private static final int windowHeight = 300;
	private static final int mainPanelWidth = (int)(windowWidth * 0.9);
	private static final int mainPanelHeight = (int)(windowHeight * 0.9);


	private JLabel fileNameLabel = null;
	private File csvFile = null;
	private JFormattedTextField minTextField = null;
	private JFormattedTextField maxTextField = null;

	public BingoPlayerMain(){
		this.setTitle("Bingo Player 設定画面");
		this.setBounds(200, 200, windowWidth, windowHeight);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* 説明 */
		JLabel explanationLabel = new JLabel("ビンゴ番号のCSVファイルと使用するシリアル番号の範囲を入力");
		explanationLabel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.9),50));
		explanationLabel.setHorizontalAlignment(JLabel.CENTER);

		/* CSV選択ボタン */
		//テキスト
		fileNameLabel = new JLabel("選択されていません");
		fileNameLabel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.3),30));
		fileNameLabel.setHorizontalAlignment(JLabel.CENTER);
		//選択ボタン
		JButton fileChooseButton = new JButton("file select");
		fileChooseButton.setPreferredSize(new Dimension(100,30));
		fileChooseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSVファイル", "csv"));
				fileChooser.setAcceptAllFileFilterUsed(false);

				int selected = fileChooser.showOpenDialog(BingoPlayerMain.this);
				if (selected == JFileChooser.APPROVE_OPTION){
					csvFile = fileChooser.getSelectedFile();
					fileNameLabel.setText(csvFile.getName());
				}
			}
		});

		JPanel fileChooseButtonPanel = new JPanel();
		fileChooseButtonPanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.45), 40));
		fileChooseButtonPanel.add(fileChooseButton, BorderLayout.EAST);
		JPanel fileChoosePanel = new JPanel();
		fileChoosePanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.92), 40));
		fileChoosePanel.add(fileChooseButtonPanel, BorderLayout.WEST);
		fileChoosePanel.add(fileNameLabel, BorderLayout.EAST);

		/* 範囲指定フォーム */
		//テキスト
		JLabel minLabel = new JLabel("シリアル番号最小値");
		minLabel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.45),30));
		minLabel.setHorizontalAlignment(JLabel.CENTER);
		JLabel maxLabel = new JLabel("シリアル番号最大値");
		maxLabel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.45),30));
		maxLabel.setHorizontalAlignment(JLabel.CENTER);
		//入力フォーム
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setGroupingUsed(false);
		minTextField = new JFormattedTextField(nf);
		minTextField.setPreferredSize(new Dimension((int)(mainPanelWidth*0.45),30));
		minTextField.setText("");
		minTextField.setColumns(15);
		minTextField.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {
				minTextField.setText("");
			}
			public void focusLost(FocusEvent e) {}
		});
		maxTextField = new JFormattedTextField(nf);
		maxTextField.setPreferredSize(new Dimension((int)(mainPanelWidth*0.45),30));
		maxTextField.setText("");
		maxTextField.setColumns(15);
		maxTextField.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent e) {
				maxTextField.setText("");
			}
			public void focusLost(FocusEvent e) {}
		});

		JPanel minRangePanel = new JPanel();
		minRangePanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.90),30));
		minRangePanel.add(minLabel, BorderLayout.WEST);
		minRangePanel.add(minTextField, BorderLayout.EAST);
		JPanel maxRangePanel = new JPanel();
		maxRangePanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.90),30));
		maxRangePanel.add(maxLabel, BorderLayout.WEST);
		maxRangePanel.add(maxTextField, BorderLayout.EAST);
		JPanel rangePanel = new JPanel();
		rangePanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.92),80));
		rangePanel.add(minRangePanel, BorderLayout.NORTH);
		rangePanel.add(maxRangePanel, BorderLayout.SOUTH);

		/* 設定用パネル配置 */
		JPanel settingPanel = new JPanel();
		settingPanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.95),200));
		settingPanel.add(explanationLabel, BorderLayout.NORTH);
		settingPanel.add(fileChoosePanel, BorderLayout.CENTER);
		settingPanel.add(rangePanel, BorderLayout.SOUTH);

		/* NEXTボタン */
		JButton nextButton = new JButton("NEXT");
		nextButton.setPreferredSize(new Dimension(100,30));
		nextButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(csvFile == null){
					JOptionPane.showMessageDialog(BingoPlayerMain.this, "ファイルが選択されていません");
					return;
				}
				int min = 0;
				int max = 0;
				try{
					min = Integer.parseInt(minTextField.getText());
					max = Integer.parseInt(maxTextField.getText());
				}
				catch(Exception ee){
					JOptionPane.showMessageDialog(BingoPlayerMain.this, "範囲の入力値が異常です");
					return;
				}
				if(min > max){
					JOptionPane.showMessageDialog(BingoPlayerMain.this, "入力値の大小が適切でありません");
				}
				else if(BingoCard.isOutOfRangeInSerial(min) || BingoCard.isOutOfRangeInSerial(max)){
					JOptionPane.showMessageDialog(BingoPlayerMain.this, "入力値が範囲外です");
				}
				else{
					BingoPlayer bingoPlayer = new BingoPlayer(csvFile.getPath(), min, max);
					bingoPlayer.setVisible(true);
					setVisible(false);
				}
			}
		});

		JPanel nextButtonPanel = new JPanel();
		nextButtonPanel.setPreferredSize(new Dimension((int)(mainPanelWidth*0.95),50));
		nextButtonPanel.add(nextButton, BorderLayout.CENTER);

		/* パネル配置 */
		JPanel mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(mainPanelWidth,mainPanelHeight));
		mainPanel.add(settingPanel, BorderLayout.NORTH);
		mainPanel.add(nextButtonPanel, BorderLayout.SOUTH);
		Container contentPane = this.getContentPane();
		contentPane.add(mainPanel);

		setVisible(true);
	}

	public static void main(String args[]){
		JFrame BingoPlayerMain = new BingoPlayerMain();
	}
}
