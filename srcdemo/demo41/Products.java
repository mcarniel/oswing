package demo41;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Test Value Object.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class Products extends ValueObjectImpl {

  private String pname;
  private Units units;
  private java.math.BigDecimal id;


  public Products() {
  }


  public String getPname() {
    return pname;
  }
  public void setPname(String pname) {
    this.pname = pname;
  }
  public Units getUnits() {
    return units;
  }
  public void setUnits(Units units) {
    this.units = units;
  }


  public java.math.BigDecimal getId() {
    return id;
  }
  public void setId(java.math.BigDecimal id) {
    this.id = id;
  }


  /**
   * Method used by Grid and Form objects to internally duplicate a value object.
   * @return
   * @throws java.lang.CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException {
    Products vo = (Products)super.clone();
    if (vo.getUnits()!=null)
      vo.setUnits((Units)vo.getUnits().clone());
    return vo;
  }



}
