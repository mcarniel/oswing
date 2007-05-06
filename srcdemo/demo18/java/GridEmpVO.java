package demo18.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value Object used to store employee summary info.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GridEmpVO extends ValueObjectImpl {

  private String empCode;
  private String lastName;
  private String firstName;
  private String deptCode;
  private String deptDescription;

  public GridEmpVO() {
  }


  public String getEmpCode() {
    return empCode;
  }
  public void setEmpCode(String empCode) {
    this.empCode = empCode;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getDeptCode() {
    return deptCode;
  }
  public void setDeptCode(String deptCode) {
    this.deptCode = deptCode;
  }
  public String getDeptDescription() {
    return deptDescription;
  }
  public void setDeptDescription(String deptDescription) {
    this.deptDescription = deptDescription;
  }





}
