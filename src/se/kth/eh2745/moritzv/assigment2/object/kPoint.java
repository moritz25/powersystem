package se.kth.eh2745.moritzv.assigment2.object;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class kPoint extends State implements Comparable<kPoint> {

	protected double[] vektor;
	protected ArrayList<LearningState> assignedLearningStates;
	protected ArrayList<VerificationState> assignedTestStates;
	protected String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setClassName(SystemClasses sysClass) {
		this.className = sysClass.toString();
	}

	public kPoint(double[] vektor) {
		super();
		this.vektor = vektor;
		assignedLearningStates = new ArrayList<LearningState>();
		assignedTestStates = new ArrayList<VerificationState>();
	}

	@Override
	public double[] getVektor() {

		return vektor;
	}

	public void setVektor(double[] vektor) {
		this.vektor = vektor;
	}

	public void assignState(LearningState state) {
		assignedLearningStates.add(state);
	}

	public boolean isAssigned(LearningState state) {
		return assignedLearningStates.contains(state);
	}

	public void removeAssigned(LearningState state) {
		assignedLearningStates.remove(state);
	}

	public ArrayList<LearningState> getAssignedLearning() {
		Collections.sort(assignedLearningStates);
		return assignedLearningStates;
	}

	public void assignTestState(VerificationState testState) {
		assignedTestStates.add(testState);

	}

	public ArrayList<VerificationState> getAssignedTesting() {
		Collections.sort(assignedTestStates);
		return assignedTestStates;
	}

	public ArrayList<? extends MeasauredState> getAssigned(boolean learning) {
		if (learning) {
			Collections.sort(assignedLearningStates);
			return assignedLearningStates;
		} else {
			Collections.sort(assignedTestStates);
			return assignedTestStates;
		}
	}

	public double getAverageDistanceOfAssigned() {
		double summedDistance = 0;
		for (LearningState completeState : assignedLearningStates) {
			summedDistance += distance(completeState);
		}
		return summedDistance / assignedLearningStates.size();
	}

	public Integer[] getLearningTimes() {
		Collections.sort(assignedLearningStates);
		Integer[] stateTimes = new Integer[assignedLearningStates.size()];
		for (int i = 0; i < stateTimes.length; i++) {
			stateTimes[i] = assignedLearningStates.get(i).getTime();
		}
		return stateTimes;
	}

	public String getLearningTimesString() {
		Collections.sort(assignedLearningStates);
		String stateNames = "";
		int i = 0;
		for (LearningState completeState : assignedLearningStates) {
			if (++i > 10) {
				stateNames += ", ";
				i = 0;
			} else {
				stateNames += ", ";
			}
			stateNames += completeState.getTime();
		}
		if (stateNames.equals("")) {
			return "-";
		} else {
			return stateNames.substring(2);
		}
	}

	public Integer[] getVerificationTimes() {
		Collections.sort(assignedTestStates);
		Integer[] stateTimes = new Integer[assignedTestStates.size()];
		for (int i = 0; i < stateTimes.length; i++) {
			stateTimes[i] = assignedTestStates.get(i).getTime();
		}
		return stateTimes;
	}

	public String getVerificationTimesString() {
		Collections.sort(assignedTestStates);
		String stateNames = "";
		int i = 0;
		for (VerificationState completeState : assignedTestStates) {
			if (++i > 10) {
				stateNames += ", ";
				i = 0;
			} else {
				stateNames += ", ";
			}
			stateNames += completeState.getTime();
		}
		if (stateNames.equals("")) {
			return "-";
		} else {
			return stateNames.substring(2);
		}
	}

	@Override
	public String toString() {

		return "kPoint [assignedStates=" + getLearningTimesString() + ", vektor=" + Arrays.toString(vektor) + "]";
	}

	@Override
	public int compareTo(kPoint o) {
		if (this.getAssignedLearning().size() == 0) {
			if (o.getAssignedLearning().size() == 0) {
				return 0;
			} else {
				return 1;
			}
		}
		if (o.getAssignedLearning().size() == 0) {
			return -1;
		}
		return this.getAssignedLearning().get(0).compareTo(o.getAssignedLearning().get(0));
	}

	public SystemClasses autoAssign() {
		int[] hits = new int[SystemClasses.values().length];
		for (int i = 0; i < hits.length; i++) {
			for (LearningState state : getAssignedLearning()) {
				if (SystemClasses.values()[i].getAutoTimes().contains(state.time)) {
					++hits[i];
				}
			}
		}
		int max_hits = 0;
		int max_index = 0;
		for (int i = 0; i < hits.length; i++) {
			if (hits[i] > max_hits) {
				max_hits = hits[i];
				max_index = i;

			}

		}
		this.setClassName(SystemClasses.values()[max_index]);
		return SystemClasses.values()[max_index];

	}

}
