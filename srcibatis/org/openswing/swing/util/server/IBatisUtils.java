package org.openswing.swing.util.server;

import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import java.util.Map;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import org.openswing.swing.message.send.java.GridParams;
import com.ibatis.sqlmap.client.SqlMapClient;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Helper class containing an utility method useful when retrieving a block of data for a grid, using iBatis.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class IBatisUtils {


  /**
   * Read a block of records from the result set, starting from a Query object.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param blockSize number of records to read
   * @param sqlMap SQL mapping used by iBatis
   * @param id select identifier in XML mapping
   */
  public static Response getBlockFromQuery(
    int action,
    int startIndex,
    int blockSize,
    SqlMapClient sqlMap,
    String id,
    IBatisParamsWrapper gridParams
  ) throws Exception {

    // read a block of records...
    ArrayList gridList = new ArrayList();
    boolean moreRows = false;
    int resultSetLength = -1;
    int rowCount = 0;
    List list = null;
    if (action==GridParams.LAST_BLOCK_ACTION) {
      // last block requested: the whole result set will be loaded, to determine the result set length
      list = sqlMap.queryForList(id,gridParams);
      resultSetLength = list.size();
      startIndex = Math.max(rowCount-blockSize,0);
      for(int i=startIndex;i<resultSetLength;i++)
        gridList.add(list.get(i));
      return new VOListResponse(gridList,false,resultSetLength);
    } else {
      if (action==GridParams.PREVIOUS_BLOCK_ACTION) {
        action = GridParams.NEXT_BLOCK_ACTION;
        startIndex = Math.max(startIndex-blockSize,0);
      }
    }

    // read a block of data...
    list = sqlMap.queryForList(id,gridParams,startIndex,blockSize+1);
    gridList.addAll(list);
    if (gridList.size()>blockSize) {
      gridList.remove(gridList.size() - 1);
      moreRows = true;
    }
    if (resultSetLength==-1)
      resultSetLength = gridList.size();
    return new VOListResponse(gridList,moreRows,resultSetLength);
  }


}
