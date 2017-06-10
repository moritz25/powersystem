package se.kth.eh2745.moritzv.assigment2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import se.kth.eh2745.moritzv.assigment2.db.MysqlConnector;
import se.kth.eh2745.moritzv.assigment2.exception.MysqlException;
import se.kth.eh2745.moritzv.assigment2.object.LearningState;
import se.kth.eh2745.moritzv.assigment2.object.kPoint;

public class Cluster {

	protected kPoint[] kPoints;
	protected LearningState[] states;
	protected MysqlConnector con;
	private Random random = new Random();

	public Cluster(int k, LearningState[] states) throws MysqlException {

		this.states = states;

		kPoints = new kPoint[k];
		randomObjectForKStart(k);

		clusterStep();
		int i = 0;
		Arrays.sort(kPoints);
		for (kPoint kPoint : kPoints) {
			kPoint.setClassName("Cluster " + ++i + "");
		}
		if (Config.verbose) {
			for (kPoint kPoint : kPoints) {

				System.out.println(kPoint);
				System.out.println(kPoint.getAverageDistanceOfAssigned());
			}
		}

	}

	public static Cluster clusterStartComperator(int k) throws MysqlException {
		MysqlConnector con = new MysqlConnector();
		return clusterStartComperator(Config.starting, k, con.getAllLeariningStates().toArray(new LearningState[0]));

	}

	public static Cluster clusterStartComperator(int tries, int k, LearningState[] states) throws MysqlException {
		double minCost = Double.MAX_VALUE;
		Cluster optimalCluster = null;
		for (int i = 0; i < tries; i++) {
			LearningState[] newStates = new LearningState[states.length];
			for (int j = 0; j < states.length; j++) {
				try {
					newStates[j] = states[j].clone();
				} catch (CloneNotSupportedException e) {

					e.printStackTrace();
				}
			}
			Cluster tmpCluster = new Cluster(k, newStates);

			if (tmpCluster.getCost() < minCost) {
				minCost = tmpCluster.getCost();
				optimalCluster = tmpCluster;
			}
		}
		return optimalCluster;
	}

	private void randomObjectForKStart(int k) {
		ArrayList<Integer> usedStartingStates = new ArrayList<Integer>();

		for (int j = 0; j < k; j++) {

			int index = getRandomInt(usedStartingStates, states.length);
			usedStartingStates.add(index);
			if (Config.verbose) {
				System.out.println("Used as Starting point point with time: " + states[index].getTime());
			}
			kPoints[j] = new kPoint(states[index].getVektor());

		}
	}

	public double getCost() {
		double cost = 0;
		for (kPoint kPoint : kPoints) {
			cost += kPoint.getAverageDistanceOfAssigned();
		}
		return cost;
	}

	private int getRandomInt(ArrayList<Integer> exclude, int max) {
		int candidate = random.nextInt(max);
		if (exclude.contains(candidate)) {
			return getRandomInt(exclude, max);
		} else {

			return candidate;
		}
	}

	private void clusterStep() {
		if (Config.verbose) {
			System.out.println("Do Cluster Step");
		}
		boolean hadUpdate = false;
		for (LearningState state : states) {

			hadUpdate = findNearestKPoint(state) || hadUpdate;
		}

		if (hadUpdate) {
			updateKMean();
			clusterStep();
		}
	}

	private boolean findNearestKPoint(LearningState state) {
		double minDistance = Double.MAX_VALUE;
		kPoint minKPoint = null;
		for (kPoint kPoint : kPoints) {

			double currentDistance = state.distance(kPoint);
			if (currentDistance < minDistance) {
				minDistance = currentDistance;
				minKPoint = kPoint;

			}
		}

		return state.updateAssignedKPoint(minKPoint);
	}

	private void updateKMean() {
		for (kPoint kPoint : kPoints) {
			kPoint.getAssignedLearning();
			double[] average = new double[kPoint.getVektor().length];
			for (LearningState state : kPoint.getAssignedLearning()) {
				double[] sVektor = state.getVektor();

				for (int i = 0; i < average.length; i++) {
					average[i] += sVektor[i];
				}
			}
			for (int i = 0; i < average.length; i++) {
				average[i] = average[i] / kPoint.getAssignedLearning().size();
			}
			kPoint.setVektor(average);
		}
	}

	public kPoint[] getkPoints() {
		return kPoints;
	}

	public LearningState[] getStates() {
		return states;
	}

	public ArrayList<LearningState> getStatesByKPoint(int index) {
		return kPoints[index].getAssignedLearning();
	}

}
