package demo45;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value Object used to store group sales.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GroupSalesVO extends ValueObjectImpl {

  private String area;
  private BigDecimal salesNumber;
  private java.sql.Date saleDate;
  private java.math.BigDecimal totalAmount;
  private String note;

  public GroupSalesVO() {
  }


  public String getArea() {
    return area;
  }
  public void setArea(String area) {
    this.area = area;
  }
  public java.math.BigDecimal getTotalAmount() {
    return totalAmount;
  }
  public void setTotalAmount(java.math.BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }
  public java.sql.Date getSaleDate() {
    return saleDate;
  }
  public void setSaleDate(java.sql.Date saleDate) {
    this.saleDate = saleDate;
  }
  public BigDecimal getSalesNumber() {
    return salesNumber;
  }
  public void setSalesNumber(BigDecimal salesNumber) {
    this.salesNumber = salesNumber;
  }
  public String getNote() {
    return note;
  }
  public void setNote(String note) {
    this.note = note;
  }




}
