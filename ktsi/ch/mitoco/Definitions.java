//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2.3-3- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.17 at 06:07:35 PM MESZ 
//


package ch.mitoco;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Definitions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Definitions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Lenght" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Font" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mMode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Definitions", propOrder = {
    "lenght",
    "font",
    "mMode"
})
public class Definitions {

    @XmlElement(name = "Lenght")
    protected int lenght;
    @XmlElement(name = "Font", required = true)
    protected String font;
    protected int mMode;

    /**
     * Gets the value of the lenght property.
     * 
     */
    public int getLenght() {
        return lenght;
    }

    /**
     * Sets the value of the lenght property.
     * 
     */
    public void setLenght(int value) {
        this.lenght = value;
    }

    /**
     * Gets the value of the font property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFont() {
        return font;
    }

    /**
     * Sets the value of the font property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFont(String value) {
        this.font = value;
    }

    /**
     * Gets the value of the mMode property.
     * 
     */
    public int getMMode() {
        return mMode;
    }

    /**
     * Sets the value of the mMode property.
     * 
     */
    public void setMMode(int value) {
        this.mMode = value;
    }

}