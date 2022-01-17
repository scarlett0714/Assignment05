package num05.yshwang.konkuk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SearchDialog extends JDialog {
	VocManager voc;
	
	JPanel searchScreen = new JPanel(new FlowLayout());
	
	JTextField tf = new JTextField("영단어를 입력하세요");
	JButton okButton = new JButton("SEARCH");
	MyFrame parent;
	
	JTextField result = new JTextField("검색 결과");
	
	public SearchDialog(MyFrame parent, String title, boolean modal, VocManager voc) {
		super(parent, title, modal);
		this.parent = parent;
		this.voc = voc;
		
		this.setSize(300,100);
		this.setLocationRelativeTo(null);
		this.setBackground(new Color(222,239,239));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		init();
		this.setVisible(true);
	}

	public void init() {
		
		searchScreen.add(tf);
		searchScreen.add(okButton);
		
		this.add(searchScreen, BorderLayout.NORTH);
		this.add(result, BorderLayout.CENTER);
		
		okButton.addActionListener(e->{
			
			String str = getInput();
			if(str!=null) {
				result.setText(voc.searchVoc(str));
				}
			parent.searchDig = null;
		});
		
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				tf.setText(""); 
				
				parent.searchDig = null;
				dispose();
			}
			
		});
	}
	
	String getInput() {
		if(tf.getText().length()==0)
			return null;
		else
			return tf.getText();
	}

}
