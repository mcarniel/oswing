package demo17;

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
public class EmpVO extends ValueObjectImpl {

  private String empCode;
  private String lastName;
  private String firstName;
  private DeptVO dept;
  private String sex;
  private BigDecimal salary;
  private Date hireDate;
  private String note;
  private TaskVO task;
  private String workingPlace;


  public EmpVO() {
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
  public DeptVO getDept() {
    return dept;
  }
  public void setDept(DeptVO dept) {
    this.dept = dept;
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
  public TaskVO getTask() {
    return task;
  }
  public void setTask(TaskVO task) {
    this.task = task;
  }
  public String getWorkingPlace() {
    return workingPlace;
  }
  public void setWorkingPlace(String workingPlace) {
    this.workingPlace = workingPlace;
  }





}
