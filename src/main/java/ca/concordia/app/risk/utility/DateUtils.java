package ca.concordia.app.risk.utility;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


public class DateUtils {
	
	public static XMLGregorianCalendar getXMLDateTime(Date date) throws DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date());
		XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
		return xmlGregorianCalendar;	
	}
}
