package demo43;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value Object used to store a file.</p>
 * <p>Copyright: Copyright (C) 2008 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class FileVO extends ValueObjectImpl {

  private String fileName;
  private byte[] file;
  private java.util.Date uploadDate;


  public FileVO() {}


  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public byte[] getFile() {
    return file;
  }
  public void setFile(byte[] file) {
    this.file = file;
  }
  public java.util.Date getUploadDate() {
    return uploadDate;
  }
  public void setUploadDate(java.util.Date uploadDate) {
    this.uploadDate = uploadDate;
  }

}
