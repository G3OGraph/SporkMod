package in.parapengu.spork.exception.rotation;

import java.io.File;

public class RotationLoadException extends Exception {

	private static final long serialVersionUID = 1482787630528188112L;

	private String reason;
	private Exception exception;

	public RotationLoadException(File file, String reason) {
		super("Failed to load rotation from " + file.getName() + ": " + reason);
		this.reason = reason;
	}

	public RotationLoadException(File file, Exception exception) {
		this(file, exception.getClass().getSimpleName() + (exception.getMessage() == null || exception.getMessage().length() <= 0 ? "" : ": " + exception.getMessage()));
		this.exception = exception;
	}

	public String getReason() {
		return reason;
	}

	@Override
	public void printStackTrace() {
		if(exception != null) {
			System.out.println(getMessage());
			exception.printStackTrace();
		} else {
			super.printStackTrace();
		}
	}

}
