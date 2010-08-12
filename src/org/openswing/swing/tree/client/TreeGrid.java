package org.openswing.swing.tree.client;


import java.lang.reflect.*;
import java.text.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: grid that contains a tree in the first column.</p>
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
public class TreeGrid extends JTable {

    /** a subclass of JTree. */
    protected TreeTableCellRenderer tree;

    private TreeGridNodeRenderer treeRenderer;

    /** grid columns alignments: collection of pairs <attribute name,alignment: Integer> */
    private Hashtable gridColumnAlignments = new Hashtable();

    /** attribute name that identifies the column containing the tree */
    private String attributeName;

    /** attribute names to used to show grid columns */
    private ArrayList gridColumns = new ArrayList();

    /** optional attribute name that identifies the name of the image to show as tree node */
    private String iconAttributeName;


    public TreeGrid(TreeTableModel treeTableModel,String attributeName,ArrayList gridColumns,ArrayList gridColumnSizes,Hashtable gridColumnAlignments,String folderIconName,String leavesImageName,Format formatter,boolean rootVisible) {
      super();
      this.attributeName = attributeName;
      this.gridColumns = gridColumns;
      this.gridColumnAlignments = gridColumnAlignments;

      treeRenderer = new TreeGridNodeRenderer(folderIconName,leavesImageName,formatter);

      // Create the tree. It will be used as a renderer and editor.
      tree = new TreeTableCellRenderer(treeTableModel);
      tree.setRootVisible(rootVisible);
      tree.setCellRenderer(treeRenderer);

      // Install a tableModel representing the visible rows in the tree.
      super.setModel(new TreeTableModelAdapter(treeTableModel, tree));

      // Force the JTable and JTree to share their row selection models.
      ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
      tree.setSelectionModel(selectionWrapper);
      setSelectionModel(selectionWrapper.getListSelectionModel());

      // Install the tree editor renderer and editor.
      setDefaultRenderer(TreeTableModel.class, tree);

      setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

      // No grid.
      setShowGrid(false);

      // No intercell spacing
      setIntercellSpacing(new Dimension(0, 0));

      setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      for(int i=0;i<gridColumnSizes.size();i++) {
        getColumnModel().getColumn(i).setPreferredWidth( ( (Integer)gridColumnSizes.get(i)).intValue());
        if (gridColumnAlignments.containsKey(gridColumns.get(i)) &&
            !gridColumns.get(i).equals(attributeName)) {
          DefaultTableCellRenderer rend = new DefaultTableCellRenderer();
          rend.setHorizontalAlignment( ((Integer)gridColumnAlignments.get(gridColumns.get(i))).intValue() );
          getColumnModel().getColumn(i).setCellRenderer(rend);
        }
      }

      JTableHeader th = this.getTableHeader();
      th.setPreferredSize(new Dimension(th.getPreferredSize().width,ClientSettings.HEADER_HEIGHT));

      setRowHeight(ClientSettings.CELL_HEIGHT);
      setBackground(ClientSettings.GRID_CELL_BACKGROUND);
      setForeground(ClientSettings.GRID_CELL_FOREGROUND);
      setSelectionForeground(ClientSettings.GRID_SELECTION_FOREGROUND);
      setSelectionBackground(ClientSettings.GRID_SELECTION_BACKGROUND);

    }


    /**
     * Overridden to message super and forward the method to the tree.
     * Since the tree is not actually in the component hieachy it will
     * never receive this unless we forward it in this manner.
     */
    public void updateUI() {
        super.updateUI();
        if(tree != null) {
            tree.updateUI();
        }
        // Use the tree's default foreground and background colors in the
        // table.
        LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font");
    }

    /* Workaround for BasicTableUI anomaly. Make sure the UI never tries to
     * paint the editor. The UI currently uses different techniques to
     * paint the renderers and editors and overriding setBounds() below
     * is not the right thing to do for an editor. Returning -1 for the
     * editing row in this case, ensures the editor is never painted.
     */
    public int getEditingRow() {
        return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 :
                editingRow;
    }

    /**
     * Overridden to pass the new rowHeight to the tree.
     */
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);
        if (tree != null && tree.getRowHeight() != rowHeight) {
            tree.setRowHeight(getRowHeight());
        }
    }

    /**
     * Returns the tree that is being shared between the model.
     */
    public JTree getTree() {
        return tree;
    }


    /**
     * @return optional attribute name that identifies the name of the image to show as tree node
     */
    public final String getIconAttributeName() {
      return iconAttributeName;
    }


    /**
     * Optional attribute name that identifies the name of the image to show as tree node.
     */
    public final void setIconAttributeName(String iconAttributeName) {
      this.iconAttributeName = iconAttributeName;
    }


    /**
     * A TreeCellRenderer that displays a JTree.
     */
    public class TreeTableCellRenderer extends JTree implements TableCellRenderer {

        /** last table/tree row asked to renderer. */
        protected int visibleRow;

        public TreeTableCellRenderer(TreeModel model) {
            super(model);
        }

        /**
         * updateUI is overridden to set the colors of the Tree's renderer
         * to match that of the table.
         */
        public void updateUI() {
            super.updateUI();
            // Make the tree's cell renderer use the table's cell selection
            // colors.
            TreeCellRenderer tcr = getCellRenderer();
            if (tcr instanceof DefaultTreeCellRenderer) {
                DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer)tcr);
                // For 1.1 uncomment this, 1.2 has a bug that will cause an
                // exception to be thrown if the border selection color is
                // null.
                // dtcr.setBorderSelectionColor(null);
                dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
                dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
            }
        }

        /**
         * Sets the row height of the tree, and forwards the row height to
         * the table.
         */
        public void setRowHeight(int rowHeight) {
            if (rowHeight > 0) {
                super.setRowHeight(rowHeight);
                if (TreeGrid.this != null &&
                    TreeGrid.this.getRowHeight() != rowHeight) {
                    TreeGrid.this.setRowHeight(getRowHeight());
                }
            }
        }

        /**
         * This is overridden to set the height to match that of the JTable.
         */
        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, 0, w, TreeGrid.this.getHeight());
        }

        /**
         * Sublcassed to translate the graphics such that the last visible
         * row will be drawn at 0,0.
         */
        public void paint(Graphics g) {
            g.translate(0, -visibleRow * getRowHeight());
            super.paint(g);
        }

        /**
         * TreeCellRenderer method. Overridden to update the visible row.
         */
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row, int column) {
            if(isSelected)
                setBackground(table.getSelectionBackground());
            else
                setBackground(table.getBackground());

            visibleRow = row;
            return this;
        }
    }









    /**
     * <p>Title: OpenSwing Framework</p>
     * <p>Description: Inner class used to render the tree cells.</p>
     * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
     * <p> </p>
     * @author Mauro Carniel
     * @version 1.0
     */
    public class TreeGridNodeRenderer extends DefaultTreeCellRenderer {

      ImageIcon folderIcon = null;
      ImageIcon leafIcon = null;
      TreePanel treePanel;

      /** formatter to use for this column (optional, may be null) */
      private Format formatter;


      /**
       * Costructor.
       * @param tree node container
       */
      public TreeGridNodeRenderer(String folderIconName,String leavesImageName,Format formatter) {
        try {
          this.treePanel = treePanel;
          this.formatter = formatter;
          folderIcon = new ImageIcon(ClientUtils.getImage(folderIconName));
          leafIcon = new ImageIcon(ClientUtils.getImage(leavesImageName));
          this.setOpaque(false);
          this.setBackgroundNonSelectionColor(new java.awt.Color(0,0,0,0));

        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }


      public Component getTreeCellRendererComponent(JTree tree,
                                                    Object value,
                                                    boolean sel,
                                                    boolean expanded,
                                                    boolean leaf,
                                                    int row,
                                                    boolean hasFocus) {
        ValueObject vo = null;
        try {
          super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row,hasFocus);

          DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
          vo = (ValueObject) node.getUserObject();

          if (vo!=null && iconAttributeName!=null) {
            Method getter = vo.getClass().getMethod("get"+iconAttributeName.substring(0,1).toUpperCase()+iconAttributeName.substring(1),new Class[0]);
            value = getter.invoke(vo,new Object[0]);
            if (value!=null) {
              setIcon(new ImageIcon(ClientUtils.getImage(value.toString())));
            }
            else {
              if (leaf)
                setIcon(leafIcon);
              else
                setIcon(folderIcon);
            }
          }
          else {
            if (leaf)
              setIcon(leafIcon);
            else
              setIcon(folderIcon);
          }

        } catch (Exception ex) {
          ex.printStackTrace();
        }
        JLabel l = (JLabel)this;

        try {
          if (vo!=null) {

            Method getter = vo.getClass().getMethod("get"+attributeName.substring(0,1).toUpperCase()+attributeName.substring(1),new Class[0]);
            value = getter.invoke(vo,new Object[0]);
            if (value!=null) {
              if (formatter!=null)
                value = formatter.format(value);
              l.setText(value.toString());

              if (gridColumnAlignments.get(attributeName)!=null) {
                ((JLabel)this).setHorizontalAlignment( ((Integer)gridColumnAlignments.get(attributeName)).intValue() );
              }


            }
          }

        }
        catch (Throwable ex1) {
          ex1.printStackTrace();
        }

        return l;
      }


    } // end inner-class


























    /**
     * TreeTableCellEditor implementation. Component returned is the
     * JTree.
     */
    public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
        public Component getTableCellEditorComponent(JTable table,
                                                     Object value,
                                                     boolean isSelected,
                                                     int r, int c) {
            return tree;
        }

        /**
         * Overridden to return false, and if the event is a mouse event
         * it is forwarded to the tree.<p>
         * The behavior for this is debatable, and should really be offered
         * as a property. By returning false, all keyboard actions are
         * implemented in terms of the table. By returning true, the
         * tree would get a chance to do something with the keyboard
         * events. For the most part this is ok. But for certain keys,
         * such as left/right, the tree will expand/collapse where as
         * the table focus should really move to a different column. Page
         * up/down should also be implemented in terms of the table.
         * By returning false this also has the added benefit that clicking
         * outside of the bounds of the tree node, but still in the tree
         * column will select the row, whereas if this returned true
         * that wouldn't be the case.
         * <p>By returning false we are also enforcing the policy that
         * the tree will never be editable (at least by a key sequence).
         */
        public boolean isCellEditable(EventObject e) {
            if (e instanceof MouseEvent) {
                for (int counter = getColumnCount() - 1; counter >= 0;
                     counter--) {
                    if (getColumnClass(counter) == TreeTableModel.class) {
                        MouseEvent me = (MouseEvent)e;
                        MouseEvent newME = new MouseEvent(tree, me.getID(),
                                   me.getWhen(), me.getModifiers(),
                                   me.getX() - getCellRect(0, counter, true).x,
                                   me.getY(), me.getClickCount(),
                                   me.isPopupTrigger());
                        tree.dispatchEvent(newME);
                        break;
                    }
                }
            }
            return false;
        }
    }


    /**
     * ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel
     * to listen for changes in the ListSelectionModel it maintains. Once
     * a change in the ListSelectionModel happens, the paths are updated
     * in the DefaultTreeSelectionModel.
     */
    class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {
        /** Set to true when we are updating the ListSelectionModel. */
        protected boolean         updatingListSelectionModel;

        public ListToTreeSelectionModelWrapper() {
            super();
            getListSelectionModel().addListSelectionListener
                                    (createListSelectionListener());
        }

        /**
         * Returns the list selection model. ListToTreeSelectionModelWrapper
         * listens for changes to this model and updates the selected paths
         * accordingly.
         */
        ListSelectionModel getListSelectionModel() {
            return listSelectionModel;
        }

        /**
         * This is overridden to set <code>updatingListSelectionModel</code>
         * and message super. This is the only place DefaultTreeSelectionModel
         * alters the ListSelectionModel.
         */
        public void resetRowSelection() {
            if(!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    super.resetRowSelection();
                }
                finally {
                    updatingListSelectionModel = false;
                }
            }
            // Notice how we don't message super if
            // updatingListSelectionModel is true. If
            // updatingListSelectionModel is true, it implies the
            // ListSelectionModel has already been updated and the
            // paths are the only thing that needs to be updated.
        }

        /**
         * Creates and returns an instance of ListSelectionHandler.
         */
        protected ListSelectionListener createListSelectionListener() {
            return new ListSelectionHandler();
        }

        /**
         * If <code>updatingListSelectionModel</code> is false, this will
         * reset the selected paths from the selected rows in the list
         * selection model.
         */
        protected void updateSelectedPathsFromSelectedRows() {
            if(!updatingListSelectionModel) {
                updatingListSelectionModel = true;
                try {
                    // This is way expensive, ListSelectionModel needs an
                    // enumerator for iterating.
                    int        min = listSelectionModel.getMinSelectionIndex();
                    int        max = listSelectionModel.getMaxSelectionIndex();

                    clearSelection();
                    if(min != -1 && max != -1) {
                        for(int counter = min; counter <= max; counter++) {
                            if(listSelectionModel.isSelectedIndex(counter)) {
                                TreePath selPath = tree.getPathForRow(counter);

                                if(selPath != null) {
                                    addSelectionPath(selPath);
                                }
                            }
                        }
                    }
                }
                finally {
                    updatingListSelectionModel = false;
                }
            }
        }

        /**
         * Class responsible for calling updateSelectedPathsFromSelectedRows
         * when the selection of the list changse.
         */
        class ListSelectionHandler implements ListSelectionListener {
            public void valueChanged(ListSelectionEvent e) {
                updateSelectedPathsFromSelectedRows();
            }
        }
    }
}
