/*
 *
 */
package oloboguara.socrates.core.exception;

/**
 * Exception class for handle Validation exceptions.
 * 
 * @author Diego Martins
 * @version 1.0
 * @date 17/09/2010 19:05:03
 * 
 */
public class EntityValidationException extends Exception {

	private static final long serialVersionUID = 7755980388423925578L;

	/**
	 * @param arg0
	 */
	public EntityValidationException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public EntityValidationException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public EntityValidationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
