package demo5.server;

import org.openswing.swing.server.Action;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.QueryUtil;
import demo5.java.TestVO;
import java.sql.Connection;
import org.openswing.swing.server.ConnectionManager;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class GridLoadAction implements Action {
  public GridLoadAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadGrid";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);

      GridParams pars = (GridParams)inputPar;

      String sql = "select DEMO5.TEXT,DEMO5.DECNUM,DEMO5.CURRNUM,DEMO5.THEDATE,DEMO5.COMBO,DEMO5.CHECK_BOX,DEMO5.RADIO,DEMO5.CODE,DEMO5_LOOKUP.DESCRCODE from DEMO5,DEMO5_LOOKUP where DEMO5.CODE=DEMO5_LOOKUP.CODE";
      ArrayList vals = new ArrayList();

      HashMap map = new HashMap();
      map.put("stringValue","DEMO5.TEXT");
      map.put("numericValue","DEMO5.DECNUM");
      map.put("currencyValue","DEMO5.CURRNUM");
      map.put("dateValue","DEMO5.THEDATE");
      map.put("comboValue","DEMO5.COMBO");
      map.put("checkValue","DEMO5.CHECK_BOX");
      map.put("radioButtonValue","DEMO5.RADIO");
      map.put("lookupValue","DEMO5.CODE");
      map.put("descrLookupValue","DEMO5_LOOKUP.DESCRCODE");

      return QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          vals,
          map,
          TestVO.class,
          "Y",
          "N",
          context,
          pars,
          25,
          true
      );
    }
    catch (Exception ex) {
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }


  }


}
