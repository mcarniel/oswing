package demo40;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Order Value Object.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class OrderRowDiscountVO extends ValueObjectImpl {


  private java.math.BigDecimal orderNumber;
  private java.math.BigDecimal orderYear;
  private java.math.BigDecimal rowNumber;
  private String discountCode;
  private String discountDescription;
  private java.math.BigDecimal discountValue;


  public OrderRowDiscountVO() {
  }


  public java.math.BigDecimal getOrderNumber() {
    return orderNumber;
  }
  public void setOrderNumber(java.math.BigDecimal orderNumber) {
    this.orderNumber = orderNumber;
  }
  public java.math.BigDecimal getDiscountValue() {
    return discountValue;
  }
  public void setDiscountValue(java.math.BigDecimal discountValue) {
    this.discountValue = discountValue;
  }
  public java.math.BigDecimal getOrderYear() {
    return orderYear;
  }
  public void setOrderYear(java.math.BigDecimal orderYear) {
    this.orderYear = orderYear;
  }
  public java.math.BigDecimal getRowNumber() {
    return rowNumber;
  }
  public void setRowNumber(java.math.BigDecimal rowNumber) {
    this.rowNumber = rowNumber;
  }
  public String getDiscountCode() {
    return discountCode;
  }
  public void setDiscountCode(String discountCode) {
    this.discountCode = discountCode;
  }
  public String getDiscountDescription() {
    return discountDescription;
  }
  public void setDiscountDescription(String discountDescription) {
    this.discountDescription = discountDescription;
  }



}
