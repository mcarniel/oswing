package demo42;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value Object used to store tasks.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class TaskVO extends ValueObjectImpl {

  private String taskCode;
  private String description;
  private String status;

  public TaskVO() {
  }


  public String getTaskCode() {
    return taskCode;
  }
  public void setTaskCode(String taskCode) {
    this.taskCode = taskCode;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }




}
