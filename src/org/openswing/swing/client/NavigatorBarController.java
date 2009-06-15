package org.openswing.swing.client;

import java.awt.event.ActionEvent;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Navigator bar controller: it is implemented by Grids component.</p>
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
public interface NavigatorBarController {


    /**
     * @return last record number in result set; -1 as default value
     */
    public int getTotalResultSetLength();


    /**
     * @return block size; -1 if loading is not based on one page per time
     */
    public int getBlockSize();


    /**
     * Method invoked by NavigatorBar to change page to load.
     * @param pageNr pabe number to load
     */
    public void loadPage(int pageNr);


    /**
     * Method called when user click on "first" button on the navigation bar:
     * it will clear table model, reload data and select the first row of the block.
     * This operations are executed in a separated thread.
     */
    public void firstRow(NavigatorBar navBar);


    /**
     * Method called when user click on "next" button on the navigation bar:
     * it may clear table model, reload next data block and select the first row of the block.
     * This operations are executed in a separated thread.
     */
    public void nextRow(NavigatorBar navBar,ActionEvent e);


    /**
     * Method called when user click on "previous page" button on the navigation bar:
     * it does clear table model, reload previous data block and select the last row of the block.
     */
    public void previousPage(NavigatorBar navBar);


    /**
     * Method called when user click on "next page" button on the navigation bar:
     * it does clear table model, reload next data block and select the first row of the block.
     * This operations are executed in a separated thread.
     */
    public void nextPage(NavigatorBar navBar);


    /**
     * Method called when user click on "previous" button on the navigation bar:
     * it may clear table model, reload previous data block and select the last row of the block.
     * This operations are executed in a separated thread.
     */
    public void previousRow(NavigatorBar navBar,ActionEvent e);


    /**
     * Method called when user click on "last" button on the navigation bar:
     * it may clear table model, reload the last data block and select the last row of the block.
     * This operations are executed in a separated thread.
     */
    public void lastRow(NavigatorBar navBar);






}
