package in.parapengu.spork.util;

import com.google.common.collect.Lists;
import in.parapengu.spork.Spork;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to log to the console
 */
public class Log {

	private static Logger log;

	private static boolean debug = false;

	static {
		Log.log = Spork.get().getLogger();
	}

	/**
	 * Logs to the console with the specified level enum and message
	 *
	 * @param level The {@link java.util.logging.Level} enum value
	 * @param messages The message(s) to print to the console
	 */
	public static void log(Level level, String... messages) {
		for(String s : messages)
			log.log(level, s);
	}

	/**
	 * Logs to the console using {@link java.util.logging.Level}.INFO
	 *
	 * @param messages The message(s) to print to the console
	 */
	public static void log(String... messages) {
		log(Level.INFO, messages);
	}

	/**
	 * Prints an exception to the console, shows only the simple name of the class and the message if debug mode is disabled,
	 *
	 * @param exception The exception to print to the console
	 */
	public static void log(Exception exception) {
		if(debug) {
			exception.printStackTrace();
		} else {
			Log.warning(exception.getClass().getSimpleName() + ": " + exception.getMessage());
		}
	}

	/**
	 * Prints an exception to the console, shows only the simple name of the class and the message if debug mode is disabled,
	 *
	 * @param exception The exception to print to the console
	 */
	public static void exception(Exception exception) {
		log(exception);
	}

	/**
	 * Logs to the console using {@link java.util.logging.Level}.INFO
	 *
	 * @param messages The message(s) to print to the console
	 */
	public static void info(String... messages) {
		log(messages);
	}

	/**
	 * Logs to the console using {@link java.util.logging.Level}.WARNING
	 *
	 * @param messages The message(s) to print to the console
	 */
	public static void warning(String... messages) {
		log(Level.WARNING, messages);
	}

	/**
	 * Logs to the console using {@link java.util.logging.Level}.SEVERE
	 *
	 * @param messages The message(s) to print to the console
	 */
	public static void severe(String... messages) {
		log(Level.SEVERE, messages);
	}

	/**
	 * Prints a stack trace to the console with the defined length, starting 2 elements in to the trace
	 * Trace only shows if debug mode is enabled
	 *
	 * @param length The length of the trace
	 */
	public static void debug(int length) {
		debug(2, length);
	}

	/**
	 * Prints a stack trace to the console with the defined start point and length
	 * Trace only shows if debug mode is enabled
	 *
	 * @param start The start point of the trace
	 * @param length The length of the trace
	 */
	public static void debug(int start, int length) {
		int end = start + length;
		List<StackTraceElement> trace = Lists.newArrayList(Thread.currentThread().getStackTrace());
		List<StackTraceElement> print = trace.subList(start, (trace.size() > end ? end : trace.size() - 1));
		for(StackTraceElement element : print) {
			debug(element.toString());
		}
	}

	/**
	 * Logs to the console using {@link java.util.logging.Level}.INFO with a prefix of {@code "[DEBUG]"}
	 * Message(s) only show if debug mode is enabled
	 *
	 * @param messages The message(s) to print to the console
	 */
	public static void debug(String... messages) {
		if(debug) {
			for(String message : messages) {
				log("[DEBUG] " + message);
			}
		}
	}

	/**
	 * Gets if debug mode is enabled
	 *
	 * @return true if debug mode is enabled, false otherwise
	 */
	public static boolean isDebug() {
		return debug;
	}

	/**
	 * Sets if debug mode should be enabled
	 *
	 * @param debug If debug mode should be enabled
	 */
	public static void setDebug(boolean debug) {
		Log.debug = debug;
		log((debug ? "Enabled" : "Disabled") + " debugging.");
	}

}