package demo26;

import java.util.ArrayList;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value object used for the detail Form.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DetailTestVO extends TestVO{

  private String address;
  private String state;
  private String zipCode;


  public DetailTestVO() {
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
