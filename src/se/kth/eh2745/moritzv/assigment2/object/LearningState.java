package se.kth.eh2745.moritzv.assigment2.object;

public class LearningState extends MeasauredState implements Cloneable {

	public LearningState(int time) {
		super(time);
	}

	public LearningState addMeasurement(Measurement measurement) {
		return (LearningState) super.addMeasurement(measurement);
	}
	public boolean updateAssignedKPoint(kPoint kPoint) {
		if (!kPoint.equals(assignedKPoint)) {
			if (assignedKPoint != null) {
				assignedKPoint.removeAssigned(this);
			}
			assignedKPoint = kPoint;
			kPoint.assignState(this);
			return true;
		}
		return false;
	}

}
