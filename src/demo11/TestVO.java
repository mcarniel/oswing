package demo11;

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

  private String itemCode;
  private String description;
  private java.math.BigDecimal qty;
  private java.math.BigDecimal price;
  public TestVO() {
  }
  public String getItemCode() {
    return itemCode;
  }
  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public java.math.BigDecimal getQty() {
    return qty;
  }
  public void setQty(java.math.BigDecimal qty) {
    this.qty = qty;
  }
  public java.math.BigDecimal getPrice() {
    return price;
  }
  public void setPrice(java.math.BigDecimal price) {
    this.price = price;
  }




}
