package demo43;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.io.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.table.java.GridDataLocator;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Grid controller of file frame.
 * It read/write an image gif/jpg file from file system.</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class FileGridFrameController extends GridController implements GridDataLocator {

  private FileGridFrame frame = null;


  public FileGridFrameController() {
    frame = new FileGridFrame(this);
    ClientUtils.centerFrame(frame);
    frame.setVisible(true);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    try {
      FileVO vo = (FileVO)newValueObjects.get(0);

      FileOutputStream out = new FileOutputStream(System.getProperty("user.home")+vo.getFileName());
      byte[] bb = vo.getFile();
      out.write(bb);
      out.close();
      return new VOListResponse(newValueObjects,false,newValueObjects.size());
    }
    catch (Exception ex) {
      return new ErrorResponse("Error on creating the demo value object:\n"+ex.toString());
    }

  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    try {
      FileVO vo = null;
      FileVO oldVO = null;
      FileOutputStream out = null;
      byte[] bb = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (FileVO)persistentObjects.get(i);
        oldVO = (FileVO)oldPersistentObjects.get(i);

        if (oldVO.getFileName()!=null && oldVO.getFileName().equals(vo.getFileName()))
          new File(System.getProperty("user.home")+oldVO.getFileName()).delete();

        out = new FileOutputStream(System.getProperty("user.home")+vo.getFileName());
        bb = vo.getFile();
        out.write(bb);
        out.close();

        if (oldVO.getFileName()!=null)
          new File(oldVO.getFileName()).delete();

      }
      return new VOListResponse(persistentObjects,false,persistentObjects.size());
    }
    catch (Exception ex) {
      return new ErrorResponse("Error on updaing the demo value objects list:\n"+ex.toString());
    }

  }

  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    try {
      FileVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
         vo = (FileVO)persistentObjects.get(i);
         new File(System.getProperty("user.home")+vo.getFileName()).delete();
      }

      return new VOResponse(Boolean.TRUE);
    }
    catch (Exception ex) {
      return new ErrorResponse("Error on deleting the demo value objects list:\n"+ex.toString());
    }
  }



  /**
   * Method invoked by the grid to load a block or rows.
   * @param action fetching versus: PREVIOUS_BLOCK_ACTION, NEXT_BLOCK_ACTION or LAST_BLOCK_ACTION
   * @param startPos start position of data fetching in result set
   * @param filteredColumns filtered columns
   * @param currentSortedColumns sorted columns
   * @param currentSortedVersusColumns ordering versus of sorted columns
   * @param valueObjectType v.o. type
   * @param otherGridParams other grid parameters
   * @return response from the server: an object of type VOListResponse if data loading was successfully completed, or an ErrorResponse onject if some error occours
   */
  public Response loadData(
      int action,
      int startIndex,
      Map filteredColumns,
      ArrayList currentSortedColumns,
      ArrayList currentSortedVersusColumns,
      Class valueObjectType,
      Map otherGridParams) {
    try {
      ArrayList list = new ArrayList();
      FileVO vo = null;
      File f = new File(System.getProperty("user.home"));
      File[] ff = f.listFiles(new FilenameFilter() {

        public boolean accept(File dir, String name) {
          return new File(dir.getAbsoluteFile()+"/"+name).isFile();
        }

      });
      BufferedInputStream in = null;
      for(int i=0;i<ff.length;i++) {
        try {
          vo = new FileVO();
          in = new BufferedInputStream(new FileInputStream(ff[i]));
          byte[] bb = new byte[ (int) ff[i].length()];
          in.read(bb);
          in.close();
          vo.setFile(bb);
          vo.setFileName(ff[i].getName());
          vo.setUploadDate(new Date(ff[i].lastModified()));
          list.add(vo);
        }
        catch (Exception ex1) {
        }
      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Exception ex) {
      return new ErrorResponse("Error on creating the demo value objecst list:\n"+ex.toString());
    }
  }


  /**
   * Callback method invoked each time a cell is edited: this method define if the new value is valid.
   * This method is invoked ONLY if:
   * - the edited value is not equals to the old value OR it has exmplicitely called setCellAt or setValueAt
   * - the cell is editable
   * Default behaviour: cell value is valid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param oldValue old cell value (before cell editing)
   * @param newValue new cell value (just edited)
   * @return <code>true</code> if cell value is valid, <code>false</code> otherwise
   */
  public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
    if (attributeName.equals("file") && newValue!=null)
      frame.getGrid().getVOListTableModel().setField(rowNumber,"uploadDate",new Date());
    return true;
  }


}
