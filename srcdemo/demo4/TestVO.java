package demo4;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Test Value Object.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class TestVO extends ValueObjectImpl {

  private String stringValue;
  private ComboVO combo;
  private BigDecimal numericValue;
  private BigDecimal currencyValue;
  private Boolean checkValue;
  private Boolean radioButtonValue;
  private Date dateValue;
  private String lookupValue;
  private String descrLookupValue;
  private String taValue;
  private String formattedTextValue;
  private String uri;
  private String tooltipURI;
  private String linkLabel;


  public TestVO() {
  }


  public Boolean getCheckValue() {
    return checkValue;
  }
  public BigDecimal getCurrencyValue() {
    return currencyValue;
  }
  public Date getDateValue() {
    return dateValue;
  }
  public BigDecimal getNumericValue() {
    return numericValue;
  }
  public Boolean getRadioButtonValue() {
    return radioButtonValue;
  }
  public String getStringValue() {
    return stringValue;
  }
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }
  public void setRadioButtonValue(Boolean radioButtonValue) {
    this.radioButtonValue = radioButtonValue;
  }
  public void setNumericValue(BigDecimal numericValue) {
    this.numericValue = numericValue;
  }
  public void setDateValue(Date dateValue) {
    this.dateValue = dateValue;
  }
  public void setCurrencyValue(BigDecimal currencyValue) {
    this.currencyValue = currencyValue;
  }
  public void setCheckValue(Boolean checkValue) {
    this.checkValue = checkValue;
  }
  public String getLookupValue() {
    return lookupValue;
  }
  public void setLookupValue(String lookupValue) {
    this.lookupValue = lookupValue;
  }
  public String getDescrLookupValue() {
    return descrLookupValue;
  }
  public void setDescrLookupValue(String descrLookupValue) {
    this.descrLookupValue = descrLookupValue;
  }
  public String getTaValue() {
    return taValue;
  }
  public void setTaValue(String taValue) {
    this.taValue = taValue;
  }
  public String getFormattedTextValue() {
    return formattedTextValue;
  }
  public void setFormattedTextValue(String formattedTextValue) {
    this.formattedTextValue = formattedTextValue;
  }

  public ComboVO getCombo() {
    return combo;
  }
  public void setCombo(ComboVO combo) {
    this.combo = combo;
  }
  public String getTooltipURI() {
    return tooltipURI;
  }
  public String getUri() {
    return uri;
  }
  public void setTooltipURI(String tooltipURI) {
    this.tooltipURI = tooltipURI;
  }
  public void setUri(String uri) {
    this.uri = uri;
  }
  public String getLinkLabel() {
    return linkLabel;
  }
  public void setLinkLabel(String linkLabel) {
    this.linkLabel = linkLabel;
  }




}
