package demo2;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Test Lookup Value Object.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class TestLookupVO extends ValueObjectImpl {

  private String lookupValue;
  private String descrLookupValue;

  public TestLookupVO() {
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




}
