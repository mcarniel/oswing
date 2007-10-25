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
  private String pricelistCode;
  private String description;
  private String note;
  private java.sql.Date startDate;
  private java.sql.Date endDate;


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
  public String getPricelistCode() {
    return pricelistCode;
  }
  public void setPricelistCode(String pricelistCode) {
    this.pricelistCode = pricelistCode;
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
  public java.sql.Date getStartDate() {
    return startDate;
  }
  public void setStartDate(java.sql.Date startDate) {
    this.startDate = startDate;
  }
  public java.sql.Date getEndDate() {
    return endDate;
  }
  public void setEndDate(java.sql.Date endDate) {
    this.endDate = endDate;
  }

}
