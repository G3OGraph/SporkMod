package in.parapengu.spork.exception.region;

import in.parapengu.spork.module.Module;

public class ModuleParsingException extends Exception {

	private static final long serialVersionUID = -2057908058647206000L;

	private String reason;
	private Exception exception;

	public ModuleParsingException(Class<? extends Module> module, String reason) {
		super("Failed to load " + module.getSimpleName() + ": " + reason);
		this.reason = reason;
	}

	public ModuleParsingException(Class<? extends Module> module, Exception exception) {
		this(module, exception.getClass().getSimpleName() + (exception.getMessage() == null || exception.getMessage().length() <= 0 ? "" : ": " + exception.getMessage()));
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
