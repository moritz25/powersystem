package se.kth.eh2745.moritzv.assigment2;

import java.awt.EventQueue;

import org.apache.commons.cli.Options;

import se.kth.eh2745.moritzv.assigment2.Config;
import se.kth.eh2745.moritzv.assigment2.gui.Frame1;

public class run {

	public static void main(String[] args) {

		Options options;
		try {
			options = Config.defineOptions();
			Config.parseArgs(options, args);
		} catch (Exception e1) {
			System.err.println("Error in main");
			e1.printStackTrace();

			return;
		}

	}

	public static void startGui() {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 frame = new Frame1();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

}
