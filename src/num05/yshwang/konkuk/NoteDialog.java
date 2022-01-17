package num05.yshwang.konkuk;

import java.awt.BorderLayout;



import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class NoteDialog extends JDialog {

	VocManager voc;
	
	int[] vocTemp2;

	JPanel noteScreen = new JPanel(new FlowLayout());

	JTextArea odap = new JTextArea(15, 25);

	MyFrame parent;

	public NoteDialog(MyFrame parent, String title, boolean modal, VocManager voc) {
		super(parent, title, modal);
		this.parent = parent;
		this.voc = voc;

		this.setSize(300, 350);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		init();
		this.setVisible(true);
	}

	public void init() {

		this.add(noteScreen, BorderLayout.NORTH);
		this.add(new JScrollPane(odap), BorderLayout.CENTER);

		int maxindex = 0;
		int max = 0;
		int count = 0;

		readFileObj();

		if(vocTemp2!=null) {
			while (count < 20) {
				for (int i = 0; i < voc.voc2.length; i++) {
					if (voc.voc2[maxindex] <= voc.voc2[i]) {
						maxindex = i;
					}
				}
				max = voc.voc2[maxindex];
				String str = max + "번->" + voc.voc.get(maxindex).eng + " : " + voc.voc.get(maxindex).kor;
				odap.append(str + "\n");

				count++;
				voc.voc2[maxindex] = 0;
			}

			voc.voc2 = vocTemp2.clone();
			
			
		}
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				parent.noteDig = null;
				dispose();
			}

		});
		
	}
	
	public void readFileObj() { //파일에 있는 객체 불러오기
		FileInputStream fin;
		try {
			fin = new FileInputStream("C:\\Users\\tptty\\Desktop\\학교자료\\1-2\\JAVA 프로그래밍\\과제\\note.txt");
			ObjectInputStream ois = new ObjectInputStream(fin);		
			@SuppressWarnings("unchecked")
			int[] vocTemp = (int[]) ois.readObject();
			
			ois.close();
			
			voc.voc2 = vocTemp.clone();
			this.vocTemp2 = vocTemp.clone();
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "오답노트가 없습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
			
		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, "오답노트가 없습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "오답노트가 없습니다.", "ERROR", JOptionPane.ERROR_MESSAGE);
			
		}
	}

}
