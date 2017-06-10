package se.kth.eh2745.moritzv.assigment2.object;

public class VerificationState extends MeasauredState {

	public VerificationState(int time) {
		super(time);
	}

	public void setAssignedKPoint(kPoint kPoint) {

		assignedKPoint = kPoint;
		kPoint.assignTestState(this);

	}
}
