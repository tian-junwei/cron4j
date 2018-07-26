package test;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TestClass {

	public static void testMethod(String[] args) {
		JPanel all = new JPanel();
		all.setLayout(new GridLayout(args.length, 1));
		all.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		for (int i = 0; i < args.length; i++) {
			all.add(new JLabel(args[i], JLabel.CENTER));
		}
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setTitle("TEST");
		frame.setContentPane(all);
		frame.pack();
		frame.setVisible(true);
	}

}
