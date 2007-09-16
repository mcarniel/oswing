package demo23;

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

  private String propertyName;
  private Object propertyValue;
  public TestVO() {}
  public String getPropertyName() {
    return propertyName;
  }
  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }
  public Object getPropertyValue() {
    return propertyValue;
  }
  public void setPropertyValue(Object propertyValue) {
    this.propertyValue = propertyValue;
  }

}
