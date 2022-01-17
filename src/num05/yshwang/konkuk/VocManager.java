package num05.yshwang.konkuk;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class VocManager implements Serializable{
	private static final long serialVersionUID = 1L;
	private String userName;
	ArrayList<Word> voc;
	public int[] voc2;
	
	ArrayList<Integer> tempAnswer = new ArrayList<>();

	static Scanner scan = new Scanner(System.in);

	public VocManager(String userName) {
		this.userName = userName;
	}

	void addWord(Word word) {
		voc.add(word);
	}

	boolean makeVoc(String fileName) {
		voc = new ArrayList<>();

		try (Scanner scan = new Scanner(new File(fileName))) {
			while (scan.hasNextLine()) {
				String str = scan.nextLine();
				String[] temp = str.split("\t");
				try {
				addWord(new Word(temp[0].trim(), temp[1].trim()));
				}catch(ArrayIndexOutOfBoundsException e) {
					JOptionPane.showMessageDialog(null, "해당 파일로는 단어장을 생성할 수 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
					return false;
				}
			}
			voc2 = new int[voc.size()];

		} catch (FileNotFoundException e) {
			
		}
		return true;
	}


	public int[] chooseQuizNum() {
		int[] quiz = new int[4];
		Random rand = new Random();
		for (int i = 0; i < quiz.length; i++) {
			quiz[i] = rand.nextInt(voc.size());
			for (int j = 0; j < i; j++) {
				if (quiz[j] == quiz[i] || voc.get(quiz[j]).kor.equals(voc.get(quiz[i]).kor)) {
					i--;
					break;
				}
			}
		}
		return quiz;
	}
	
	

	public String searchVoc(String sWord) {
		sWord = sWord.trim();
		boolean findResult = false;
		for (Word word : voc) {
			if (word != null && word.eng.equals(sWord)) {
				
				findResult = true;
				return word.kor;
			}
		}
		return "단어장에 등록된 단어가 아닙니다.";

	}

	public String getUserName() {
		return userName;
	}

}



