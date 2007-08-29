package demo22;

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
  private String comboValue;
  private BigDecimal numericValue;
  private BigDecimal currencyValue;
  private Boolean checkValue;
  private Date dateValue;
  private String formattedTextValue;
  private Integer intValue;


  public TestVO() {
  }


  public Boolean getCheckValue() {
    return checkValue;
  }
  public String getComboValue() {
    return comboValue;
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
  public String getStringValue() {
    return stringValue;
  }
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
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
  public void setComboValue(String comboValue) {
    this.comboValue = comboValue;
  }
  public void setCheckValue(Boolean checkValue) {
    this.checkValue = checkValue;
  }
  public String getFormattedTextValue() {
    return formattedTextValue;
  }
  public void setFormattedTextValue(String formattedTextValue) {
    this.formattedTextValue = formattedTextValue;
  }
  public Integer getIntValue() {
    return intValue;
  }
  public void setIntValue(Integer intValue) {
    this.intValue = intValue;
  }




}
