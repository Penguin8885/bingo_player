package bingo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class BingoCardCSV{
	HashMap<Integer, BingoCard> bcMap;

	public BingoCardCSV(String filename) throws IOException{
		try{
			bcMap = new HashMap<Integer, BingoCard>();
			BufferedReader br = new BufferedReader(new FileReader(filename)); //Reader

			String line;
			while((line = br.readLine()) != null){
				if(line.equals("") || line.charAt(0) == '#'){
					continue;
				}

				String[] tokens = line.split(",", 0);

				int serial = Integer.parseInt(tokens[0]);	//シリアル番号取得
				int[] numbers = new int[BingoCard.CARD_SIZE];

				//ビンゴカード番号取得(FreeZone削除)
				for(int i = 0; i < BingoCard.CARD_SIZE/2; i++){
					numbers[i] = Integer.parseInt(tokens[i + 1]);	//前半取得
				}
				for(int i = BingoCard.CARD_SIZE/2; i < BingoCard.CARD_SIZE; i++){
					numbers[i] = Integer.parseInt(tokens[i + 2]);	//後半取得
				}

				BingoCard bc = new BingoCard(serial, numbers);	//ビンゴカードの作成
				if(bcMap.containsKey(serial)){
					br.close();
					throw new IOException("シリアル番号が重複しています");
				}
				bcMap.put(serial, bc);	//ビンゴカードをマップに保存
			}

			br.close();

		}
		catch(IOException e){
			throw new IOException("CSVファイルを読み込めません");
		}
		catch(ArrayIndexOutOfBoundsException e){
			throw new IOException("CSVファイルの形式が不適切です");
		}
		catch(NumberFormatException e){
			throw new IOException("CSVファイルの数値が不適切です");
		}
	}

	public HashMap<Integer, BingoCard> getBingoCards() {
		return bcMap;
	}
}
