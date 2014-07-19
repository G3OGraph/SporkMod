package in.parapengu.spork.exception.map;

import in.parapengu.spork.map.MapLoader;

import java.io.File;

public class MapCreateException extends Exception {

	private static final long serialVersionUID = 1482787630528188112L;

	private String reason;
	private Exception exception;

	public MapCreateException(MapLoader loader, String reason) {
		super("Failed to load " + loader.getName() + ": " + reason);
		this.reason = reason;
	}

	public MapCreateException(MapLoader loader, Exception exception) {
		this(loader, exception.getClass().getSimpleName() + (exception.getMessage() == null || exception.getMessage().length() <= 0 ? "" : ": " + exception.getMessage()));
		this.exception = exception;
	}

	public MapCreateException(File folder, String reason) {
		super("Failed to load " + folder.getName() + ": " + reason);
		this.reason = reason;
	}

	public MapCreateException(File folder, Exception exception) {
		this(folder, exception.getClass().getSimpleName() + (exception.getMessage() == null || exception.getMessage().length() <= 0 ? "" : ": " + exception.getMessage()));
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
