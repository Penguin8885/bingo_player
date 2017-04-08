package bingo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BingoPlayerMain extends JFrame{
	private JFrame thisPointer;

	private static final int windowWidth = 300;
	private static final int windowHeight = 120;

	private JLabel label;

	public BingoPlayerMain(){
		thisPointer = this;

		this.setTitle("ファイル選択");
		this.setBounds(200, 200, windowWidth, windowHeight);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		/* テキスト表示パネル */
		label = new JLabel("ビンゴ番号のCSVファイルを選んでください");
		JPanel textPanel = new JPanel();
		textPanel.setPreferredSize(new Dimension((int)(windowWidth*0.9), (int)(windowHeight*0.35)));
		textPanel.add(label);

		/* 選択ボタン */
		JButton button = new JButton("file select");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				JFileChooser filechooser = new JFileChooser();

				int selected = filechooser.showOpenDialog(thisPointer);
				if (selected == JFileChooser.APPROVE_OPTION){
					JFrame bingoPlayer = new BingoPlayer(filechooser.getSelectedFile().getPath());
					bingoPlayer.setVisible(true);
					setVisible(false);
				}else if (selected == JFileChooser.CANCEL_OPTION){
					label.setText("キャンセルされました");
				}else if (selected == JFileChooser.ERROR_OPTION){
					label.setText("エラー又は取消しされました");
				}
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension((int)(windowWidth*0.9), (int)(windowHeight*0.26)));
		buttonPanel.add(button);

		Container contentPane = this.getContentPane();
		contentPane.add(buttonPanel, BorderLayout.NORTH);
		contentPane.add(textPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	public static void main(String args[]){
		JFrame BingoPlayerMain = new BingoPlayerMain();
	}
}
