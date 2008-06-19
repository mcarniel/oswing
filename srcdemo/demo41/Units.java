package demo41;

import org.openswing.swing.message.receive.java.*;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value Object used for the ComboBoxVOControl.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class Units extends ValueObjectImpl {

  private String unitName;
  private java.math.BigDecimal id;

  public Units() {
  }


  public String getUnitName() {
    return unitName;
  }
  public void setUnitName(String unitName) {
    this.unitName = unitName;
  }
  public java.math.BigDecimal getId() {
    return id;
  }
  public void setId(java.math.BigDecimal id) {
    this.id = id;
  }




}
