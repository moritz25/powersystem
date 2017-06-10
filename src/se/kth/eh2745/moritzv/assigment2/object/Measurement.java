package se.kth.eh2745.moritzv.assigment2.object;

import java.util.HashMap;

import com.wphooper.number.Complex;

import se.kth.eh2745.moritzv.assigment2.Config;
import se.kth.eh2745.moritzv.assigment2.db.MysqlConnector;
import se.kth.eh2745.moritzv.assigment2.exception.MysqlException;

public class Measurement {
	protected int time;
	protected String name;
	protected double value;
	static protected HashMap<String, Double> minimal;
	static protected HashMap<String, Double> maximal;

	public Measurement(String name, int time, double value) {
		if (Config.normalized) {
			if (minimal == null) {
				minimal = new HashMap<String, Double>();
				maximal = new HashMap<String, Double>();
			}

			if (minimal.get(name) == null) {
				minimal.put(name, value);
				maximal.put(name, value);
			} else {
				if (minimal.get(name) > value) {
					minimal.put(name, value);
				}
				if (maximal.get(name) < value) {
					maximal.put(name, value);
				}
			}
		}

		this.time = time;
		this.name = name;
		this.value = value;
	}

	public int getTime() {
		return time;
	}

	public String getName() {
		return name;
	}

	public double getValue() {
		return getValue(true);
	}

	public double getValue(boolean normalized) {
		if (normalized && Config.normalized) {
			if (minimal.get(name) + maximal.get(name) == 0) {
				return 0;
			}
			return (value - minimal.get(name)) / (minimal.get(name) + maximal.get(name));

		} else {
			return value;
		}

	}

	@Override
	public String toString() {
		return "Measurement [time=" + time + ", name=" + name + ", value=" + value + "]";
	}

	public static Complex getComplex(String name, int t, boolean normalized, boolean learning) {
		MysqlConnector con = new MysqlConnector();
		try {
			double volt = con.getMeasurmentsForTAndName(t, name + "_VOLT", learning).getValue(normalized);
			double ang = con.getMeasurmentsForTAndName(t, name + "_ANG", learning).getValue(normalized);
			return Complex.fromPolar(volt, ang);
		} catch (MysqlException e) {
			e.printStackTrace();
			return new Complex(0);
		}

	}

}
