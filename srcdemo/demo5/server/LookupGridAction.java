package demo5.server;

import org.openswing.swing.server.Action;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.server.ConnectionManager;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import demo5.java.TestLookupVO;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class LookupGridAction implements Action {
  public LookupGridAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "lookupGrid";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    HashMap map = new HashMap();
    map.put("lookupValue","DEMO5_LOOKUP.CODE");
    map.put("descrLookupValue","DEMO5_LOOKUP.DESCRCODE");
    try {
      conn = ConnectionManager.getConnection(context);
      return QueryUtil.getQuery(
          conn,
          userSessionPars,
          "select DEMO5_LOOKUP.CODE,DEMO5_LOOKUP.DESCRCODE from DEMO5_LOOKUP",
          new ArrayList(),
          map,
          TestLookupVO.class,
          "Y",
          "N",
          context,
          (GridParams) inputPar,
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


