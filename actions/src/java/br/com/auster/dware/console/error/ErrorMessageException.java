package br.com.auster.dware.console.error;

public class ErrorMessageException extends RuntimeException {
	private static final long serialVersionUID = 7232735666673277422L;

	public ErrorMessageException() {
		super();
	}
	
	public ErrorMessageException(String msg) {
		super(msg);
	}
	
	public ErrorMessageException(Throwable t) {
		super(t);
	}
	
	public ErrorMessageException(String msg, Throwable t) {
		super(msg, t);
	}
}