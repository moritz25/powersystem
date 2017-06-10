package se.kth.eh2745.moritzv.assigment2;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.w3c.dom.Document;

import se.kth.eh2745.moritzv.assigment2.run;
import se.kth.eh2745.moritzv.assigment2.db.MysqlConnector;
import se.kth.eh2745.moritzv.assigment2.exception.ConfigLoadFailedException;
import se.kth.eh2745.moritzv.assigment2.object.LearningState;
import se.kth.eh2745.moritzv.assigment2.object.kPoint;

public class Config {
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	public static String dbUrl = "jdbc:mysql://localhost/";
	// Database credentials
	public static String user;
	public static String pass;
	public static String dbName;
	public static boolean verbose = false;
	public static int starting;
	public static int kcluster;
	public static String verificationTable;
	public static String learningTable;
	public static boolean normalized = true;
	private static MysqlConnector con;
	private static Cluster cluster;

	public static Options defineOptions() {
		Options options = new Options();

		options.addOption("v", "verbose", false, "Be verbose");
		options.addOption("P", "pass", true, "Password for Mysql (overrids config.xml)");
		options.addOption("U", "user", true, "User for Mysql (overrids config.xml)");
		options.addOption("D", "db-name", true, "Database Name for Mysql (overrids config.xml)");
		options.addOption("L", "db-url", true, "Database Url for Mysql (overrids config.xml)");
		options.addOption("N", "no-config", false, "Don't load config.xml");

		options.addOption("c", "cluster", false, "Cluster Data");
		options.addOption("S", "show-cluster", false, "Show clustered Data");
		options.addOption("s", "startingpoints", true,
				"# of different Starting Points are compared for the optimal Clustering");
		options.addOption("k", "k-cluster", true, "# of Points for Clustering");
		options.addOption("C", "cluster-compare", true, "Compare different k, going up until <arg>");
		options.addOption("l", "cluster-table", true, "Table name for cluster learning Data");
		options.addOption("a", "auto-assign", false, "Automaticaly assign Labels (works only with special Dataset)");
		options.addOption("t", "test", false, "Verificate data");
		options.addOption("T", "test-table", true, "Table name for Data for Verification");

		options.addOption("g", "gui", false, "Start GUI");

		options.addOption("h", "help", false, "Print this help");

		return options;
	}

	public static void parseArgs(Options options, String[] args) throws Exception {
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("verbose")) {
				Config.verbose = true;
			}
			if (!cmd.hasOption("no-config")) {
				Config.loadXML();
			}
			if (cmd.hasOption("user")) {
				Config.user = cmd.getOptionValue('u');
			}
			if (cmd.hasOption("db-name")) {
				Config.dbName = cmd.getOptionValue("db-name");
			}
			if (cmd.hasOption("db-url")) {
				Config.dbUrl = cmd.getOptionValue("db-url");
			}
			if (cmd.hasOption("pass")) {
				Config.pass = cmd.getOptionValue('p');
			}

			if (cmd.hasOption("startingpoints")) {
				Config.starting = Integer.parseInt(cmd.getOptionValue("startingpoints"));
			} else {
				Config.starting = 50;
			}
			if (cmd.hasOption("k-cluster")) {
				Config.kcluster = Integer.parseInt(cmd.getOptionValue("k-cluster"));
			} else {
				Config.kcluster = 4;
			}
			if (cmd.hasOption("cluster-table")) {
				Config.learningTable = cmd.getOptionValue("cluster-table");
			} else {
				Config.learningTable = "measurements";
			}

			if (cmd.hasOption("neighbour-table")) {
				Config.verificationTable = cmd.getOptionValue("neighbour-table");
			} else {
				Config.verificationTable = "analog_values";
			}

			if (cmd.hasOption("cluster-compare")) {
				MysqlConnector con = new MysqlConnector();

				for (int i = 1; i < Integer.parseInt(cmd.getOptionValue("cluster-compare")); i++) {
					System.out.print("i: " + i + " ");
					Cluster.clusterStartComperator(Config.starting, i,
							con.getAllLeariningStates().toArray(new LearningState[0]));
				}

			}

			if (cmd.hasOption("cluster")) {
				con = new MysqlConnector();

				cluster = Cluster.clusterStartComperator(Config.starting, Config.kcluster,
						con.getAllLeariningStates().toArray(new LearningState[0]));

			}
			
			if (cmd.hasOption("a")) {
				for (kPoint kPoint : cluster.getkPoints()) {
					kPoint.autoAssign();
				}
			}

			if (cmd.hasOption("show-cluster")) {

				for (kPoint kPoint : cluster.getkPoints()) {

					System.out.println(kPoint.getClassName()+": "+ kPoint.getLearningTimesString());

				}

				/*
				 * for (CompleteState states : cluster.getStates()) {
				 * System.out.println(states); }
				 */

			}

			if (cmd.hasOption("test")) {

				con = new MysqlConnector();

				Verification neighbour = new Verification(3, cluster.getStates());
				System.out.println(neighbour.classifyAll());

			}


			if (cmd.hasOption("gui")) {
				run.startGui();
			}

			if (cmd.hasOption("h")) {
				printHelp(options);
			}
		} catch (ParseException | ConfigLoadFailedException e1) {

			e1.printStackTrace();
			printHelp(options);
			return;
		}

		if (args.length == 0) {
			printHelp(options);
		}
	}

	private static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("AdmittanceCalculator.jar", options);
	}

	public static void loadXML() throws ConfigLoadFailedException {
		try {

			File XmlFile = new File("config.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(XmlFile);

			dbUrl = doc.getElementsByTagName("db-url").item(0).getTextContent();
			user = doc.getElementsByTagName("db-user").item(0).getTextContent();
			pass = doc.getElementsByTagName("db-pass").item(0).getTextContent();
			dbName = doc.getElementsByTagName("db-name").item(0).getTextContent();

		} catch (Exception e) {
			System.err.println("Could not load config.xml, trying Commandline values");
		}
	}

}
