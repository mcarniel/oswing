package org.openswing.swing.internationalization.java;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Class for retrieve the collection of all internationalization properties:
 * translations, data/numeric/currency formats.</p>
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
public abstract class ResourcesFactory {


  /**
   * Load dictionary, according to the specified language id.
   * @param langId language id identifier
   */
  public abstract void setLanguage(String langId) throws UnsupportedOperationException;


  /**
   * @return internationalization settings, according with the current language
   */
  public abstract Resources getResources();


  /**
   * @param langId language id identifier
   * @return internationalization settings, according with the language specified
   */
  public abstract Resources getResources(String langId) throws UnsupportedOperationException;


}
