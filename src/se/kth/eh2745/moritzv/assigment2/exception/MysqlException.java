package se.kth.eh2745.moritzv.assigment2.exception;

public class MysqlException extends Exception {

	private static final long serialVersionUID = -5443192998974490147L;

	public MysqlException(Throwable cause) {
		super(cause);
		
	}

	public MysqlException() {
	super();
	}
}
