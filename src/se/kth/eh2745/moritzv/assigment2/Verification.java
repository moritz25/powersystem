package se.kth.eh2745.moritzv.assigment2;

import java.util.Collection;
import java.util.HashMap;

import se.kth.eh2745.moritzv.assigment2.db.MysqlConnector;
import se.kth.eh2745.moritzv.assigment2.exception.MysqlException;
import se.kth.eh2745.moritzv.assigment2.object.LearningState;
import se.kth.eh2745.moritzv.assigment2.object.MeasauredState;
import se.kth.eh2745.moritzv.assigment2.object.VerificationState;
import se.kth.eh2745.moritzv.assigment2.object.kPoint;

public class Verification {

	protected LearningState[] learningStates;
	protected int k;
	protected MysqlConnector con;

	public Verification(int k, LearningState[] learningStates) throws MysqlException {
		con = new MysqlConnector();
		this.learningStates = learningStates;
		this.k = k;

	}

	public String classify(VerificationState state) {
		LearningState[] nearest;

		nearest = findNearestK(state);
		kPoint classOfState = findMostCommonClass(nearest);

		state.setAssignedKPoint(classOfState);
		return classOfState.getClassName();

	}

	public String classifyAll() throws MysqlException {
		Collection<VerificationState> states = con.getAllTestStates();
		String classes = "";
		for (VerificationState completeState : states) {
			classes += completeState.getTime() + ": " + classify(completeState)   + "\n";
		}
		return classes;
	}

	private kPoint findMostCommonClass(LearningState[] nearest) {
		HashMap<kPoint, Integer> names = new HashMap<kPoint, Integer>();
		for (LearningState near : nearest) {
			kPoint kPoint = near.getAssignedKPoint();
			if (names.get(kPoint) == null) {
				names.put(kPoint, 1);
			} else {
				names.put(kPoint, names.get(kPoint) + 1);
			}
		}
		int maxHits = 0;
		for (kPoint kPoint : names.keySet()) {
			if (names.get(kPoint) > maxHits) {
				maxHits = names.get(kPoint);
			}
		}
		kPoint classOfState = null;
		for (kPoint kPoint : names.keySet()) {
			if (names.get(kPoint) == maxHits) {
				if (classOfState == null) {
					classOfState = kPoint;
				} else {
					System.err.println("Can't decide for this state, has serveral Classes with same count.");
					System.err.println("Possible classes" + classOfState + ", " + kPoint);
				}
			}
		}

		return classOfState;
	}

	private LearningState[] findNearestK(MeasauredState state) {
		LearningState[] nearest = new LearningState[k];
		for (LearningState learning : learningStates) {
			for (int i = 0; i < nearest.length; i++) {

				if (state.distance(learning) < state.distance(nearest[i])) {
					nearest[i] = learning;
					break;
				}
			}

		}
		return nearest;
	}
}
