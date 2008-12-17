package org.openswing.swing.pivottable.client;

import javax.swing.*;
import java.awt.dnd.*;
import java.awt.Toolkit;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Draggable button used within pivot table.</p>
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
public class DraggableButton extends JPanel implements DragSourceListener, DropTargetListener {

  /** drag source */
  private DragSource dragSource = new DragSource();

  /** panel identifier, used for DnD */
  private String panelId = null;

  /** drop gestures */
  private DropTarget dropTarget = null;

  /** cursor to show on dragging */
  private Cursor dragCursor = null;

  /** component position */
  private int pos;

  /** list of DraggableButtonListener elements */
  private ArrayList draggableButtonListeners = new ArrayList();


  public DraggableButton(String panelId,int pos) {
    this.panelId = panelId;
    this.pos = pos;
    try {
      dragSource.createDefaultDragGestureRecognizer(
          this,
          DnDConstants.ACTION_MOVE,
          new DragGestureAdapter(this)
      );
      dropTarget = new DropTarget(this, this);
      dragCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        ClientUtils.getImage("column.gif"),
        new Point(15, 10),
        ClientSettings.getInstance().getResources().getResource("drag")
      );
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public final void addDraggableButtonListener(DraggableButtonListener listener) {
    draggableButtonListeners.add(listener);
  }


  public final void removeDraggableButtonListener(DraggableButtonListener listener) {
    draggableButtonListeners.remove(listener);
  }


  /********************************************************************
   *
   *             DRAG 'N DROP MANAGEMENTS METHODS
   *
   ********************************************************************/



  /************************************************************
   * DRAG MANAGEMENT
   ************************************************************/


  class DragGestureAdapter implements DragGestureListener {

    private DragSourceListener dragListener = null;

    public DragGestureAdapter(DragSourceListener dragListener) {
      this.dragListener = dragListener;
    }


    /**
     * A drag gesture has been initiated.
     */
    public final void dragGestureRecognized( DragGestureEvent event) {
      setCursor(dragCursor);
      try {
        dragSource.startDrag(event, DragSource.DefaultMoveDrop,
                             new StringSelection(panelId + "_" + pos),
                             dragListener);
      }
      catch (InvalidDnDOperationException ex) {
      }
    }

  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has entered the DropSite
   */
  public final void dragEnter (DragSourceDragEvent event) {
    setCursor(Cursor.getDefaultCursor());
  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has exited the DropSite.
   */
  public final void dragExit (DragSourceEvent event) {
    setCursor(Cursor.getDefaultCursor());
  }


  /**
   * This message goes to DragSourceListener, informing it that the dragging is currently ocurring over the DropSite.
   */
  public final void dragOver (DragSourceDragEvent event) {
    setCursor(dragCursor);
  }


  /**
   * This method is invoked when the user changes the dropAction.
   */
  public final void dropActionChanged ( DragSourceDragEvent event) { }


  /**
   * This message goes to DragSourceListener, informing it that the dragging has ended.
   */
  public final void dragDropEnd (DragSourceDropEvent event) {
  }



  /************************************************************
   * DROP MANAGEMENT
   ************************************************************/

  /**
   * This method is invoked when you are dragging over the DropSite.
   */
  public final void dragEnter (DropTargetDragEvent event) {
    event.acceptDrag (DnDConstants.ACTION_MOVE);
  }


  /**
   * This method is invoked when you are exit the DropSite without dropping.
   */
  public final void dragExit (DropTargetEvent event) {
  }

  /**
   * This method is invoked when a drag operation is going on.
   */
  public final void dragOver (DropTargetDragEvent event) {
  }


  /**
   * This method is invoked when a drop event has occurred.
   */
  public final void drop(DropTargetDropEvent event) {
    try {
      setCursor(Cursor.getDefaultCursor());
      Transferable transferable = event.getTransferable();
      if (transferable.isDataFlavorSupported (DataFlavor.stringFlavor)){

        String id = (String)transferable.getTransferData ( DataFlavor.stringFlavor);
        if (id!=null && id.startsWith(panelId)) {
          event.acceptDrop(DnDConstants.ACTION_MOVE);
          event.getDropTargetContext().dropComplete(true);
          String[] tokens = id.split("_");
          int draggedCompPos = Integer.parseInt(tokens[tokens.length-1]);
//          System.out.println("draggedCompPos:"+draggedCompPos+" pos:"+pos);
          if (draggedCompPos!=pos) {
            DraggableButtonEvent e = new DraggableButtonEvent(
              panelId,
              draggedCompPos,
              pos
            );
            for(int i=0;i<draggableButtonListeners.size();i++)
              ((DraggableButtonListener)draggableButtonListeners.get(i)).dragEventFired(e);
          }
        }
        else
          event.rejectDrop();

      } else{
        event.rejectDrop();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      event.rejectDrop();
    } finally {
      try {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
      catch (Exception ex) {
      }

    }
  }

  /**
   * This method is invoked if the use modifies the current drop gesture.
   */
  public final void dropActionChanged ( DropTargetDragEvent event ) {
  }




}
