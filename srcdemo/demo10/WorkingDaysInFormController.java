package demo10;

import java.sql.*;
import java.util.*;

import org.openswing.swing.export.java.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.java.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller for working days of the employee.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * <p> </p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class WorkingDaysInFormController extends WorkingDaysController {

  private EmpDetailFrame frame = null;


  public WorkingDaysInFormController(EmpDetailFrame frame,Connection conn) {
    super(conn);
    this.frame = frame;
  }


  /**
   * Callback method invoked by grid before showing exporting dialog;
   * this method can be overrided to redefine document formats allowed for the grid
   * @return list of available formats; possible values: ExportOptions.XLS_FORMAT, ExportOptions.CSV_FORMAT1, ExportOptions.CSV_FORMAT2, ExportOptions.XML_FORMAT, ExportOptions.XML_FORMAT_FAT, ExportOptions.HTML_FORMAT, ExportOptions.PDF_FORMAT, ExportOptions.RTF_FORMAT; default value: ClientSettings.EXPORTING_FORMATS
   */
  public String[] getExportingFormats() {
    return new String[]{ ExportOptions.PDF_FORMAT };
  }


  /**
   * Callback method invoked by grid when exporting data from grid.
   * @param exportOptions options used to export data; these options can be programmatically changed, in order to customize exporting result
   */
  public void exportGrid(ExportOptions exportOptions) {
    ComponentExportOptions comp = new ComponentExportOptions();
    Object[][] cells = new Object[5][4];
    cells[0][0] = frame.labelEmpCode.getText(); cells[0][1] = frame.controlempCode.getText();
    cells[0][2] = frame.labelSex.getText(); cells[0][3] = frame.controlSex.getValue();

    cells[1][0] = frame.labelFName.getText(); cells[1][1] = frame.controlFName.getText();
    cells[1][2] = frame.labelLName.getText(); cells[1][3] = frame.controlLName.getText();

    cells[2][0] = frame.labelSalary.getText(); cells[2][1] = frame.controlSex.getValue();
    cells[2][2] = frame.labelDate.getText(); cells[2][3] = frame.controlDate.getDate();

    cells[3][0] = frame.labelDeptCode.getText(); cells[3][1] = frame.controlLookup.getValue();
    cells[3][2] = frame.controlDeptDescr.getText();

    cells[4][0] = frame.labelTask.getText(); cells[4][1] = frame.controlCodTask.getValue();
    cells[4][2] = frame.controlTaskDescr.getText();

    comp.setCellsContent(cells);
    exportOptions.getComponentsExportOptions().add(0,comp);
  }


}
