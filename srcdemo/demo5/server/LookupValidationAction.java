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
import org.openswing.swing.message.send.java.GridParams;
import demo5.java.TestLookupVO;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.LookupValidationParams;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */

public class LookupValidationAction implements Action {
  public LookupValidationAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "lookupValidate";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    ArrayList pars = new ArrayList();
    pars.add(((LookupValidationParams)inputPar).getCode());

    HashMap map = new HashMap();
    map.put("lookupValue","DEMO5_LOOKUP.CODE");
    map.put("descrLookupValue","DEMO5_LOOKUP.DESCRCODE");
    try {
      conn = ConnectionManager.getConnection(context);
      return QueryUtil.getQuery(
          conn,
          userSessionPars,
          "select DEMO5_LOOKUP.CODE,DEMO5_LOOKUP.DESCRCODE from DEMO5_LOOKUP where CODE=?",
          pars,
          map,
          TestLookupVO.class,
          "Y",
          "N",
          context,
          new GridParams(GridParams.NEXT_BLOCK_ACTION,0,new HashMap(),new ArrayList(),new ArrayList(),new HashMap()),
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

//      if (list.size() > 0)
//        return new VOListResponse(list, false, list.size());
//      else
//        return new ErrorResponse("Code not valid");
  }


}
