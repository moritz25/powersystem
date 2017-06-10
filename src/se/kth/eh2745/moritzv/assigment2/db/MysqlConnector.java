package se.kth.eh2745.moritzv.assigment2.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import se.kth.eh2745.moritzv.assigment2.Config;
import se.kth.eh2745.moritzv.assigment2.exception.ConnectionFailedException;
import se.kth.eh2745.moritzv.assigment2.exception.MysqlException;
import se.kth.eh2745.moritzv.assigment2.object.LearningState;
import se.kth.eh2745.moritzv.assigment2.object.Measurement;
import se.kth.eh2745.moritzv.assigment2.object.VerificationState;

public class MysqlConnector {

	static Connection conn = null;

	private Connection getConn() throws ConnectionFailedException {
		if (conn == null) {

			makeConnection(Config.dbName);

		}
		return conn;
	}

	private void makeConnection(String dbName) throws ConnectionFailedException {
		try {
			Class.forName(Config.JDBC_DRIVER);

			conn = DriverManager.getConnection(Config.dbUrl + dbName, Config.user, Config.pass);

		} catch (Exception e) {
			throw new ConnectionFailedException(e);
		}
	}

	public ArrayList<Measurement> getAllMeasurments(String table) throws MysqlException {
		try {

			String sql = "SELECT name,time,value FROM `" + table + "` ";
			PreparedStatement selectSt = getConn().prepareStatement(sql);

			ResultSet queryResult = selectSt.executeQuery();
			ArrayList<Measurement> measurments = new ArrayList<Measurement>();
			while (queryResult.next()) {
				measurments.add(new Measurement(queryResult.getString("name"), queryResult.getInt("time"),
						queryResult.getDouble("value")));

			}
			return measurments;

		} catch (SQLException e) {
			throw new MysqlException(e);
		}
	}

	public Measurement getMeasurmentsForTAndName(int time, String name, boolean learning) throws MysqlException {
		try {
			String tableName;
			if (learning) {
				tableName = Config.learningTable;
			} else {
				tableName = Config.verificationTable;
			}
			String sql = "SELECT name,time,value FROM  " + tableName + " WHERE time = ?  and name = ?";

			PreparedStatement selectSt = getConn().prepareStatement(sql);
			selectSt.setInt(1, time);

			selectSt.setString(2, name);
			ResultSet queryResult = selectSt.executeQuery();
			queryResult.next();
			return new Measurement(queryResult.getString("name"), queryResult.getInt("time"),
					queryResult.getDouble("value"));

		} catch (SQLException e) {
			throw new MysqlException(e);
		}
	}

	public ArrayList<Integer> getLearningTimes() throws MysqlException {
		try {

			String sql = "SELECT time FROM  " + Config.learningTable + "  GROUP BY time ORDER BY Time ASC";

			PreparedStatement selectSt = getConn().prepareStatement(sql);

			ResultSet queryResult = selectSt.executeQuery();

			ArrayList<Integer> times = new ArrayList<Integer>();
			while (queryResult.next()) {
				times.add(queryResult.getInt("time"));

			}
			return times;

		} catch (SQLException e) {
			throw new MysqlException(e);
		}
	}

	public Integer[] getLearningTimesAsArray() {
		try {

			return getLearningTimes().toArray(new Integer[0]);

		} catch (Exception e) {
			return new Integer[1];
		}
	}

	public ArrayList<Integer> getVerificationTimes() throws MysqlException {
		try {

			String sql = "SELECT time FROM  " + Config.verificationTable + "  GROUP BY time ORDER BY Time ASC";

			PreparedStatement selectSt = getConn().prepareStatement(sql);

			ResultSet queryResult = selectSt.executeQuery();

			ArrayList<Integer> times = new ArrayList<Integer>();
			while (queryResult.next()) {
				times.add(queryResult.getInt("time"));

			}
			return times;

		} catch (SQLException e) {
			throw new MysqlException(e);
		}
	}

	public Integer[] getVerificationTimesAsArray() {
		try {

			return getVerificationTimes().toArray(new Integer[0]);

		} catch (Exception e) {
			return new Integer[1];
		}
	}

	public Collection<LearningState> getAllLeariningStates() throws MysqlException {

		HashMap<Integer, LearningState> states = new HashMap<Integer, LearningState>();
		for (Measurement measurement : getAllMeasurments(Config.learningTable)) {
			if (!states.containsKey(measurement.getTime())) {
				states.put(measurement.getTime(), new LearningState(measurement.getTime()));
			}
			states.get(measurement.getTime()).addMeasurement(measurement);
		}
		return states.values();

	}

	public Collection<VerificationState> getAllTestStates() throws MysqlException {

		HashMap<Integer, VerificationState> states = new HashMap<Integer, VerificationState>();
		for (Measurement measurement : getAllMeasurments(Config.verificationTable)) {
			if (!states.containsKey(measurement.getTime())) {
				states.put(measurement.getTime(), new VerificationState(measurement.getTime()));
			}
			states.get(measurement.getTime()).addMeasurement(measurement);
		}
		return states.values();

	}

	public double getAverage(int time, String type, String table) throws MysqlException {

		try {

			String sql = "SELECT AVG(value) FROM " + table + " WHERE time=? and name LIKE ?  GROUP BY time ";

			PreparedStatement selectSt = getConn().prepareStatement(sql);
			selectSt.setInt(1, time);

			selectSt.setString(2, "%_" + type);
			ResultSet queryResult = selectSt.executeQuery();
			queryResult.next();

			return queryResult.getDouble(1);

		} catch (SQLException e) {
			throw new MysqlException(e);
		}

	}

}
