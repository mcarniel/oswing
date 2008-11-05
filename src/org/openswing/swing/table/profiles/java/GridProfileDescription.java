package org.openswing.swing.table.profiles.java;

import java.io.*;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid profile description: it contains the profile identifier,
 * the profile description and a flag used to identify the default profile,
 * to show it in the user profiles list.</p>
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
public class GridProfileDescription implements Serializable {

  /** profile identifier */
  private Object id = null;

  /** profile description */
  private String description = null;

  /** flag used to identify the default profile */
  private boolean defaultProfile;


  /**
   * Constructor.
   * @param id profile identifier
   * @param description profile description
   * @param defaultProfile flag used to identify the default profile
   */
  public GridProfileDescription(Object id,String description,boolean defaultProfile) {
    this.id = id;
    this.description = description;
    this.defaultProfile = defaultProfile;
 }


  public String getDescription() {
    return description;
  }
  public Object getId() {
    return id;
  }
  public boolean isDefaultProfile() {
    return defaultProfile;
  }


  public final boolean equals(Object o) {
    if (o==null || !(o instanceof GridProfileDescription))
      return false;
    return ((GridProfileDescription)o).getId().equals(id);
  }

}
