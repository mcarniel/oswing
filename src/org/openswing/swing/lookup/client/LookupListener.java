package org.openswing.swing.lookup.client;


import org.openswing.swing.message.receive.java.ValueObject;
import java.util.Collection;
import org.openswing.swing.client.*;
import org.openswing.swing.client.*;



/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Lookup Listener.</p>
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
public interface LookupListener {

  /**
   * Method called when lookup code is validated and when code is selected on lookup grid frame.
   * @param validated <code>true</code> if lookup code is correclty validated, <code>false</code> otherwise
   */
  public void codeValidated(boolean validated);


  /**
   * Method called when lookup code is changed (also when is set to "" or null)
   * @param parentVO lookup container v.o.
   * @param parentChangedAttributes lookup container v.o. attributes
   */
  public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes);


  /**
   * Method called before code validation and on lookup button click.
   */
  public void beforeLookupAction(ValueObject parentVO);


  /**
   * Validation is forced.
   */
  public void forceValidate();

}
