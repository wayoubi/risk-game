package ca.concordia.app.risk.model.xmlbeans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Java class for continents complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="continents">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="continent" type="{http://ca.concordia.app.risk/game}continent" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "continents", namespace = "http://ca.concordia.app.risk/game", propOrder = {"list"})
public class ContinentsModel {

    @XmlElement(name = "continent", namespace = "http://ca.concordia.app.risk/game", required = true)
    protected List<ContinentModel> list;

    /**
     * Gets the value of the list property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot.
     * Therefore any modification you make to the returned list will be present
     * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
     * for the list property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     *
     * <pre>
     * getList().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContinentModel }
     */
    public List<ContinentModel> getList() {
        if (list == null) {
            list = new ArrayList<ContinentModel>();
        }
        return this.list;
    }

}
