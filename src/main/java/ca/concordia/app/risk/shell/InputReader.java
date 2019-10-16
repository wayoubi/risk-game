package ca.concordia.app.risk.shell;

import org.jline.reader.LineReader;
import org.springframework.util.StringUtils;

/**
 * 
 * @author i857625
 *
 */
public class InputReader {

	/**
	 * 
	 */
	public static final Character DEFAULT_MASK = '*';

	/**
	 * 
	 */
	private Character mask;

	/**
	 * 
	 */
	private LineReader lineReader;

	/**
	 * 
	 * @param lineReader
	 * line reader
	 */
	public InputReader(LineReader lineReader) {
		this(lineReader, null);
	}

	/**
	 * 
	 * @param lineReader
	 * line reader
	 * @param mask
	 * mask
	 */
	public InputReader(LineReader lineReader, Character mask) {
		this.lineReader = lineReader;
		this.mask = mask != null ? mask : DEFAULT_MASK;
	}

	/**
	 * 
	 * @param prompt
	 * prompt
	 * @return 
	 * result
	 */
	public String prompt(String prompt) {
		return prompt(prompt, null, true);
	}

	/**
	 * 
	 * @param prompt
	 * prompt
	 * @param defaultValue
	 * default value
	 * @return 
	 * result
	 */
	public String prompt(String prompt, String defaultValue) {
		return prompt(prompt, defaultValue, true);
	}

	/**
	 * 
	 * @param prompt
	 * prompt
	 * @param defaultValue
	 * default value
	 * @param echo
	 * echo
	 * @return
	 * result
	 */
	public String prompt(String prompt, String defaultValue, boolean echo) {

		String answer = "";
		if (echo) {
			answer = lineReader.readLine(prompt + ": ");
		} else {
			answer = lineReader.readLine(prompt + ": ", mask);
		}
		if (StringUtils.isEmpty(answer)) {
			return defaultValue;
		}
		return answer;
	}
}