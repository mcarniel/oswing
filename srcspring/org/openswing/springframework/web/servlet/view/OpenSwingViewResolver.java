package org.openswing.springframework.web.servlet.view;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.view.AbstractView;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import java.io.ObjectOutputStream;
import org.openswing.swing.message.receive.java.ErrorResponse;


/**
 * <p>Title: OpenSwing Framework</p>
* <p>Description: Class that does not render the model but only send to the OpenSwing client the model (that must be always a Response object).</p>
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
public class OpenSwingViewResolver extends AbstractView {

  public static final String RESPONSE_PROPERTY_NAME = "response";


  /**
   * Give back to OpenSwing client layer the Response object stored in "model" argument.
   * @param request current HTTP request
   * @param response current HTTP response
   * @throws Exception if rendering failed
   */
  protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    Object answer = null;
    answer = model.get(RESPONSE_PROPERTY_NAME);
    if (answer==null)
      answer = model.values().iterator().next();
    if (answer==null)
      answer = new ErrorResponse("no model found!");

    ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
    oos.writeObject(answer);
    oos.close();
  }


}
