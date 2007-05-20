package demo19;

import org.openswing.swing.mdi.client.*;
import com.ibatis.sqlmap.client.SqlMapClient;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Client Facade, called by the MDI Tree.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class DemoClientFacade implements ClientFacade {

  private SqlMapClient sqlMap = null;

  public DemoClientFacade(SqlMapClient sqlMap) {
    this.sqlMap = sqlMap;
  }


  public void getEmps() {
    new EmpGridFrameController(sqlMap);
  }




}
