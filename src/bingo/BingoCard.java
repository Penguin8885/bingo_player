package bingo;

public class BingoCard{
	public static final int MIN_NUM = 1;
	public static final int MAX_NUM = 75;
	public static final int CARD_SIZE = 24;
	public static final int MIN_SERIAL = 1;
	public static final int MAX_SERIAL = 6000;

	private int serial;
	private int[] numbers;
	private boolean[] hit;

	private int state;
	public static final int NONE = 0;
	public static final int REACH = 1;
	public static final int BINGO = 2;
	private static int[][] lineArray = {	//ビンゴ確認用配列
		{0,1,2,3,4},
		{5,6,7,8,9},
		{10,11,12,13},
		{14,15,16,17,18},
		{19,20,21,22,23},
		{0,5,10,14,19},
		{1,6,11,15,20},
		{2,7,16,21},
		{3,8,12,17,22},
		{4,9,13,18,23},
		{0,6,17,23},
		{4,8,15,19}
	};

	public BingoCard(int serial, int[] numbers){
		if(numbers.length != CARD_SIZE) {
			System.err.println("ビンゴカードの配列サイズが異常");
			System.exit(1);
		}
		numbers = sortNumbers(numbers);
		this.serial = serial;
		this.numbers = numbers.clone();
		hit = new boolean[CARD_SIZE];
		for(int i = 0; i < hit.length; i++){
			hit[i] = false;
		}
		state = NONE;
	}

	/* 数字を追加 */
	public void add(int num){
		if(isOutOfRangeInNum(num)){
			System.err.println("入力値が異常");
			System.exit(1);
		}
		for(int i = 0; i < CARD_SIZE; i++){
			if(numbers[i] == num){
				hit[i] = true;
				updataState(1);
				return;
			}
		}
	}
	/* 数字を削除 */
	public void remove(int num){
		if(isOutOfRangeInNum(num)){
			System.err.println("入力値が異常");
			System.exit(1);
		}
		for(int i = 0; i < CARD_SIZE; i++){
			if(numbers[i] == num){
				hit[i] = false;
				updataState(-1);
				return;
			}
		}
	}

	/* 状態を更新 */
	private void updataState(int todoNum){
		if(todoNum == 1 && state == BINGO) return;
		if(todoNum == -1) state = NONE;

		/* 各行の穴が空いた数から現状態を求める */
		for(int i = 0; i < 12; i++){
			int sum = 0;
			for(int j = 0; j < lineArray[i].length; j++){
				if(hit[lineArray[i][j]]) sum++;
			}
			if(lineArray[i].length - sum == 0){
				state = BINGO;
				return;
			}
			if(lineArray[i].length - sum == 1){
				state = REACH;
			}
		}
	}

	public static boolean isOutOfRangeInNum(int num){
		if(num < MIN_NUM || num > MAX_NUM){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isOutOfRangeInSerial(int serial){
		if(serial < MIN_SERIAL || serial > MAX_SERIAL){
			return true;
		}else{
			return false;
		}
	}

	/* ゲッタ */
	public int getSerial(){
		return serial;
	}
	public int getNumber(int index){
		return numbers[index];
	}
	public int[] getNumbers(){
		return numbers;
	}
	public boolean getHit(int index){
		return hit[index];
	}
	public boolean[] getHit(){
		return hit;
	}
	public int getState(){
		return state;
	}

	private int[] sortNumbers(int[] numbers){
		int[] numbers_ch = new int[CARD_SIZE];
		numbers_ch[0] = numbers[0];
		numbers_ch[1] = numbers[5];
		numbers_ch[2] = numbers[10];
		numbers_ch[3] = numbers[14];
		numbers_ch[4] = numbers[19];
		numbers_ch[5] = numbers[1];
		numbers_ch[6] = numbers[6];
		numbers_ch[7] = numbers[11];
		numbers_ch[8] = numbers[15];
		numbers_ch[9] = numbers[20];
		numbers_ch[10] = numbers[2];
		numbers_ch[11] = numbers[7];
		numbers_ch[12] = numbers[16];
		numbers_ch[13] = numbers[21];
		numbers_ch[14] = numbers[3];
		numbers_ch[15] = numbers[8];
		numbers_ch[16] = numbers[12];
		numbers_ch[17] = numbers[17];
		numbers_ch[18] = numbers[22];
		numbers_ch[19] = numbers[4];
		numbers_ch[20] = numbers[9];
		numbers_ch[21] = numbers[13];
		numbers_ch[22] = numbers[18];
		numbers_ch[23] = numbers[23];
		return numbers_ch;
	}

}
