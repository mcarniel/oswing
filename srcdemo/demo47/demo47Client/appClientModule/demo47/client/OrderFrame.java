package demo47.client;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

import org.openswing.swing.client.*;
import org.openswing.swing.message.send.java.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupDataLocator;
import org.openswing.swing.lookup.client.LookupListener;

import java.awt.event.*;
import java.util.*;

import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.tree.java.OpenSwingTreeNode;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.util.java.Consts;

import demo47.java.Doc01Orders;
import demo47.server.ArticlesRemote;
import demo47.server.CustomersRemote;
import demo47.server.OrdersRemote;
import demo47.server.PricelistsRemote;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid Frame for orders.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class OrderFrame extends InternalFrame {

  GridControl grid = new GridControl();
  JPanel detailButtonsPanel = new JPanel();
  JPanel gridButtonsPanel = new JPanel();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ExportButton exportButton = new ExportButton();
  FlowLayout flowLayout1 = new FlowLayout();
  FlowLayout flowLayout2 = new FlowLayout();
  IntegerColumn colOrderRowNr = new IntegerColumn();
  IntegerColumn colQty = new IntegerColumn();
  TextColumn colItemDescr = new TextColumn();
  CurrencyColumn colUnitPrice = new CurrencyColumn();
  CurrencyColumn colTotal = new CurrencyColumn();
  TextColumn colNote = new TextColumn();
  CodLookupColumn colItemId = new CodLookupColumn();
  JPanel detailPanel = new JPanel();
  Form form = new Form();
  JPanel gridPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();

  InsertButton insert2Button = new InsertButton();
  ReloadButton reload2Button = new ReloadButton();
  EditButton edit2Button = new EditButton();
  SaveButton save2Button = new SaveButton();
  DeleteButton delete2Button = new DeleteButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelYear = new LabelControl();
  LabelControl labelNote = new LabelControl();
  LabelControl labelNr = new LabelControl();
  NumericControl controlYear = new NumericControl();
  NumericControl controlNr = new NumericControl();
  LabelControl labelDate = new LabelControl();
  DateControl controlDate = new DateControl();
  LabelControl labelCust = new LabelControl();
  LabelControl labelPricelist = new LabelControl();
  ComboBoxControl controlState = new ComboBoxControl();
  LabelControl labelState = new LabelControl();
  TextAreaControl controlNote = new TextAreaControl();
  LabelControl labelTotal = new LabelControl();
  CurrencyControl controlTotal = new CurrencyControl();
  CodLookupControl controlCustomer = new CodLookupControl();
  TextControl controlCustDesc = new TextControl();
  CodLookupControl controlPricelist = new CodLookupControl();
  TextControl controlPricelistDesc = new TextControl();
  NavigatorBar navigatorBar = new NavigatorBar();
  private OrdersFrame parentFrame = null;
  

  public OrderFrame(OrdersFrame parentFrame,OrderController controller) {
	this.parentFrame = parentFrame;
    try {
      jbInit();
      setSize(800,500);
      
      
      OrderRowsController gridController = new OrderRowsController(this);
      grid.setController(gridController);
      grid.setGridDataLocator(gridController);

      form.setFormController(controller);
      
      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("pk.docNumber");
      pk.add("pk.docYear"); 
      form.linkGrid(parentFrame.getGrid(),pk,true,true,true,navigatorBar);
      
      LookupController lookupControllerCustomer = new LookupController();
      lookupControllerCustomer.setLookupValueObjectClassName("demo47.java.Doc02Customers");
      lookupControllerCustomer.addLookup2ParentLink("customerId","customerId.customerId"); // necessario per l'auto-completition...
//      lookupControllerCustomer.addLookup2ParentLink("description","customerId.description");      
      lookupControllerCustomer.addLookup2ParentLink("customerId");
      lookupControllerCustomer.setVisibleColumn("customerId", true);
      lookupControllerCustomer.setVisibleColumn("description", true);     
      lookupControllerCustomer.setHeaderColumnName("customerId", "Customer Id");
      lookupControllerCustomer.setHeaderColumnName("description", "Description");     
      lookupControllerCustomer.setFilterableColumn("customerId", true);
      lookupControllerCustomer.setSortedColumn("customerId", Consts.ASC_SORTED);
      lookupControllerCustomer.addLookupListener(new LookupListener() {

		@Override
		public void beforeLookupAction(ValueObject arg0) {
		}

		@Override
		public void codeChanged(ValueObject arg0, Collection arg1) {
			Doc01Orders vo = (Doc01Orders)form.getVOModel().getValueObject();
			controlPricelist.setValue(null);
			controlPricelistDesc.setValue(null);
			vo.setDoc03Pricelists(null);
		}

		@Override
		public void codeValidated(boolean arg0) {
		}

		@Override
		public void forceValidate() {
		}
    	  
      });
      controlCustomer.setLookupController(lookupControllerCustomer);
      lookupControllerCustomer.setLookupDataLocator(new LookupDataLocator() {

          /**
           * Method called by lookup controller when validating code.
           * @param code code to validate
           * @return code validation response: VOListResponse if code validation has success, ErrorResponse otherwise
           */
          public Response validateCode(String code) {
            try {
            	FilterWhereClause filter = new FilterWhereClause("customerId","=",code);
            	GridParams gridParams = new GridParams();
            	gridParams.getFilteredColumns().put("customerId", new FilterWhereClause[] {filter,null} );
            	CustomersRemote customersService = (CustomersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("CustomersBean");
            	return customersService.getCustomers(gridParams);
            } catch (Exception ex) {
              ex.printStackTrace();
              return new ErrorResponse(ex.getMessage());
            }
          }

          /**
           * Method called by lookup controller when user clicks on lookup button.
           * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
               * @param startIndex current index row on grid to use to start fetching data
           * @param filteredColumns filtered columns
           * @param currentSortedColumns sorted columns
           * @param currentSortedVersusColumns ordering versus of sorted columns
               * @param valueObjectType type of value object associated to the lookup grid
           * @return list of value objects to fill in the lookup grid: VOListResponse if data fetching has success, ErrorResponse otherwise
           */
          public Response loadData(
              int action,
              int startIndex,
              Map filteredColumns,
              ArrayList currentSortedColumns,
              ArrayList currentSortedVersusColumns,
              Class valueObjectType
              ) {
            try {
            	CustomersRemote customersService = (CustomersRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("CustomersBean");
            	return customersService.getCustomers(
            	  new GridParams(
        		      action,
        		      startIndex,
        		      filteredColumns,
        		      currentSortedColumns,
        		      currentSortedVersusColumns,
        		      new HashMap()
            	  )
            	);
            }
            catch (Exception ex) {
              ex.printStackTrace();
              return new ErrorResponse(ex.getMessage());
            }
          }


          /**
           * Method called by the TreePanel to fill the tree.
           * @return a VOReponse containing a DefaultTreeModel object
           */
          public Response getTreeModel(JTree tree) {
            return new VOResponse(new DefaultTreeModel(new OpenSwingTreeNode()));
          }


      });

      
      LookupController lookupControllerPricelist = new LookupController();
      lookupControllerPricelist.setLookupValueObjectClassName("demo47.java.Doc03Pricelists");
      lookupControllerPricelist.addLookup2ParentLink("pk.pricelistId","customerId.customerId"); // necessario per l'auto-completition...
//      lookupControllerPricelist.addLookup2ParentLink("description","customerId.description");      
      lookupControllerPricelist.addLookup2ParentLink("doc03Pricelists");
      lookupControllerPricelist.setVisibleColumn("pk.pricelistId", true);
      lookupControllerPricelist.setVisibleColumn("description", true);
      lookupControllerPricelist.setHeaderColumnName("pk.pricelistId", "Pricelist Id");
      lookupControllerPricelist.setHeaderColumnName("description", "Description");     
      lookupControllerPricelist.setFilterableColumn("pk.pricelistId", true);
      lookupControllerPricelist.setSortedColumn("pk.pricelistId", Consts.ASC_SORTED);
      controlPricelist.setLookupController(lookupControllerPricelist);
      lookupControllerPricelist.setLookupDataLocator(new LookupDataLocator() {

          /**
           * Method called by lookup controller when validating code.
           * @param code code to validate
           * @return code validation response: VOListResponse if code validation has success, ErrorResponse otherwise
           */
          public Response validateCode(String code) {
            try {
            	Doc01Orders vo = (Doc01Orders)form.getVOModel().getValueObject();
            	FilterWhereClause filter = new FilterWhereClause("pk.pricelistId","=",code);
            	GridParams gridParams = new GridParams();
            	gridParams.getFilteredColumns().put("pk.pricelistId", new FilterWhereClause[] {filter,null} );
            	PricelistsRemote pricelistsService = (PricelistsRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("PricelistsBean");
            	return pricelistsService.getPricelists(vo.getCustomerId(),gridParams);
            } catch (Exception ex) {
              ex.printStackTrace();
              return new ErrorResponse(ex.getMessage());
            }
          }

          /**
           * Method called by lookup controller when user clicks on lookup button.
           * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
               * @param startIndex current index row on grid to use to start fetching data
           * @param filteredColumns filtered columns
           * @param currentSortedColumns sorted columns
           * @param currentSortedVersusColumns ordering versus of sorted columns
               * @param valueObjectType type of value object associated to the lookup grid
           * @return list of value objects to fill in the lookup grid: VOListResponse if data fetching has success, ErrorResponse otherwise
           */
          public Response loadData(
              int action,
              int startIndex,
              Map filteredColumns,
              ArrayList currentSortedColumns,
              ArrayList currentSortedVersusColumns,
              Class valueObjectType
              ) {
            try {
            	Doc01Orders vo = (Doc01Orders)form.getVOModel().getValueObject();
            	PricelistsRemote pricelistsService = (PricelistsRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("PricelistsBean");
            	return pricelistsService.getPricelists(
            	  vo.getCustomerId(),
            	  new GridParams(
        		      action,
        		      startIndex,
        		      filteredColumns,
        		      currentSortedColumns,
        		      currentSortedVersusColumns,
        		      new HashMap()
            	  )
            	);
            }
            catch (Exception ex) {
              ex.printStackTrace();
              return new ErrorResponse(ex.getMessage());
            }
          }


          /**
           * Method called by the TreePanel to fill the tree.
           * @return a VOReponse containing a DefaultTreeModel object
           */
          public Response getTreeModel(JTree tree) {
            return new VOResponse(new DefaultTreeModel(new OpenSwingTreeNode()));
          }


      });

      LookupController lookupControllerItem = new LookupController();
      lookupControllerItem.setLookupValueObjectClassName("demo47.java.Doc05Articles");
      lookupControllerItem.addLookup2ParentLink("itemId","itemId.itemId"); // necessario per l'auto-completition...
//      lookupControllerItem.addLookup2ParentLink("descrizione","itemId.descrizione");      
      lookupControllerItem.addLookup2ParentLink("defaultUnitPrice","unitPrice"); // necessario per l'auto-completition...
      lookupControllerItem.addLookup2ParentLink("itemId");
      lookupControllerItem.setVisibleColumn("itemId", true);
      lookupControllerItem.setVisibleColumn("descrizione", true);
      lookupControllerItem.setFilterableColumn("itemId", true);
      lookupControllerItem.setSortedColumn("itemId", Consts.ASC_SORTED);
      lookupControllerItem.setHeaderColumnName("itemId", "Item Id");
      lookupControllerItem.setHeaderColumnName("descrizione", "Description");     
      colItemId.setLookupController(lookupControllerItem);
      lookupControllerItem.setLookupDataLocator(new LookupDataLocator() {

          /**
           * Method called by lookup controller when validating code.
           * @param code code to validate
           * @return code validation response: VOListResponse if code validation has success, ErrorResponse otherwise
           */
          public Response validateCode(String code) {
            try {
            	FilterWhereClause filter = new FilterWhereClause("itemId","=",code);
            	GridParams gridParams = new GridParams();
            	gridParams.getFilteredColumns().put("itemId", new FilterWhereClause[] {filter,null} );
            	ArticlesRemote articlesService = (ArticlesRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("ArticlesBean");
            	return articlesService.getArticles(gridParams);
            } catch (Exception ex) {
              ex.printStackTrace();
              return new ErrorResponse(ex.getMessage());
            }
          }

          /**
           * Method called by lookup controller when user clicks on lookup button.
           * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
               * @param startIndex current index row on grid to use to start fetching data
           * @param filteredColumns filtered columns
           * @param currentSortedColumns sorted columns
           * @param currentSortedVersusColumns ordering versus of sorted columns
               * @param valueObjectType type of value object associated to the lookup grid
           * @return list of value objects to fill in the lookup grid: VOListResponse if data fetching has success, ErrorResponse otherwise
           */
          public Response loadData(
              int action,
              int startIndex,
              Map filteredColumns,
              ArrayList currentSortedColumns,
              ArrayList currentSortedVersusColumns,
              Class valueObjectType
              ) {
            try {
            	ArticlesRemote articlesService = (ArticlesRemote)((DemoClientFacade)MDIFrame.getClientFacade()).getRemote("ArticlesBean");
            	return articlesService.getArticles(
            	  new GridParams(
        		      action,
        		      startIndex,
        		      filteredColumns,
        		      currentSortedColumns,
        		      currentSortedVersusColumns,
        		      new HashMap()
            	  )
            	);
            }
            catch (Exception ex) {
              ex.printStackTrace();
              return new ErrorResponse(ex.getMessage());
            }
          }


          /**
           * Method called by the TreePanel to fill the tree.
           * @return a VOReponse containing a DefaultTreeModel object
           */
          public Response getTreeModel(JTree tree) {
            return new VOResponse(new DefaultTreeModel(new OpenSwingTreeNode()));
          }


      });

      setTitle("Order");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  
  public Form getForm() {
	  return form;
  } 

  
  public GridControl getGrid() {
	  return grid;
  } 
  

  public void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    System.currentTimeMillis();
    gridButtonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);

    controlYear.setAttributeName("pk.docYear");
    controlNr.setAttributeName("pk.docNumber");
    controlDate.setAttributeName("docDate");
    controlState.setAttributeName("state");
    controlNote.setAttributeName("note");
    controlTotal.setAttributeName("total");
    controlCustomer.setAttributeName("customerId.customerId");
    controlCustDesc.setAttributeName("customerId.description");
    controlPricelist.setAttributeName("doc03Pricelists.pk.pricelistId");
    controlPricelistDesc.setAttributeName("doc03Pricelists.description");
    controlCustomer.setRequired(true);
    controlPricelist.setRequired(true);
    controlPricelist.setMaxCharacters(20);

    controlNr.setLinkLabel(labelNr);
    controlDate.setLinkLabel(labelDate);
    controlState.setLinkLabel(labelState);
    controlNote.setLinkLabel(labelNote);
    controlTotal.setLinkLabel(labelTotal);
    controlCustomer.setLinkLabel(labelCust);
    controlPricelist.setLinkLabel(labelPricelist);

    
    form.setInsertButton(insert2Button);
    form.setReloadButton(reload2Button);
    form.setSaveButton(save2Button);
    form.setEditButton(edit2Button);
    form.setDeleteButton(delete2Button);
    form.setVOClassName("demo47.java.Doc01Orders");
    form.setLayout(gridBagLayout1);

    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton);
    grid.setInsertButton(insertButton);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    grid.setEditButton(editButton);
    grid.setValueObjectClassName("demo47.java.Doc04OrderRows");
    grid.setCreateInnerVO(true);
    gridPanel.setLayout(borderLayout1);
    detailPanel.setLayout(borderLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    labelYear.setLabel("Year");
    labelNote.setLabel("Note");
    labelNr.setLabel("Order Nr");
    labelDate.setLabel("Order Date");
    labelCust.setLabel("Customer");
    labelPricelist.setLabel("Pricelist");
    labelState.setLabel("State");
    labelTotal.setLabel("Total Amount");
    controlYear.setRequired(true);
    controlYear.setEnabledOnEdit(false);
    controlNr.setRequired(true);
    controlNr.setEnabledOnEdit(false);
    controlDate.setRequired(true);
    controlCustomer.setMaxCharacters(20);
    controlCustDesc.setEnabledOnInsert(false);
    controlCustDesc.setEnabledOnEdit(false);
    controlPricelistDesc.setEnabledOnInsert(false);
    controlPricelistDesc.setEnabledOnEdit(false);
    controlState.setDomainId("ORDER_STATE");
    controlState.setRequired(true);
    controlTotal.setEnabledOnEdit(false);
    controlTotal.setEnabledOnInsert(false);
    gridPanel.add(gridButtonsPanel,BorderLayout.NORTH);
    gridPanel.add(grid,BorderLayout.CENTER);
    detailPanel.add(detailButtonsPanel,BorderLayout.NORTH);
    detailPanel.add(form,BorderLayout.CENTER);
    form.add(labelYear,         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(labelNote,         new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    form.add(labelNr,       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlYear,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlNr,      new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(labelDate,      new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlDate,      new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(labelCust,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(labelPricelist,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(labelState,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlState,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlNote,     new GridBagConstraints(1, 4, 5, 2, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 40));
    form.add(labelTotal,   new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlTotal,  new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailButtonsPanel.setLayout(flowLayout2);
    this.getContentPane().add(gridPanel, BorderLayout.CENTER);
    this.getContentPane().add(detailPanel, BorderLayout.NORTH);
    gridButtonsPanel.add(insertButton, null);
    gridButtonsPanel.add(editButton, null);
    gridButtonsPanel.add(saveButton, null);
    gridButtonsPanel.add(reloadButton, null);
    gridButtonsPanel.add(exportButton, null);
    gridButtonsPanel.add(deleteButton, null);
    grid.add(colOrderRowNr, null);
    grid.add(colItemId, null);
    grid.add(colItemDescr, null);
    grid.add(colQty, null);
    grid.add(colUnitPrice, null);
    grid.add(colTotal, null);
    grid.add(colNote, null);
    
    grid.setAutoLoadData(false);

    colItemId.setColumnName("itemId.itemId");
    colItemId.setHeaderColumnName("Item Id");
    colItemId.setPreferredWidth(100);
    colItemId.setEditableOnEdit(false);
    colItemId.setEditableOnInsert(true);
    colItemId.setColumnFilterable(true);
    colItemDescr.setColumnName("itemId.descrizione");
    colItemDescr.setHeaderColumnName("Description");
    colItemDescr.setPreferredWidth(200);
    colItemDescr.setEditableOnEdit(false);
    colItemDescr.setEditableOnInsert(false);
    colTotal.setColumnName("total");
    colTotal.setHeaderColumnName("Total");
    colTotal.setPreferredWidth(80);
    colTotal.setEditableOnEdit(false);
    colTotal.setEditableOnInsert(false);
    colNote.setColumnName("note");
    colNote.setHeaderColumnName("Note");
    colNote.setPreferredWidth(250);
    colNote.setEditableOnEdit(true);
    colNote.setEditableOnInsert(true);
    colNote.setColumnRequired(false);

    colOrderRowNr.setColumnName("pk.rowNumber");
    colOrderRowNr.setHeaderColumnName("Nr");
    colOrderRowNr.setPreferredWidth(50);
    colOrderRowNr.setSortingOrder(1);
    colOrderRowNr.setSortVersus(Consts.ASC_SORTED);
    colOrderRowNr.setColumnVisible(false);
    colOrderRowNr.setColumnSelectable(false);
    colQty.setColumnName("qty");
    colQty.setHeaderColumnName("Qty");
    colQty.setEditableOnEdit(true);
    colQty.setEditableOnInsert(true);
    colQty.setColumnRequired(true);
    colQty.setPreferredWidth(50);
    colUnitPrice.setColumnName("unitPrice");
    colUnitPrice.setHeaderColumnName("Unit Price");
    colQty.setEditableOnEdit(true);
    colQty.setEditableOnInsert(true);
    colUnitPrice.setPreferredWidth(80);
    colUnitPrice.setEditableOnEdit(true);
    colUnitPrice.setEditableOnInsert(true);

    detailButtonsPanel.add(insert2Button, null);
    detailButtonsPanel.add(edit2Button, null);
    detailButtonsPanel.add(save2Button, null);
    detailButtonsPanel.add(reload2Button, null);
    detailButtonsPanel.add(delete2Button, null);
    detailButtonsPanel.add(navigatorBar, null);
    form.add(controlCustomer,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlCustDesc,   new GridBagConstraints(2, 1, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    form.add(controlPricelist,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlPricelistDesc,  new GridBagConstraints(2, 2, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

  }

  
  public void setButtonsEnabled(boolean enabled) {
	    deleteButton.setEnabled(enabled);
	    insertButton.setEnabled(enabled);
	    reloadButton.setEnabled(enabled);
	    editButton.setEnabled(enabled);
	    exportButton.setEnabled(enabled);
	    if (!enabled)
	      saveButton.setEnabled(false);
	  }  

}

