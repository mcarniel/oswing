package demo39;

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

  private String code;
  private String folderCode;
  private String description;


  public TestVO() {
  }


  public String getCode() {
    return code;
  }
  public String getDescription() {
    return description;
  }
  public void setCode(String code) {
    this.code = code;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getFolderCode() {
    return folderCode;
  }
  public void setFolderCode(String folderCode) {
    this.folderCode = folderCode;
  }



}
