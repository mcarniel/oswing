package org.openswing.swing.client;

import org.openswing.swing.lookup.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Code container interface: it must be defined by each code container.
 * </p>
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
public interface CodBoxContainer {


  /**
   * Code validation event: this method must be implemented by code containers to validate the specified code.
   * @param code code to validate
   */
  public void validateCode(String code) throws RestoreFocusOnInvalidCodeException;


  /**
   * Callback called when the code input field abilitation is changed.
   * @param enabled code input field abilitation
   */
  public void setEnabled(boolean enabled);

}
