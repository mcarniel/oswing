package demo30;

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
  private ArrayList pricelists;
  private String description;
  private String note;


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
  public ArrayList getPricelists() {
    return pricelists;
  }
  public void setPricelists(ArrayList pricelists) {
    this.pricelists = pricelists;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getNote() {
    return note;
  }
  public void setNote(String note) {
    this.note = note;
  }


}
