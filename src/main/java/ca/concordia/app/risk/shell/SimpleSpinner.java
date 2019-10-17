package ca.concordia.app.risk.shell;

import org.jline.terminal.Terminal;

/**
 * 
 * @author i857625
 * 
 *
 */
public class SimpleSpinner {

	/**
	 * 
	 */
	private static final String CUU = "\u001B[A";

	/**
	 *
	 */
	private Terminal terminal;

	/**
	 * 
	 */
	private char[] spinner = { '|', '/', '-', '\\' };

	/**
	 * 
	 */
	private String pattern = " %s: %d ";

	/**
	 * 
	 */
	private int spinCounter = 0;

	/**
	 * 
	 */
	private boolean started;

	/**
	 * 
	 * @param terminal
	 */
	public SimpleSpinner(Terminal terminal) {
		this(terminal, null);
	}

	/**
	 * 
	 * @param terminal
	 * @param pattern
	 */
	public SimpleSpinner(Terminal terminal, String pattern) {
		this(terminal, pattern, null);
	}

	/**
	 * 
	 * @param terminal
	 * @param pattern
	 * @param spinner
	 */
	public SimpleSpinner(Terminal terminal, String pattern, char[] spinner) {
		this.terminal = terminal;

		if (pattern != null) {
			this.pattern = pattern;
		}
		if (spinner != null) {
			this.spinner = spinner;
		}
	}

	/**
	 * 
	 * @param count
	 * @param message
	 */
	public void display(int count, String message) {
		if (!started) {
			terminal.writer().println();
			started = true;
		}
		String progress = String.format(pattern, message, count);

		terminal.writer().println(CUU + "\r" + getSpinnerChar() + progress);
		terminal.flush();
	}

	/**
	 * 
	 */
	public void display() {
		if (!started) {
			terminal.writer().println();
			started = true;
		}
		terminal.writer().println(CUU + "\r" + getSpinnerChar());
		terminal.flush();
	}

	public void reset() {
		spinCounter = 0;
		started = false;
	}

	private char getSpinnerChar() {
		char spinChar = spinner[spinCounter];
		spinCounter++;
		if (spinCounter == spinner.length) {
			spinCounter = 0;
		}
		return spinChar;
	}

	public char[] getSpinner() {
		return spinner;
	}

	public void setSpinner(char[] spinner) {
		this.spinner = spinner;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}