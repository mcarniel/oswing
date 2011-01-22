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

public class OrderVO extends ValueObjectImpl {


  private java.math.BigDecimal orderNumber;
  private java.math.BigDecimal orderYear;
  private java.math.BigDecimal total;
  private String customerId;
  private java.sql.Date orderDate;
  private boolean hasOrderRows;
  private String state;


  public OrderVO() {
  }


  public java.math.BigDecimal getOrderNumber() {
    return orderNumber;
  }
  public void setOrderNumber(java.math.BigDecimal orderNumber) {
    this.orderNumber = orderNumber;
  }
  public java.sql.Date getOrderDate() {
    return orderDate;
  }
  public void setOrderDate(java.sql.Date orderDate) {
    this.orderDate = orderDate;
  }
  public java.math.BigDecimal getOrderYear() {
    return orderYear;
  }
  public void setOrderYear(java.math.BigDecimal orderYear) {
    this.orderYear = orderYear;
  }
  public java.math.BigDecimal getTotal() {
    return total;
  }
  public void setTotal(java.math.BigDecimal total) {
    this.total = total;
  }
  public String getCustomerId() {
    return customerId;
  }
  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
  public boolean isHasOrderRows() {
    return hasOrderRows;
  }
  public void setHasOrderRows(boolean hasOrderRows) {
    this.hasOrderRows = hasOrderRows;
  }
  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }



}
