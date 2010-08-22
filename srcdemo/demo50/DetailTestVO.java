package demo50;

import java.util.ArrayList;
import org.openswing.swing.message.receive.java.ValueObjectImpl;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Value object used for the detail Form.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DetailTestVO extends ValueObjectImpl{


  private String stringValue;
  private String fileDescription;
  private byte[] file;


  public DetailTestVO() {
  }


  public byte[] getFile() {
    return file;
  }
  public String getFileDescription() {
    return fileDescription;
  }
  public String getStringValue() {
    return stringValue;
  }
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }
  public void setFileDescription(String fileDescription) {
    this.fileDescription = fileDescription;
  }
  public void setFile(byte[] file) {
    this.file = file;
  }




}
