package ca.concordia.app.risk.shell;

import org.jline.reader.LineReader;
import org.springframework.util.StringUtils;

/**
 * @author i857625
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
     * @param lineReader
     */
    public InputReader(LineReader lineReader) {
        this(lineReader, null);
    }

    /**
     * @param lineReader
     * @param mask
     */
    public InputReader(LineReader lineReader, Character mask) {
        this.lineReader = lineReader;
        this.mask = mask != null ? mask : DEFAULT_MASK;
    }

    /**
     * @param prompt
     * @return
     */
    public String prompt(String prompt) {
        return prompt(prompt, null, true);
    }

    /**
     * @param prompt
     * @param defaultValue
     * @return
     */
    public String prompt(String prompt, String defaultValue) {
        return prompt(prompt, defaultValue, true);
    }

    /**
     * @param prompt
     * @param defaultValue
     * @param echo
     * @return
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