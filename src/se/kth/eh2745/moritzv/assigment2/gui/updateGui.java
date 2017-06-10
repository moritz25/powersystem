package se.kth.eh2745.moritzv.assigment2.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.wphooper.number.Complex;

import se.kth.eh2745.moritzv.assigment2.Cluster;
import se.kth.eh2745.moritzv.assigment2.Config;
import se.kth.eh2745.moritzv.assigment2.Verification;
import se.kth.eh2745.moritzv.assigment2.exception.MysqlException;
import se.kth.eh2745.moritzv.assigment2.object.MeasauredState;
import se.kth.eh2745.moritzv.assigment2.object.Measurement;
import se.kth.eh2745.moritzv.assigment2.object.SystemClasses;
import se.kth.eh2745.moritzv.assigment2.object.kPoint;

public class updateGui {

	private int oldK = 0;
	private int oldClusterK = -1;
	private Cluster cluster;
	private Verification verification;

	Frame1 frame;

	public updateGui(Frame1 frame) {
		this.frame = frame;
	}

	public void updateCluster() {
		try {
			if (oldK != (int) frame.kSpinner.getValue()) {
				oldK = (int) frame.kSpinner.getValue();
				oldClusterK = -1;

				cluster = Cluster.clusterStartComperator(oldK);
				verification = new Verification(oldK, cluster.getStates());
				verification.classifyAll();

				updateClusterSelect();
				updateAssoc();
			}

		} catch (MysqlException e1) {

			e1.printStackTrace();
		}
	}

	public void updateClusterSelect() {

		int current_clusterK = frame.comboBox_data.getSelectedIndex();

		if (oldClusterK != current_clusterK) {
			oldClusterK = current_clusterK;
			String info = "";
			if (current_clusterK != 1) {
				frame.comboBox_cluster_time
						.setModel(new DefaultComboBoxModel<Integer>(frame.con.getLearningTimesAsArray()));
				for (kPoint kpoint : cluster.getkPoints()) {
					info += kpoint.getClassName() + ": " + kpoint.getLearningTimesString() + "\n";
				}

			} else {
				frame.comboBox_cluster_time
						.setModel(new DefaultComboBoxModel<Integer>(frame.con.getVerificationTimesAsArray()));
				for (kPoint kpoint : cluster.getkPoints()) {
					info += kpoint.getClassName() + ": " + kpoint.getVerificationTimesString() + "\n";
				}
				System.out.println("Verification Data");

			}
			frame.textPane.setText(info);

			updateLabels();

		}

	}

	public void updateLabels() {
		int time;
		Object selected;

		selected = frame.comboBox_cluster_time.getSelectedItem();

		if (selected == null) {
			time = 1;
		} else {
			time = (int) selected;
		}
		boolean learning;

		if (oldClusterK == 0) {
			learning = true;
		} else {
			learning = false;
		}
		frame.lbltime.setText("Time: " + time);
		for (String key : frame.labels.keySet()) {

			try {
				Measurement volt = frame.con.getMeasurmentsForTAndName(time, key + "_VOLT", learning);

				Measurement ang = frame.con.getMeasurmentsForTAndName(time, key + "_ANG", learning);
				frame.labels.get(key).setText(key + ":\nVolt: " + volt.getValue() + " \nAng: " + ang.getValue());

			} catch (MysqlException e) {
				e.printStackTrace();
				frame.labels.get(key).setText(key + ":\n Could not get Data");

			}
		}
	}

	private ArrayList<Double> costCache = new ArrayList<Double>(200);

	public void updateK() {
		if (costCache.size() == 0) {
			costCache.add(0, -1.0); // dummy value;
		}
		XYSeries series1 = new XYSeries("Cost");
		for (int i = 1; i < (int) frame.kMaxSpinner.getValue() + 1; i++) {
			if (costCache.size() > i) {
				series1.add(i, costCache.get(i));
			} else {
				try {
					double cost = Cluster.clusterStartComperator(i).getCost();
					series1.add(i, cost);
					costCache.add(i, cost);

				} catch (MysqlException e) {
					series1.add(i, -1);
				}
			}

		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);

		if (frame.chartk == null) {
			XYSplineRenderer render = new XYSplineRenderer();
			NumberAxis xax = new NumberAxis("k");
			NumberAxis yax = new NumberAxis("cost");
			XYPlot plot = new XYPlot(dataset, xax, yax, render);
			JFreeChart chartk = new JFreeChart(plot);
			frame.chartk = new ChartPanel(chartk);

		} else {
			frame.chartk.getChart().getXYPlot().setDataset(0, dataset);
		}

	}

	public void updateAssoc() {

		frame.panel_cluster_class.removeAll();
		frame.panel_cluster_class.setLayout(new GridLayout(oldK, 2, 0, 0));
		frame.assocComboxList = new ArrayList<JComboBox<SystemClasses>>();
		for (int i = 1; i <= oldK; i++) {
			int j = i - 1;
			JLabel lblCluster = new JLabel(
					"Cluster " + i + " (" + cluster.getkPoints()[j].getAssignedLearning().size() + " Learning States)");
			frame.panel_cluster_class.add(lblCluster);

			JComboBox<SystemClasses> comboBox = new JComboBox<SystemClasses>();
			comboBox.setModel(new DefaultComboBoxModel<SystemClasses>(SystemClasses.values()));
			comboBox.setToolTipText(i + "");

			frame.panel_cluster_class.add(comboBox);
			frame.assocComboxList.add(comboBox);
			comboBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					cluster.getkPoints()[j].setClassName((SystemClasses) comboBox.getSelectedItem());
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							renewPlots();
						}
					});
				}
			});
		}
		renewPlots();

	}

	public void autoAssign() {
		for (int i = 0; i < oldK; i++) {
			SystemClasses systemClass = cluster.getkPoints()[i].autoAssign();
			frame.assocComboxList.get(i).setSelectedItem(systemClass);
		}
	}

	public void renewPlots() {

		makeGenPlot();
		makeVolAngPlot();
	}

	private void makeVolAngPlot() {

		XYSeriesCollection dataset = new XYSeriesCollection();

		String tableName;
		boolean learning;

		if (frame.combox_assoc_cluster.getSelectedIndex() == 0) {
			tableName = Config.learningTable;
			learning = true;
		} else {
			tableName = Config.verificationTable;
			learning = false;
		}

		for (int i = 0; i < oldK; i++) {
			XYSeries series = new XYSeries("Cluster " + (i + 1) + ": " + getCluster().getkPoints()[i].getClassName());

			for (MeasauredState state : cluster.getkPoints()[i].getAssigned(learning)) {
				try {
					series.add(frame.con.getAverage(state.getTime(), "ANG", tableName),
							frame.con.getAverage(state.getTime(), "VOLT", tableName));
				} catch (MysqlException e) {
					e.printStackTrace();
				}
			}
			dataset.addSeries(series);

		}

		if (frame.chartassocVolAng == null) {
			XYDotRenderer render = new XYDotRenderer();
			render.setDotHeight(10);
			render.setDotWidth(10);
			NumberAxis xax = new NumberAxis("Angle");
			NumberAxis yax = new NumberAxis("Volt");
			yax.setAutoRangeIncludesZero(false);
			XYPlot plot = new XYPlot(dataset, xax, yax, render);

			JFreeChart chartk = new JFreeChart(plot);
			chartk.setTitle("Voltage to Angle-Plot");
			frame.chartassocVolAng = new ChartPanel(chartk);

		} else {
			frame.chartassocVolAng.getChart().getXYPlot().setDataset(0, dataset);
		}
	}

	private void makeGenPlot() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		boolean learning;
		if (frame.combox_assoc_cluster.getSelectedIndex() == 0) {

			learning = true;
		} else {

			learning = false;
		}
		// {"CLAR","BOWM"},
		String[][] buses = new String[][] { { "AMHE", "WAUT" }, { "WINL", "MAPL" } };
		for (int i = 0; i < oldK; i++) {
			XYSeries series = new XYSeries("Cluster " + (i + 1) + ": " + getCluster().getkPoints()[i].getClassName());

			for (MeasauredState state : cluster.getkPoints()[i].getAssigned(learning)) {

				double min_gen = Double.MAX_VALUE;
				for (String[] bus_gen : buses) {
					Complex gen = Measurement.getComplex(bus_gen[0], state.getTime(), false, learning);
					Complex bus = Measurement.getComplex(bus_gen[1], state.getTime(), false, learning);

					double p = Math.abs(gen.abs() * bus.abs() * Math.sin(bus.argument() - gen.argument()));

					if (p < min_gen) {
						min_gen = p;
					}
				}

				series.add(state.getTime(), min_gen);
			}
			dataset.addSeries(series);
		}

		if (frame.chartassoc == null) {
			XYDotRenderer render = new XYDotRenderer();
			render.setDotHeight(10);
			render.setDotWidth(10);

			NumberAxis xax = new NumberAxis("# of Measurement");
			NumberAxis yax = new NumberAxis("Minimal powerflow from Generationbar");

			XYPlot plot = new XYPlot(dataset, xax, yax, render);
			JFreeChart chartk = new JFreeChart(plot);
			chartk.setTitle("Generation Plot");
			frame.chartassoc = new ChartPanel(chartk);

		} else {
			frame.chartassoc.getChart().getXYPlot().setDataset(0, dataset);
		}
	}

	public Cluster getCluster() {
		if (cluster == null) {
			try {
				cluster = Cluster.clusterStartComperator(4);
			} catch (MysqlException e) {
				e.printStackTrace();
			}
		}
		return cluster;
	}

}
