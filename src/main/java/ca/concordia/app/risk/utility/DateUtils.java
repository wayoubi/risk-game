package ca.concordia.app.risk.utility;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 
 * @author i857625
 *
 */
public class DateUtils {

	/**
	 * 
	 */
	private DateUtils() {

	}

	/**
	 * 
	 * @param date
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	public static XMLGregorianCalendar getXMLDateTime(Date date) throws DatatypeConfigurationException {
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		gregorianCalendar.setTime(date);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	}
}