package num05.yshwang.konkuk;

import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class MyFrame extends JFrame {
	Container c = getContentPane();
	
	JPanel main, panel1; //panel1 : main화면, panel2 : menu화면	
	JPanel panel3, panel4;
	ImgPanel imgPanel = new ImgPanel();
	
	JLabel school, major, num, name;
	
	ImageIcon img = new ImageIcon("img/profileImg.jpg");
	
	VocManager voc = new VocManager("황윤선");
	
	JTextArea word = new JTextArea(15,30);
	
	String filePath = null;
	
	boolean status;
	
	//버튼메뉴
	JButton search = new JButton("SEARCH");
	SearchDialog searchDig = null;
	JButton add = new JButton("+");
	AddDialog addDig = null;
	JButton quiz = new JButton("QUIZ");
	QuizDialog quizDig = null;
	JButton note = new JButton("NOTE");
	NoteDialog noteDig = null;
	

	
	public MyFrame() {
		this("202110547 황윤선");
	}
	
	public MyFrame(String title) {
		super(title);
		this.setSize(300,350);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		c.setBackground(new Color(222,239,239));
		
		init();
		this.setVisible(true);
		
	}

	public void init() {
		mainScreen();		
		

	}
	
	public void mainScreen() {
		
		main = new JPanel(new BorderLayout());
		panel1 = new JPanel(null);
		
		school = new JLabel("건국대학교", SwingConstants.CENTER);
		major = new JLabel("건축학부");
		num = new JLabel("202110547");
		name = new JLabel("황윤선");
		
		JButton start = new JButton("START");
		
		start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				menuScreen();
				
			}
			
		});
		
		imgPanel.setLocation(73, 10);
		imgPanel.setSize(150,150);
		panel1.add(imgPanel);
		
		major.setLocation(117,150);
		major.setSize(50,50);		
		panel1.add(major);
		
		num.setLocation(113,170);
		num.setSize(100,50);
		panel1.add(num);
		
		name.setLocation(120,220);
		name.setSize(50,50);
		panel1.add(name);
		main.add(school, BorderLayout.NORTH);
		panel1.setBackground(new Color(241,241,227));
		main.add(panel1, BorderLayout.CENTER);
		main.add(start, BorderLayout.SOUTH);
		
		c.add(main);
		
	}
	
	public void menuScreen() {
		main.setVisible(false);
		createMenu();
		
		panel3 = new JPanel();
		panel4 = new JPanel(new FlowLayout());
		
		
		panel3.add(new JScrollPane(word));
		
		
		search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (filePath != null) {

					if (searchDig == null) {
						searchDig = new SearchDialog(MyFrame.this, "SEARCH", false, voc);
					} else
						searchDig.requestFocus();
				}else
					JOptionPane.showMessageDialog(null, "파일을 열어주세요", "WARNING", JOptionPane.WARNING_MESSAGE);
			}

		});
		
		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (filePath != null) {

					if (addDig == null) {
						addDig = new AddDialog(MyFrame.this, "ADD", false, voc, filePath, word);
					} else
						addDig.requestFocus();
				} else
					JOptionPane.showMessageDialog(null, "파일을 열어주세요", "WARNING", JOptionPane.WARNING_MESSAGE);
				
				

			}

		});
		
		quiz.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (filePath != null) {
					if (quizDig == null) {
						quizDig = new QuizDialog(MyFrame.this, "QUIZ", false, voc);
					} else
						quizDig.requestFocus();
					
				} else
					JOptionPane.showMessageDialog(null, "파일을 열어주세요", "WARNING", JOptionPane.WARNING_MESSAGE);
			}

		});
		
		
		note.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (filePath != null) {
					if (noteDig == null) {
						noteDig = new NoteDialog(MyFrame.this, "NOTE", false, voc);
					} else {
						noteDig.requestFocus();
					}
				} else
					JOptionPane.showMessageDialog(null, "파일을 열어주세요", "WARNING", JOptionPane.WARNING_MESSAGE);

			}

		});
		
		panel4.add(search);
		panel4.add(add);
		panel4.add(quiz);
		panel4.add(note);
		
		c.add(panel3, BorderLayout.CENTER);
		c.add(panel4, BorderLayout.SOUTH);

	}
	
	public void createMenu() {
		JMenuBar mb = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new OpenActionListener());
		fileMenu.add(openItem);
		mb.add(fileMenu);
		this.setJMenuBar(mb);
	}
	
	class OpenActionListener implements ActionListener{
		JFileChooser chooser;
		OpenActionListener(){
			chooser = new JFileChooser();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT files", "txt");
			chooser.setFileFilter(filter);
			
			int ret = chooser.showOpenDialog(null);
			if(ret!=JFileChooser.APPROVE_OPTION) { //파일을 정상적으로 선택하지 않은 경우
				JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			filePath = chooser.getSelectedFile().getPath();
			status = voc.makeVoc(filePath);
			
			
			
			// textarea에 단어 출력
			if (status == true) {
				try (Scanner scan = new Scanner(new File(filePath))) {
					while (scan.hasNextLine()) {
						String str = scan.nextLine();
						word.append(str + "\n");
					}

				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "파일 경로를 확인해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
				}
			}
			
			
		}
		
	}
	
}


@SuppressWarnings("serial")
class ImgPanel extends JPanel{
	
	
	ImageIcon img = new ImageIcon("img/profileImg.jpg");
	Image image = img.getImage();
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0,0, 150	, 150,this);

	}
	
	
}


