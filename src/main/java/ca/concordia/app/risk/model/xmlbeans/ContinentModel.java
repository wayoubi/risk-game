
package ca.concordia.app.risk.model.xmlbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for continent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="continent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="controlValue" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numberOfCountries" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "continent", namespace = "http://ca.concordia.app.risk/game", propOrder = {
    "id",
    "name",
    "controlValue",
    "numberOfCountries"
})
public class ContinentModel {

    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected int id;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected String name;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected int controlValue;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected int numberOfCountries;

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
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the controlValue property.
     * 
     */
    public int getControlValue() {
        return controlValue;
    }

    /**
     * Sets the value of the controlValue property.
     * 
     */
    public void setControlValue(int value) {
        this.controlValue = value;
    }

    /**
     * Gets the value of the numberOfCountries property.
     * 
     */
    public int getNumberOfCountries() {
        return numberOfCountries;
    }

    /**
     * Sets the value of the numberOfCountries property.
     * 
     */
    public void setNumberOfCountries(int value) {
        this.numberOfCountries = value;
    }

}
