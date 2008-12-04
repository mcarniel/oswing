package demo49;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.util.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Test Value Object.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class OrdersVO extends ValueObjectImpl {

  private BigDecimal sellQty;
  private Date orderDate;
  private String agent;
  private String category;
  private java.math.BigDecimal sellAmount;
  private String item;
  private String subCategory;
  private String country;
  private String zone;


  public OrdersVO() {
  }


  public String getItem() {
    return item;
  }
  public BigDecimal getSellQty() {
    return sellQty;
  }
  public Date getOrderDate() {
    return orderDate;
  }
  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }
  public void setSellQty(BigDecimal sellQty) {
    this.sellQty = sellQty;
  }

  public void setItem(String item) {
    this.item = item;
  }
  public String getAgent() {
    return agent;
  }
  public void setAgent(String agent) {
    this.agent = agent;
  }
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
  public java.math.BigDecimal getSellAmount() {
    return sellAmount;
  }
  public void setSellAmount(java.math.BigDecimal sellAmount) {
    this.sellAmount = sellAmount;
  }
  public String getSubCategory() {
    return subCategory;
  }
  public void setSubCategory(String subCategory) {
    this.subCategory = subCategory;
  }
  public String getCountry() {
    return country;
  }
  public void setCountry(String country) {
    this.country = country;
  }
  public String getZone() {
    return zone;
  }
  public void setZone(String zone) {
    this.zone = zone;
  }




}
