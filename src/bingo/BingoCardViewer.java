package bingo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class BingoCardViewer extends JFrame{
	BingoCard bc;

	private static final int windowWidth = 350;
	private static final int windowHeight = 400;
	private static final int statusPanelWidth = windowWidth * 6 / 7;
	private static final int statusPanelHeight = windowHeight * 1 / 7;
	private static final int numbersPanelWidth = windowWidth * 6 / 7;
	private static final int numbersPanelHeight = windowHeight * 3 / 4;


	public BingoCardViewer(BingoCard bc){
		this.bc = bc;
		this.setTitle("Bingo Card [No." + bc.getSerial() + "]");	//タイトル
		this.setBounds(200, 100, windowWidth, windowHeight);	//表示位置，サイズ
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//ウィンドウを破棄して終了

		Container contentPane = this.getContentPane();

		contentPane.add(getStatusPanel(), BorderLayout.NORTH);	//ステータスパネルを追加
		contentPane.add(getNumbersPanel(), BorderLayout.SOUTH);	//ナンバーズパネルを追加
	}

	/* ステータスパネル */
	private JPanel getStatusPanel(){
		JPanel statusPanel = new JPanel();

		statusPanel.setPreferredSize(new Dimension(statusPanelWidth, statusPanelHeight));
		statusPanel.setBorder(new LineBorder(Color.BLACK));

		/* シリアル番号 */
		JLabel serial = new JLabel("Serial No. " + bc.getSerial());
		serial.setPreferredSize(new Dimension(statusPanelWidth, statusPanelHeight * 2 / 5));
		serial.setHorizontalAlignment(JLabel.CENTER);
		serial.setVerticalAlignment(JLabel.CENTER);
		statusPanel.add(serial, BorderLayout.NORTH);

		/* ステータス */
		JLabel status = new JLabel();
		status.setPreferredSize(new Dimension(statusPanelWidth, statusPanelHeight * 2 / 5));
		status.setHorizontalAlignment(JLabel.CENTER);
		status.setVerticalAlignment(JLabel.CENTER);
		String state;
		if(bc.getState() == BingoCard.REACH) state = "Reach";
		else if(bc.getState() == BingoCard.BINGO) state = "Bingo";
		else state = "None";
		status.setText("Status : " + state);
		statusPanel.add(status, BorderLayout.SOUTH);

		return statusPanel;
	}

	/* ナンバーズパネル */
	private JPanel getNumbersPanel(){
		JPanel numbersPanel = new JPanel();
		numbersPanel.setLayout(new GridLayout(5, 5)); //格子レイアウト
		numbersPanel.setPreferredSize(new Dimension(numbersPanelWidth, numbersPanelHeight));

		/* パネルに各ナンバーラベルを追加 */
		for(int i = 0 ; i < BingoCard.CARD_SIZE / 2; i++){
			numbersPanel.add(getNumberLabel(i));
		}
		numbersPanel.add(getFreeLabel());
		for(int i = BingoCard.CARD_SIZE / 2 ; i < BingoCard.CARD_SIZE; i++){
			numbersPanel.add(getNumberLabel(i));
		}

		return numbersPanel;
	}

	/* ナンバーラベル */
	private JLabel getNumberLabel(int index){
		JLabel label = new JLabel(Integer.toString(bc.getNumber(index)));
		label.setPreferredSize(new Dimension(numbersPanelWidth / 5 , numbersPanelHeight / 5));
		label.setOpaque(true);
		if(bc.getHit(index)){
			label.setBackground(Color.ORANGE);
		}
		label.setBorder(new LineBorder(Color.BLACK));
		label.setFont(new Font("Arial", Font.PLAIN, 30));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		return label;
	}

	/* フリーラベル */
	private static JLabel getFreeLabel(){
		JLabel label = new JLabel("free");
		label.setPreferredSize(new Dimension(numbersPanelWidth / 5 , numbersPanelHeight / 5));
		label.setOpaque(true);
		label.setBackground(Color.ORANGE);
		label.setBorder(new LineBorder(Color.BLACK));
		label.setFont(new Font("Arial", Font.PLAIN, 25));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		return label;
	}
}
