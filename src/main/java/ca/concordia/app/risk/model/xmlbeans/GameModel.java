
package ca.concordia.app.risk.model.xmlbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="autoSave" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="createdDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="lastSavedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="players" type="{http://ca.concordia.app.risk/game}players"/>
 *         &lt;element name="contenents" type="{http://ca.concordia.app.risk/game}contenents"/>
 *         &lt;element name="countries" type="{http://ca.concordia.app.risk/game}countries"/>
 *         &lt;element name="borders" type="{http://ca.concordia.app.risk/game}borders"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "autoSave",
    "createdDate",
    "lastSavedDate",
    "players",
    "contenents",
    "countries",
    "borders"
})
@XmlRootElement(name = "game", namespace = "http://ca.concordia.app.risk/game")
public class GameModel {

    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean autoSave;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDate;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastSavedDate;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected PlayersModel players;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected ContenentsModel contenents;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected CountriesModel countries;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected BordersModel borders;

    /**
     * Gets the value of the autoSave property.
     * 
     */
    public boolean isAutoSave() {
        return autoSave;
    }

    /**
     * Sets the value of the autoSave property.
     * 
     */
    public void setAutoSave(boolean value) {
        this.autoSave = value;
    }

    /**
     * Gets the value of the createdDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedDate() {
        return createdDate;
    }

    /**
     * Sets the value of the createdDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedDate(XMLGregorianCalendar value) {
        this.createdDate = value;
    }

    /**
     * Gets the value of the lastSavedDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastSavedDate() {
        return lastSavedDate;
    }

    /**
     * Sets the value of the lastSavedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastSavedDate(XMLGregorianCalendar value) {
        this.lastSavedDate = value;
    }

    /**
     * Gets the value of the players property.
     * 
     * @return
     *     possible object is
     *     {@link PlayersModel }
     *     
     */
    public PlayersModel getPlayers() {
        return players;
    }

    /**
     * Sets the value of the players property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlayersModel }
     *     
     */
    public void setPlayers(PlayersModel value) {
        this.players = value;
    }

    /**
     * Gets the value of the contenents property.
     * 
     * @return
     *     possible object is
     *     {@link ContenentsModel }
     *     
     */
    public ContenentsModel getContenents() {
        return contenents;
    }

    /**
     * Sets the value of the contenents property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContenentsModel }
     *     
     */
    public void setContenents(ContenentsModel value) {
        this.contenents = value;
    }

    /**
     * Gets the value of the countries property.
     * 
     * @return
     *     possible object is
     *     {@link CountriesModel }
     *     
     */
    public CountriesModel getCountries() {
        return countries;
    }

    /**
     * Sets the value of the countries property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountriesModel }
     *     
     */
    public void setCountries(CountriesModel value) {
        this.countries = value;
    }

    /**
     * Gets the value of the borders property.
     * 
     * @return
     *     possible object is
     *     {@link BordersModel }
     *     
     */
    public BordersModel getBorders() {
        return borders;
    }

    /**
     * Sets the value of the borders property.
     * 
     * @param value
     *     allowed object is
     *     {@link BordersModel }
     *     
     */
    public void setBorders(BordersModel value) {
        this.borders = value;
    }

}
