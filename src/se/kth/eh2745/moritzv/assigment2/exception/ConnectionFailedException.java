package se.kth.eh2745.moritzv.assigment2.exception;

public class ConnectionFailedException extends MysqlException {

	private static final long serialVersionUID = -642008257766299164L;

	public ConnectionFailedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ConnectionFailedException() {
		super();
	}
}
