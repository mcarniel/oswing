package demo19;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value Object used to store employee info.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class EmpVO extends GridEmpVO {

  private String sex;
  private BigDecimal salary;
  private Date hireDate;
  private String note;


  public EmpVO() {
  }


  public BigDecimal getSalary() {
    return salary;
  }
  public Date getHireDate() {
    return hireDate;
  }
  public String getNote() {
    return note;
  }
  public String getSex() {
    return sex;
  }
  public void setSex(String sex) {
    this.sex = sex;
  }
  public void setNote(String note) {
    this.note = note;
  }
  public void setHireDate(Date hireDate) {
    this.hireDate = hireDate;
  }
  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }



}
