//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.02.15 at 12:20:45 PM CET 
//


package de.unibi.cebitec.bibiserv.jobproxy.data.task;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Container description
 * 
 * <p>Java class for tContainer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tContainer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="image" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ports" type="{}tPorts" minOccurs="0"/>
 *         &lt;element name="mounts" type="{}tMounts" minOccurs="0"/>
 *         &lt;element name="cmd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tContainer", propOrder = {
    "image",
    "ports",
    "mounts",
    "cmd"
})
public class TContainer {

    @XmlElement(required = true)
    protected String image;
    protected TPorts ports;
    protected TMounts mounts;
    protected String cmd;

    /**
     * Gets the value of the image property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets the value of the image property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImage(String value) {
        this.image = value;
    }

    /**
     * Gets the value of the ports property.
     * 
     * @return
     *     possible object is
     *     {@link TPorts }
     *     
     */
    public TPorts getPorts() {
        return ports;
    }

    /**
     * Sets the value of the ports property.
     * 
     * @param value
     *     allowed object is
     *     {@link TPorts }
     *     
     */
    public void setPorts(TPorts value) {
        this.ports = value;
    }

    /**
     * Gets the value of the mounts property.
     * 
     * @return
     *     possible object is
     *     {@link TMounts }
     *     
     */
    public TMounts getMounts() {
        return mounts;
    }

    /**
     * Sets the value of the mounts property.
     * 
     * @param value
     *     allowed object is
     *     {@link TMounts }
     *     
     */
    public void setMounts(TMounts value) {
        this.mounts = value;
    }

    /**
     * Gets the value of the cmd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * Sets the value of the cmd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCmd(String value) {
        this.cmd = value;
    }

}
