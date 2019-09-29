package ca.concordia.app.risk.model.xmlbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for country complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="country">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numberOfArmies" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="playerId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="continentId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "country", namespace = "http://ca.concordia.app.risk/game", propOrder = { "id", "name",
		"numberOfArmies", "playerId", "continentId" })
public class CountryModel {

	@XmlElement(namespace = "http://ca.concordia.app.risk/game")
	protected int id;
	@XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
	protected String name;
	@XmlElement(namespace = "http://ca.concordia.app.risk/game")
	protected int numberOfArmies;
	@XmlElement(namespace = "http://ca.concordia.app.risk/game")
	protected int playerId;
	@XmlElement(namespace = "http://ca.concordia.app.risk/game")
	protected int continentId;

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
	 * Gets the value of the numberOfArmies property.
	 * 
	 */
	public int getNumberOfArmies() {
		return numberOfArmies;
	}

	/**
	 * Sets the value of the numberOfArmies property.
	 * 
	 */
	public void setNumberOfArmies(int value) {
		this.numberOfArmies = value;
	}

	/**
	 * Gets the value of the playerId property.
	 * 
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * Sets the value of the playerId property.
	 * 
	 */
	public void setPlayerId(int value) {
		this.playerId = value;
	}

	/**
	 * Gets the value of the continentId property.
	 * 
	 */
	public int getContinentId() {
		return continentId;
	}

	/**
	 * Sets the value of the continentId property.
	 * 
	 */
	public void setContinentId(int value) {
		this.continentId = value;
	}

}
