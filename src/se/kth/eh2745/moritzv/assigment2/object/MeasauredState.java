package se.kth.eh2745.moritzv.assigment2.object;

import java.util.ArrayList;

public abstract class MeasauredState extends State implements Comparable<MeasauredState> {

	protected int time;
	protected ArrayList<Measurement> measurements;
	protected static ArrayList<String> ordering;
	protected kPoint assignedKPoint;

	public MeasauredState(int time) {
		super();
		this.time = time;
		this.measurements = new ArrayList<Measurement>();
		ordering = new ArrayList<String>();

	}

	public static ArrayList<String> getOrdering() {
		return ordering;
	}

	public int getTime() {
		return time;
	}

	public ArrayList<Measurement> getMeasurements() {
		return measurements;
	}

	public Measurement getMeasurement(String name) {

		return measurements.get(ordering.indexOf(name));
	}

	public kPoint getAssignedKPoint() {

		return assignedKPoint;
	}

	public MeasauredState addMeasurement(Measurement measurement) {
		if (!ordering.contains(measurement.getName())) {
			ordering.add(measurement.getName());
		}
		this.measurements.add(ordering.indexOf(measurement.getName()), measurement);
		return this;
	}

	public double[] getVektor() {
		double[] vektor = new double[measurements.size()];
		for (int i = 0; i < vektor.length; i++) {
			vektor[i] = measurements.get(i).getValue();
		}
		return vektor;
	}

	@Override
	public String toString() {
		// return "CompleteState [time=" + time + ", getVektor()=" +
		// Arrays.toString(getVektor()) + "]";
		if (getAssignedKPoint() == null) {
			return "CompleteState [time=" + time + "]";
		} else {
			return "CompleteState [time=" + time + ", class=" + getAssignedKPoint().getClassName() + "]";
		}
	}

	@Override
	public LearningState clone() throws CloneNotSupportedException {
		LearningState newObject = (LearningState) super.clone();
		newObject.measurements.clone();
		return newObject;
	}

	@Override
	public int compareTo(MeasauredState compareObject) {
		if (time < compareObject.time)
			return -1;
		else if (time == compareObject.time)
			return 0;
		else
			return 1;
	}

}