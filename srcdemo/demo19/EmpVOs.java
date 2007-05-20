package demo19;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Contains the old value object and the new value object, used when updating v.o.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class EmpVOs {

  private EmpVO oldVO;
  private EmpVO newVO;


  public EmpVOs(EmpVO oldVO,EmpVO newVO) {
    this.oldVO = oldVO;
    this.newVO = newVO;
  }


  public EmpVO getNewVO() {
    return newVO;
  }
  public EmpVO getOldVO() {
    return oldVO;
  }

}
