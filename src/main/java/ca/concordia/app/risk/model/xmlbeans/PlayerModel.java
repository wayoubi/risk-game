
package ca.concordia.app.risk.model.xmlbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for player complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="player">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cardExchangeCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="reinforcementNoOfArmies" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="color">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="BLACK"/>
 *               &lt;enumeration value="RED"/>
 *               &lt;enumeration value="GREEN"/>
 *               &lt;enumeration value="YELLOW"/>
 *               &lt;enumeration value="BLUE"/>
 *               &lt;enumeration value="MAGENTA"/>
 *               &lt;enumeration value="CYAN"/>
 *               &lt;enumeration value="WHITE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "player", namespace = "http://ca.concordia.app.risk/game", propOrder = { "id", "name",
		"cardExchangeCount", "reinforcementNoOfArmies", "color" })
public class PlayerModel {

	@XmlElement(namespace = "http://ca.concordia.app.risk/game")
	protected int id;
	@XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
	protected String name;
	@XmlElement(namespace = "http://ca.concordia.app.risk/game")
	protected int cardExchangeCount;
	@XmlElement(namespace = "http://ca.concordia.app.risk/game")
	protected int reinforcementNoOfArmies;
	@XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
	protected String color;

	/**
	 * Gets the value of the id property.
	 * 
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 */
	public void setId(int value) {
		this.id = value;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the cardExchangeCount property.
	 * 
	 */
	public int getCardExchangeCount() {
		return cardExchangeCount;
	}

	/**
	 * Sets the value of the cardExchangeCount property.
	 * 
	 */
	public void setCardExchangeCount(int value) {
		this.cardExchangeCount = value;
	}

	/**
	 * Gets the value of the reinforcementNoOfArmies property.
	 * 
	 */
	public int getReinforcementNoOfArmies() {
		return reinforcementNoOfArmies;
	}

	/**
	 * Sets the value of the reinforcementNoOfArmies property.
	 * 
	 */
	public void setReinforcementNoOfArmies(int value) {
		this.reinforcementNoOfArmies = value;
	}

	/**
	 * Gets the value of the color property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the value of the color property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setColor(String value) {
		this.color = value;
	}

}
