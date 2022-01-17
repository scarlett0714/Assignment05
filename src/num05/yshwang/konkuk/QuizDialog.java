package num05.yshwang.konkuk;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

@SuppressWarnings("serial")
public class QuizDialog extends JDialog implements ItemListener {

	VocManager voc;

	CardLayout cl = new CardLayout();

	JPanel quizScreen; // 문제패널
	JPanel[] card;

	JLabel question; // 문제출력 -> quizScreen의 north
	JPanel chooseAnswer; // 4지선다출력 -> quizScreen의 center

	// 전체 패널(문제패널+버튼)
	JPanel panel = new JPanel(new BorderLayout());

	// 4지선다 라디오 버튼
	ButtonGroup g;
	JRadioButton[][] check = new JRadioButton[10][4];

	// 정답입력받기
	int answer;

	int k = 0;
	int l = 0;
	
	//
	int p=0;
	int odap;
	
	
	int[] odapindex = new int[10];
	
	//
	int count = 0; // 퀴즈 횟수
	int score = 0; // 정답인 개수
	Random rand = new Random();

	//
	long time1;

	MyFrame parent;

	public QuizDialog(MyFrame parent, String title, boolean modal, VocManager voc) {
		super(parent, title, modal);
		this.parent = parent;
		this.voc = voc;

		this.setSize(250, 200);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		time1 = System.currentTimeMillis(); // 시작시간측정

		init();

		this.setVisible(true);
	}

	public void init() {
		voc.tempAnswer = new ArrayList<>();
		initPanel();
		// Quiz출제 패널 만들기(미리)

		// Quiz시작

		JButton next = new JButton(">>"); // 채점 후 다음문제로 넘어가기 ->quizScreen의 south

		panel.add(quizScreen, BorderLayout.CENTER);
		panel.add(next, BorderLayout.SOUTH);
		this.add(panel);

		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				odap = checkAnswer(); //틀린 문제 번호 반환
				if(odap!=-1) {
						for (int j = 0; j < voc.voc2.length; j++) {
							if (odapindex[odap] == j)
								voc.voc2[j] = voc.voc2[j] + 1;
						}
					
				}
				cl.next(quizScreen);

			}

		});

		parent.quizDig = null;

		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				parent.quizDig = null;
				dispose();
			}

		});
	}

	public void makeQuiz(int m) {

		chooseAnswer = new JPanel(new GridLayout(4, 1)); // 4지선다

		int[] quiz = voc.chooseQuizNum();
		int answerindex = rand.nextInt(4);

		voc.tempAnswer.add(answerindex);

		odapindex[count] = quiz[answerindex];

		try {
			// 문제출력
			question = new JLabel((count + 1) + ". " + voc.voc.get(quiz[answerindex]).eng + "의 뜻은 무엇일까요?");

			// 4지선다
			g = new ButtonGroup();
			for (int i = 0; i < quiz.length; i++) {
				check[l][i] = new JRadioButton((i + 1) + ") " + voc.voc.get(quiz[i]).kor);
				check[l][i].setBorderPainted(true);
				check[l][i].addItemListener(this);
				g.add(check[l][i]);
				chooseAnswer.add(check[l][i]);

			}

			card[m].add(question, BorderLayout.NORTH);
			card[m].add(chooseAnswer, BorderLayout.CENTER);
			count++;
			l++;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "문제출제가 되지 않았습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
		}

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		int i = -1;
		if (e.getSource() == check[k][0]) {
			i = 0;
		} else if (e.getSource() == check[k][1]) {
			i = 1;
		} else if (e.getSource() == check[k][2]) {
			i = 2;
		} else if (e.getSource() == check[k][3]) {
			i = 3;
		}

		if (i >= 0) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				answer = i;
			}
		}

	}

	public void initPanel() {
		quizScreen = new JPanel(cl); // panel2의 배치관리자 CardLayout으로 변경
		card = new JPanel[10]; // 패널을 저장할 수 있는 빈 방 생성
		for (int i = 0; i < 10; i++) {
			card[i] = new JPanel();
			makeQuiz(i);
			quizScreen.add(card[i], String.valueOf(i)); // 기본타입은 식별자로 들어갈 수 없음->참조타입으로 변환
		}

	}

	public int checkAnswer() {
		// 정답체크
		if ((answer) == voc.tempAnswer.get(k)) {
			JOptionPane.showMessageDialog(null, "정답입니다", "RESULT", JOptionPane.DEFAULT_OPTION);
			score++;
		} else if (answer >= 0 && answer <= 3) {
			String str = "틀렸습니다. 정답은 " + (voc.tempAnswer.get(k) + 1) + "번 입니다.";
			JOptionPane.showMessageDialog(null, str, "RESULT", JOptionPane.DEFAULT_OPTION);
			p=1; //틀렸다면
		}
		k++;
		

		if (k == 10) {
			long time2 = System.currentTimeMillis();
			String str = voc.getUserName() + "님 10문제 중 " + score + "개 맞추셨고, 총 " + (time2 - time1) / 1000 + "초 소요되었습니다.";
			JOptionPane.showMessageDialog(null, str, "RESULT", JOptionPane.DEFAULT_OPTION);
			parent.quizDig = null;
			dispose();
			addFileObj();

		}
		
		if(p==1) {
			p=0;
			return k-1;
		}else
			return -1;

	}

	public void addFileObj() { // 파일에 객체 추가하기
		FileOutputStream fout;
		try {
			fout = new FileOutputStream("C:\\Users\\tptty\\Desktop\\학교자료\\1-2\\JAVA 프로그래밍\\과제\\note.txt");
			ObjectOutputStream out = new ObjectOutputStream(fout); // 객체로 만들어 파일에 저장하기
			out.writeObject(voc.voc2);
			out.flush();
			out.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "파일에 출력할 수 없습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
		}

	}

}
