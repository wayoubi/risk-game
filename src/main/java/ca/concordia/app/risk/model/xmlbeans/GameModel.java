
package ca.concordia.app.risk.model.xmlbeans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
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
 *         &lt;element name="createdDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="lastSavedDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="savedPlayerId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="gamePlay" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="mapLoaded" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="countriesPopulated" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="reinforceCompleted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="attackerWin" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="defenderWin" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="attackCompleted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="cardGiven" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="cardExchangeCompleted" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="allOut" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="attackCountryNameFrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="attackCountryNameTo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numDiceAttacker" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="players" type="{http://ca.concordia.app.risk/game}players"/>
 *         &lt;element name="continents" type="{http://ca.concordia.app.risk/game}continents"/>
 *         &lt;element name="countries" type="{http://ca.concordia.app.risk/game}countries"/>
 *         &lt;element name="borders" type="{http://ca.concordia.app.risk/game}borders"/>
 *         &lt;element name="attackerDice" type="{http://ca.concordia.app.risk/game}valuelist"/>
 *         &lt;element name="defenderDice" type="{http://ca.concordia.app.risk/game}valuelist"/>
 *         &lt;element name="serilaizedGraph" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "createdDate",
    "lastSavedDate",
    "savedPlayerId",
    "gamePlay",
    "mapLoaded",
    "countriesPopulated",
    "reinforceCompleted",
    "attackerWin",
    "defenderWin",
    "attackCompleted",
    "cardGiven",
    "cardExchangeCompleted",
    "allOut",
    "attackCountryNameFrom",
    "attackCountryNameTo",
    "numDiceAttacker",
    "players",
    "continents",
    "countries",
    "borders",
    "attackerDice",
    "defenderDice",
    "serilaizedGraph"
})
@XmlRootElement(name = "game", namespace = "http://ca.concordia.app.risk/game")
public class GameModel {

    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdDate;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastSavedDate;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected int savedPlayerId;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean gamePlay;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean mapLoaded;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean countriesPopulated;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean reinforceCompleted;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean attackerWin;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean defenderWin;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean attackCompleted;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean cardGiven;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean cardExchangeCompleted;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected boolean allOut;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected String attackCountryNameFrom;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected String attackCountryNameTo;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected int numDiceAttacker;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected PlayersModel players;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected ContinentsModel continents;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected CountriesModel countries;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected BordersModel borders;
    @XmlList
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", type = Integer.class)
    protected List<Integer> attackerDice;
    @XmlList
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", type = Integer.class)
    protected List<Integer> defenderDice;
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", required = true)
    protected String serilaizedGraph;

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
     * Gets the value of the savedPlayerId property.
     * 
     */
    public int getSavedPlayerId() {
        return savedPlayerId;
    }

    /**
     * Sets the value of the savedPlayerId property.
     * 
     */
    public void setSavedPlayerId(int value) {
        this.savedPlayerId = value;
    }

    /**
     * Gets the value of the gamePlay property.
     * 
     */
    public boolean isGamePlay() {
        return gamePlay;
    }

    /**
     * Sets the value of the gamePlay property.
     * 
     */
    public void setGamePlay(boolean value) {
        this.gamePlay = value;
    }

    /**
     * Gets the value of the mapLoaded property.
     * 
     */
    public boolean isMapLoaded() {
        return mapLoaded;
    }

    /**
     * Sets the value of the mapLoaded property.
     * 
     */
    public void setMapLoaded(boolean value) {
        this.mapLoaded = value;
    }

    /**
     * Gets the value of the countriesPopulated property.
     * 
     */
    public boolean isCountriesPopulated() {
        return countriesPopulated;
    }

    /**
     * Sets the value of the countriesPopulated property.
     * 
     */
    public void setCountriesPopulated(boolean value) {
        this.countriesPopulated = value;
    }

    /**
     * Gets the value of the reinforceCompleted property.
     * 
     */
    public boolean isReinforceCompleted() {
        return reinforceCompleted;
    }

    /**
     * Sets the value of the reinforceCompleted property.
     * 
     */
    public void setReinforceCompleted(boolean value) {
        this.reinforceCompleted = value;
    }

    /**
     * Gets the value of the attackerWin property.
     * 
     */
    public boolean isAttackerWin() {
        return attackerWin;
    }

    /**
     * Sets the value of the attackerWin property.
     * 
     */
    public void setAttackerWin(boolean value) {
        this.attackerWin = value;
    }

    /**
     * Gets the value of the defenderWin property.
     * 
     */
    public boolean isDefenderWin() {
        return defenderWin;
    }

    /**
     * Sets the value of the defenderWin property.
     * 
     */
    public void setDefenderWin(boolean value) {
        this.defenderWin = value;
    }

    /**
     * Gets the value of the attackCompleted property.
     * 
     */
    public boolean isAttackCompleted() {
        return attackCompleted;
    }

    /**
     * Sets the value of the attackCompleted property.
     * 
     */
    public void setAttackCompleted(boolean value) {
        this.attackCompleted = value;
    }

    /**
     * Gets the value of the cardGiven property.
     * 
     */
    public boolean isCardGiven() {
        return cardGiven;
    }

    /**
     * Sets the value of the cardGiven property.
     * 
     */
    public void setCardGiven(boolean value) {
        this.cardGiven = value;
    }

    /**
     * Gets the value of the cardExchangeCompleted property.
     * 
     */
    public boolean isCardExchangeCompleted() {
        return cardExchangeCompleted;
    }

    /**
     * Sets the value of the cardExchangeCompleted property.
     * 
     */
    public void setCardExchangeCompleted(boolean value) {
        this.cardExchangeCompleted = value;
    }

    /**
     * Gets the value of the allOut property.
     * 
     */
    public boolean isAllOut() {
        return allOut;
    }

    /**
     * Sets the value of the allOut property.
     * 
     */
    public void setAllOut(boolean value) {
        this.allOut = value;
    }

    /**
     * Gets the value of the attackCountryNameFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttackCountryNameFrom() {
        return attackCountryNameFrom;
    }

    /**
     * Sets the value of the attackCountryNameFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttackCountryNameFrom(String value) {
        this.attackCountryNameFrom = value;
    }

    /**
     * Gets the value of the attackCountryNameTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttackCountryNameTo() {
        return attackCountryNameTo;
    }

    /**
     * Sets the value of the attackCountryNameTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttackCountryNameTo(String value) {
        this.attackCountryNameTo = value;
    }

    /**
     * Gets the value of the numDiceAttacker property.
     * 
     */
    public int getNumDiceAttacker() {
        return numDiceAttacker;
    }

    /**
     * Sets the value of the numDiceAttacker property.
     * 
     */
    public void setNumDiceAttacker(int value) {
        this.numDiceAttacker = value;
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
     * Gets the value of the continents property.
     * 
     * @return
     *     possible object is
     *     {@link ContinentsModel }
     *     
     */
    public ContinentsModel getContinents() {
        return continents;
    }

    /**
     * Sets the value of the continents property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContinentsModel }
     *     
     */
    public void setContinents(ContinentsModel value) {
        this.continents = value;
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

    /**
     * Gets the value of the attackerDice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attackerDice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttackerDice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getAttackerDice() {
        if (attackerDice == null) {
            attackerDice = new ArrayList<Integer>();
        }
        return this.attackerDice;
    }

    /**
     * Gets the value of the defenderDice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the defenderDice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDefenderDice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getDefenderDice() {
        if (defenderDice == null) {
            defenderDice = new ArrayList<Integer>();
        }
        return this.defenderDice;
    }

    /**
     * Gets the value of the serilaizedGraph property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerilaizedGraph() {
        return serilaizedGraph;
    }

    /**
     * Sets the value of the serilaizedGraph property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerilaizedGraph(String value) {
        this.serilaizedGraph = value;
    }

}
