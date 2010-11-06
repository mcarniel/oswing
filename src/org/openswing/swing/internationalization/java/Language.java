package org.openswing.swing.internationalization.java;

import java.io.Serializable;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Language descriptor: used in MDIController.getLanguages and in ChangeLanguageDialog.</p>
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

public class Language implements Serializable {

  /** language identifier */
  private String languageId = null;

  /** language description */
  private String description = null;


  public Language() {}


  /**
   * Constructor.
   * @param languageId language identifier
   * @param description language description
   */
  public Language(String languageId,String description) {
    this.languageId = languageId;
    this.description = description;
  }


  /**
   * @return language description
   */
  public final String getDescription() {
    return description;
  }


  /**
   * @return language identifier
   */
  public final String getLanguageId() {
    return languageId;
  }


  /**
   * @return language description
   */
  public final String toString() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public void setLanguageId(String languageId) {
    this.languageId = languageId;
  }


}
