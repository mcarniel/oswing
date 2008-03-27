package org.openswing.swing.domains.java;

import java.io.*;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class that represents a couple (code,description) included in a domain.</p>
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
public class DomainPair implements Serializable {

  /** code */
  private Object code = null;

  /** description related to the code */
  private String description = null;


  /**
   * Constructor.
   * @param code code
   * @param description description related to the code
   */
  public DomainPair(Object code,String description) {
    this.code = code;
    this.description = description;
  }


  /**
   * @return code
   */
  public final Object getCode() {
    return code;
  }


  /**
   * @return description related to the code
   */
  public final String getDescription() {
    return description;
  }


}
