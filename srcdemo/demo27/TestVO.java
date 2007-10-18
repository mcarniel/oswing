package demo27;

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
  private String address;
  private String state;
  private String zipCode;


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


  public String getZipCode() {
    return zipCode;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public void setZipCode(String zipCode) {
    this.zipCode = zipCode;
  }

  public String getState() {
    return state;
  }
  public void setState(String state) {
    this.state = state;
  }



}
