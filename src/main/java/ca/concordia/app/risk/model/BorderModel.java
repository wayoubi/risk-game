
package ca.concordia.app.risk.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for border complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="border">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="countryId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="neighbours" type="{http://ca.concordia.app.risk/game}neighboursList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "border", namespace = "http://ca.concordia.app.risk/game", propOrder = {
    "countryId",
    "neighbours"
})
public class BorderModel {

    @XmlElement(namespace = "http://ca.concordia.app.risk/game")
    protected int countryId;
    @XmlList
    @XmlElement(namespace = "http://ca.concordia.app.risk/game", type = Integer.class)
    protected List<Integer> neighbours;

    /**
     * Gets the value of the countryId property.
     * 
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the value of the countryId property.
     * 
     */
    public void setCountryId(int value) {
        this.countryId = value;
    }

    /**
     * Gets the value of the neighbours property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the neighbours property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNeighbours().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Integer }
     * 
     * 
     */
    public List<Integer> getNeighbours() {
        if (neighbours == null) {
            neighbours = new ArrayList<Integer>();
        }
        return this.neighbours;
    }

}
