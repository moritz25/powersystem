package se.kth.eh2745.moritzv.assigment2.exception;

public class DatabaseCreationFailedException extends MysqlException {

	private static final long serialVersionUID = 4012889246317495302L;

	public DatabaseCreationFailedException(Throwable cause) {
		super(cause);
	}

}
