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

public class OrderRowVO extends ValueObjectImpl {


  private java.math.BigDecimal orderNumber;
  private java.math.BigDecimal orderYear;
  private java.math.BigDecimal rowNumber;
  private String itemId;
  private String itemDescription;
  private java.math.BigDecimal price;
  private java.math.BigDecimal qty;


  public OrderRowVO() {
  }


  public java.math.BigDecimal getOrderNumber() {
    return orderNumber;
  }
  public void setOrderNumber(java.math.BigDecimal orderNumber) {
    this.orderNumber = orderNumber;
  }
  public java.math.BigDecimal getPrice() {
    return price;
  }
  public void setPrice(java.math.BigDecimal price) {
    this.price = price;
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
  public String getItemId() {
    return itemId;
  }
  public void setItemId(String itemId) {
    this.itemId = itemId;
  }
  public String getItemDescription() {
    return itemDescription;
  }
  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }
  public java.math.BigDecimal getQty() {
    return qty;
  }
  public void setQty(java.math.BigDecimal qty) {
    this.qty = qty;
  }



}
