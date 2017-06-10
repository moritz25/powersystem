package se.kth.eh2745.moritzv.assigment2.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.SwingConstants;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import se.kth.eh2745.moritzv.assigment2.db.MysqlConnector;
import se.kth.eh2745.moritzv.assigment2.object.SystemClasses;

import java.awt.Color;
import javax.swing.JLayeredPane;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.border.BevelBorder;
import java.awt.Insets;
import java.awt.Component;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.SpinnerNumberModel;
import java.awt.Rectangle;
import javax.swing.JButton;

public class Frame1 extends JFrame {

	private static final long serialVersionUID = 3665474518313168070L;
	protected updateGui updateGui;
	protected HashMap<String, JTextArea> labels;
	MysqlConnector con;

	protected JPanel contentPane;
	protected JLabel label_img;
	protected BufferedImage img = null;
	protected JLabel label_3;
	protected JLabel label_4;
	protected JLayeredPane layeredPane;
	protected JTextArea lb_gra;
	protected JPanel panel_img;
	protected JPanel panel_text;
	protected JTextArea lb_wau;
	protected JTextArea lb_cro;
	protected JTextArea lb_map;
	protected JTextArea lb_tro;
	protected JTextArea lb_amh;
	protected JTextArea lb_win;
	protected JTextArea lb_bow;
	protected JTextArea lb_cla;
	protected JPanel panel_cluster;
	protected JLabel lblSelectData;
	protected JComboBox<Integer> comboBox_cluster_time;
	protected JLabel label_2;
	protected JComboBox<String> comboBox_data;
	protected JTextArea textPane;
	protected JLabel lblClusterbyTime;
	protected JPanel panel;
	protected JPanel panel_1;
	protected JLabel lbltime;
	protected JTabbedPane tabbedPane_1;
	protected JPanel panel_k;
	protected JPanel panel_classes;
	protected JLabel lblSelectMaxK;
	protected JSpinner kSpinner;
	protected ChartPanel chartk;
	protected JLabel label_6;
	protected JSpinner kMaxSpinner;
	protected JPanel panel_chartk;
	protected JPanel panel_2;
	protected JPanel panel_cluster_class;
	protected ChartPanel chartassoc;
	protected JPanel panel_3;
	protected JComboBox<String> combox_assoc_cluster;
	protected JPanel panel_chart_assoc;
	protected ChartPanel chartassocVolAng;
	protected JPanel panel_4;
	protected JButton btnNewButton;
	protected ArrayList<JComboBox<SystemClasses>> assocComboxList;

	/**
	 * Create the frame.
	 */
	public Frame1() {
		con = new MysqlConnector();
		updateGui = new updateGui(this);
		try {
			img = ImageIO.read(new File("System.png"));
		} catch (IOException e) {

			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 574);
		contentPane = new JPanel();
		contentPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {

				// panel.setSize(new
				// Dimension(arg0.getComponent().getWidth(),arg0.getComponent().getHeight()));
				int height = (layeredPane.getHeight());
				int width = (layeredPane.getHeight()) * 1043 / 618;
				if (width > layeredPane.getWidth()) {
					height = (layeredPane.getWidth()) * 618 / 1043;
					width = (layeredPane.getWidth());
				}

				Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
				label_img.setIcon(new ImageIcon(dimg));

				panel_text.setSize(width, height);
				panel_img.setSize(width, height);
				panel_text.setLocation(panel_img.getLocation());

			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JLabel label = new JLabel("Power System Calculator");
		contentPane.add(label, BorderLayout.NORTH);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));

		labels = new HashMap<String, JTextArea>();

		tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane_1, BorderLayout.CENTER);

		panel_k = new JPanel();
		tabbedPane_1.addTab("Choose k", null, panel_k, null);
		GridBagLayout gbl_panel_k = new GridBagLayout();
		gbl_panel_k.columnWidths = new int[] { 444 };
		gbl_panel_k.rowHeights = new int[] { 22 };
		gbl_panel_k.columnWeights = new double[] { 1.0, 0.0 };
		gbl_panel_k.rowWeights = new double[] { 1.0 };
		panel_k.setLayout(gbl_panel_k);

		panel_chartk = new JPanel();
		GridBagConstraints gbc_panel_chartk = new GridBagConstraints();
		gbc_panel_chartk.insets = new Insets(0, 0, 5, 5);
		gbc_panel_chartk.fill = GridBagConstraints.BOTH;
		gbc_panel_chartk.gridx = 0;
		gbc_panel_chartk.gridy = 0;
		panel_k.add(panel_chartk, gbc_panel_chartk);

		panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 5);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 1;
		gbc_panel_2.gridy = 0;
		panel_k.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));

		lblSelectMaxK = new JLabel("Select max k");
		panel_2.add(lblSelectMaxK);

		kMaxSpinner = new JSpinner();
		kMaxSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				updateGui.updateK();
			}
		});
		panel_2.add(kMaxSpinner);
		kMaxSpinner.setModel(new SpinnerNumberModel(5, 2, 200, 1));

		label_6 = new JLabel("Select k");
		panel_2.add(label_6);

		kSpinner = new JSpinner();
		kSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				updateGui.updateCluster();
			}
		});
		panel_2.add(kSpinner);
		kSpinner.setModel(new SpinnerNumberModel(4, 1, 200, 1));

		panel_classes = new JPanel();
		tabbedPane_1.addTab("Associate Clusters", null, panel_classes, null);
		panel_classes.setLayout(new GridLayout(0, 2, 0, 0));

		panel_3 = new JPanel();
		panel_classes.add(panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[] { 483 };
		gbl_panel_3.rowHeights = new int[] { 20, 400 };
		gbl_panel_3.columnWeights = new double[] { 0.0 };
		gbl_panel_3.rowWeights = new double[] { 0.0, 1.0 };
		panel_3.setLayout(gbl_panel_3);

		combox_assoc_cluster = new JComboBox<String>();
		combox_assoc_cluster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateGui.renewPlots();
			}
		});
		combox_assoc_cluster
				.setModel(new DefaultComboBoxModel<String>(new String[] { "Learning Data", "Verification Data" }));

		GridBagConstraints gbc_combox_assoc_cluster = new GridBagConstraints();
		gbc_combox_assoc_cluster.fill = GridBagConstraints.BOTH;
		gbc_combox_assoc_cluster.insets = new Insets(0, 0, 5, 0);
		gbc_combox_assoc_cluster.gridx = 0;
		gbc_combox_assoc_cluster.gridy = 0;
		panel_3.add(combox_assoc_cluster, gbc_combox_assoc_cluster);

		panel_chart_assoc = new JPanel();
		GridBagConstraints gbc_panel_chart_assoc = new GridBagConstraints();
		gbc_panel_chart_assoc.fill = GridBagConstraints.BOTH;
		gbc_panel_chart_assoc.gridx = 0;
		gbc_panel_chart_assoc.gridy = 1;
		panel_3.add(panel_chart_assoc, gbc_panel_chart_assoc);

		updateGui.updateK();
		GridBagConstraints gbc_chartk = new GridBagConstraints();
		gbc_chartk.anchor = GridBagConstraints.NORTHWEST;
		gbc_chartk.gridx = 2;
		gbc_chartk.gridy = 1;
		panel_chartk.add(chartk, gbc_chartk);
		panel_4 = new JPanel();
		panel_classes.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));

		btnNewButton = new JButton("Associate automatically");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateGui.autoAssign();
			}
		});
		panel_4.add(btnNewButton, BorderLayout.NORTH);

		panel_cluster_class = new JPanel();
		panel_4.add(panel_cluster_class);

		updateGui.updateAssoc();

		GridBagConstraints gbc_chartassoc = new GridBagConstraints();
		gbc_chartassoc.anchor = GridBagConstraints.NORTHWEST;
		gbc_chartassoc.gridx = 2;
		gbc_chartassoc.gridy = 1;
		panel_chart_assoc.add(chartassoc, gbc_chartassoc);
		GridBagConstraints gbc_chartassocVolAng = new GridBagConstraints();
		gbc_chartassocVolAng.anchor = GridBagConstraints.NORTHWEST;
		gbc_chartassocVolAng.gridx = 2;
		gbc_chartassocVolAng.gridy = 1;
		panel_chart_assoc.add(chartassocVolAng, gbc_chartassocVolAng);
		panel_chart_assoc.setLayout(new GridLayout(2, 0, 0, 0));

		panel = new JPanel();
		tabbedPane_1.addTab("View Data", null, panel, null);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		panel_1 = new JPanel();
		panel.add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 490, 0 };
		gbl_panel_1.rowHeights = new int[] { 16, 467, 0 };
		gbl_panel_1.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		lbltime = new JLabel("Time: ?");
		lbltime.setHorizontalTextPosition(SwingConstants.LEFT);
		lbltime.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lbltime = new GridBagConstraints();
		gbc_lbltime.anchor = GridBagConstraints.NORTH;
		gbc_lbltime.insets = new Insets(0, 0, 5, 0);
		gbc_lbltime.gridx = 0;
		gbc_lbltime.gridy = 0;
		panel_1.add(lbltime, gbc_lbltime);

		layeredPane = new JLayeredPane();
		GridBagConstraints gbc_layeredPane = new GridBagConstraints();
		gbc_layeredPane.anchor = GridBagConstraints.NORTHWEST;
		gbc_layeredPane.gridx = 0;
		gbc_layeredPane.gridy = 1;
		panel_1.add(layeredPane, gbc_layeredPane);
		layeredPane.setBounds(new Rectangle(0, 0, 200, 200));
		layeredPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagLayout gbl_layeredPane = new GridBagLayout();
		gbl_layeredPane.columnWidths = new int[] { 486, 0 };
		gbl_layeredPane.rowHeights = new int[] { 463, 0 };
		gbl_layeredPane.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_layeredPane.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		layeredPane.setLayout(gbl_layeredPane);

		panel_img = new JPanel();
		layeredPane.setLayer(panel_img, 0, 1);
		GridBagConstraints gbc_panel_img = new GridBagConstraints();
		gbc_panel_img.fill = GridBagConstraints.BOTH;
		gbc_panel_img.gridx = 0;
		gbc_panel_img.gridy = 0;
		layeredPane.add(panel_img, gbc_panel_img);

		label_img = new JLabel("");
		label_img.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel_img.add(label_img);
		label_img.setMaximumSize(new Dimension(1000, 618));
		label_img.setHorizontalAlignment(SwingConstants.CENTER);
		Image dimg = img.getScaledInstance(label_img.getWidth() + 1, label_img.getHeight() + 1, Image.SCALE_SMOOTH);
		label_img.setIcon(new ImageIcon(dimg));

		panel_text = new JPanel();
		panel_text.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_panel_text = new GridBagConstraints();
		gbc_panel_text.fill = GridBagConstraints.BOTH;
		gbc_panel_text.gridx = 0;
		gbc_panel_text.gridy = 0;
		layeredPane.add(panel_text, gbc_panel_text);
		panel_text.setOpaque(false);
		layeredPane.setLayer(panel_text, 0, 0);
		panel_text.setLayout(new GridLayout(3, 3, 5, 5));

		lb_cla = new JTextArea("cla");
		lb_cla.setForeground(Color.BLACK);
		lb_cla.setFont(new Font("Monospaced", Font.BOLD, 15));
		lb_cla.setBorder(null);
		lb_cla.setColumns(3);
		lb_cla.setOpaque(false);
		lb_cla.setEditable(false);

		panel_text.add(lb_cla);

		lb_cro = new JTextArea("cro");
		lb_cro.setForeground(Color.BLACK);
		lb_cro.setFont(new Font("Monospaced", Font.BOLD, 15));
		lb_cro.setOpaque(false);
		lb_cro.setEditable(false);
		panel_text.add(lb_cro);

		lb_amh = new JTextArea("amh");
		lb_amh.setForeground(Color.BLACK);
		lb_amh.setFont(new Font("Monospaced", Font.BOLD, 15));
		lb_amh.setOpaque(false);
		lb_amh.setEditable(false);
		panel_text.add(lb_amh);

		lb_bow = new JTextArea("bow");
		lb_bow.setForeground(Color.BLACK);
		lb_bow.setFont(new Font("Monospaced", Font.BOLD, 15));
		lb_bow.setOpaque(false);
		lb_bow.setEditable(false);
		panel_text.add(lb_bow);

		lb_map = new JTextArea("map");
		lb_map.setForeground(Color.BLACK);
		lb_map.setFont(new Font("Monospaced", Font.BOLD, 15));
		lb_map.setOpaque(false);
		lb_map.setEditable(false);
		panel_text.add(lb_map);

		lb_wau = new JTextArea("wau");
		lb_wau.setForeground(Color.BLACK);
		lb_wau.setFont(new Font("Monospaced", Font.BOLD, 15));
		lb_wau.setOpaque(false);
		lb_wau.setEditable(false);
		panel_text.add(lb_wau);

		lb_tro = new JTextArea("tro");
		lb_tro.setForeground(Color.BLACK);
		lb_tro.setFont(new Font("Monospaced", Font.BOLD, 15));
		lb_tro.setOpaque(false);
		lb_tro.setEditable(false);
		panel_text.add(lb_tro);

		lb_win = new JTextArea("win");
		lb_win.setForeground(Color.BLACK);
		lb_win.setFont(new Font("Monospaced", Font.BOLD, 15));
		lb_win.setOpaque(false);
		lb_win.setEditable(false);
		panel_text.add(lb_win);

		lb_gra = new JTextArea("gra");
		lb_gra.setForeground(Color.BLACK);
		lb_gra.setFont(new Font("Monospaced", Font.BOLD, 15));
		lb_gra.setOpaque(false);
		lb_gra.setEditable(false);
		panel_text.add(lb_gra);
		layeredPane.setLayer(lb_gra, 0, 0);
		labels.put("AMHE", lb_amh);
		labels.put("BOWM", lb_bow);
		labels.put("CLAR", lb_cla);
		labels.put("CROSS", lb_cro);
		labels.put("GRAN", lb_gra);
		labels.put("MAPL", lb_map);
		labels.put("TROY", lb_tro);
		labels.put("WAUT", lb_wau);
		labels.put("WINL", lb_win);

		panel_cluster = new JPanel();
		panel.add(panel_cluster);
		panel_cluster.setMinimumSize(new Dimension(200, 10));
		panel_cluster.setLayout(new GridLayout(4, 2, 0, 0));

		lblSelectData = new JLabel("Select Data");
		panel_cluster.add(lblSelectData);

		comboBox_data = new JComboBox<String>();
		comboBox_data.setModel(new DefaultComboBoxModel<String>(new String[] { "Learning Data", "Verification Data" }));

		panel_cluster.add(comboBox_data);

		lblClusterbyTime = new JLabel("Cluster (by time)");
		panel_cluster.add(lblClusterbyTime);

		textPane = new JTextArea();
		textPane.setLineWrap(true);
		textPane.setWrapStyleWord(true);
		textPane.setMinimumSize(new Dimension(50, 20));
		textPane.setMaximumSize(new Dimension(500, 50));
		textPane.setEditable(false);
		textPane.setText("");
		panel_cluster.add(textPane);

		label_2 = new JLabel("Select Time");
		panel_cluster.add(label_2);

		comboBox_cluster_time = new JComboBox<Integer>();
		panel_cluster.add(comboBox_cluster_time);
		comboBox_data.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateGui.updateClusterSelect();
			}

		});

		comboBox_cluster_time.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateGui.updateLabels();
			}
		});

		updateGui.updateCluster();

	}

}
