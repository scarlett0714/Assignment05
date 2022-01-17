package num05.yshwang.konkuk;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class AddDialog extends JDialog {
VocManager voc;
	
	JTextField tfEng = new JTextField(25);
	JTextField tfKor = new JTextField(25);
	JButton okButton = new JButton("+");
	MyFrame parent;
	
	JPanel engPanel = new JPanel();
	JPanel korPanel = new JPanel();
	
	String filePath;
	JTextArea word;
	
	public AddDialog(MyFrame parent, String title, boolean modal, VocManager voc, String filepath, JTextArea word) {
		super(parent, title, modal);
		this.parent = parent;
		this.voc = voc;
		this.filePath = filepath;
		this.word = word;
		
		this.setSize(300,120);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		init();
		this.setVisible(true);
	}

	public void init() {
		this.setLayout(new GridLayout(3,1));
		
		engPanel.add(new JLabel("영단어"));
		engPanel.add(tfEng);
		korPanel.add(new JLabel("단어뜻"));
		korPanel.add(tfKor);
		this.add(engPanel);
		this.add(korPanel);
		this.add(okButton);
		
		okButton.addActionListener(e->{
			
			String strEng = getInputEng();
			String strKor = getInputKor();
			
			if (strEng != null && strEng != null) {
				voc.addWord(new Word(strEng, strKor)); //단어장에 추가
				writeFile(strEng, strKor); //파일에 추가
				word.append(strEng+"\t"+strKor+"\n"); //JTextArea에 추가
				
			} else {
				JOptionPane.showMessageDialog(null, "단어장에 추가할 수 없습니다.", "WARNING", JOptionPane.ERROR_MESSAGE);
			}
			parent.addDig = null;
			dispose();
		});
		
		voc.voc.clear();
		voc.makeVoc(filePath);
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				tfEng.setText(""); 
				tfKor.setText(""); 
				
				parent.addDig = null;
				dispose();
			}
			
		});
	}
	
	String getInputEng() {
		if(tfEng.getText().length()==0)
			return null;
		else
			return tfEng.getText();
	}
	
	String getInputKor() {
		if(tfKor.getText().length()==0)
			return null;
		else
			return tfKor.getText();
	}
	
	public void writeFile(String eng, String kor) {
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
			PrintWriter writer = new PrintWriter(bw, true);
			writer.write("\n"+eng+"\t"+kor);
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "파일에 단어를 추가할 수 없습니다.", "WARNING", JOptionPane.ERROR_MESSAGE);
		}
		
	}

}
