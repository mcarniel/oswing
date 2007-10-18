package demo26;

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

  private String surname;
  private String customerCode;
  private String name;
  private String city;


  public TestVO() {
  }


  public String getCity() {
    return city;
  }
  public void setCity(String city) {
    this.city = city;
  }
  public String getSurname() {
    return surname;
  }
  public void setSurname(String surname) {
    this.surname = surname;
  }
  public String getCustomerCode() {
    return customerCode;
  }
  public void setCustomerCode(String customerCode) {
    this.customerCode = customerCode;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }



}
