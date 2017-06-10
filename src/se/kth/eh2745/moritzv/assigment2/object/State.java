package se.kth.eh2745.moritzv.assigment2.object;

public abstract class State {

	public State() {
		super();
	}

	public abstract double[] getVektor();

	public double distance(State other) {
		if (other == null) {
			return Double.MAX_VALUE;
		}
		double[] v1 = getVektor();
		double[] v2 = other.getVektor();
		double distance_squared = 0;
		for (int i = 0; i < v1.length; i++) {
			distance_squared += (v1[i] - v2[i]) * (v1[i] - v2[i]);
		}
		return Math.sqrt(distance_squared);
	}

}