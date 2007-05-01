package demo7;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class TreeNodeVO extends ValueObjectImpl {


  private String descrLevel;
  private String codLevel;
  public TreeNodeVO() {
  }
  public String getDescrLevel() {
    return descrLevel;
  }
  public void setDescrLevel(String descrLevel) {
    this.descrLevel = descrLevel;
  }
  public String getCodLevel() {
    return codLevel;
  }
  public void setCodLevel(String codLevel) {
    this.codLevel = codLevel;
  }

}
