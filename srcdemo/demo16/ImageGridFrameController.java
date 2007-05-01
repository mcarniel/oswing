package demo16;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.io.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.table.java.GridDataLocator;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Form controller of image detail frame.
 * It read/write an image gif/jpg file from file system.</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class ImageGridFrameController extends GridController implements GridDataLocator {

  private ImageGridFrame frame = null;


  public ImageGridFrameController() {
    frame = new ImageGridFrame(this);
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
      ImageVO vo = (ImageVO)newValueObjects.get(0);

      FileOutputStream out = new FileOutputStream(vo.getImageName());
      byte[] bb = vo.getImage();
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
      ImageVO vo = null;
      ImageVO oldVO = null;
      FileOutputStream out = null;
      byte[] bb = null;
      for(int i=0;i<persistentObjects.size();i++) {
        vo = (ImageVO)persistentObjects.get(i);
        oldVO = (ImageVO)oldPersistentObjects.get(i);

        if (oldVO.getImageName()!=null && oldVO.getImageName().equals(vo.getImageName()))
          new File(oldVO.getImageName()).delete();

        out = new FileOutputStream(vo.getImageName());
        bb = vo.getImage();
        out.write(bb);
        out.close();

        if (oldVO.getImageName()!=null)
          new File(oldVO.getImageName()).delete();

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
      ImageVO vo = null;
      for(int i=0;i<persistentObjects.size();i++) {
         vo = (ImageVO)persistentObjects.get(i);
         new File(vo.getImageName()).delete();
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
      ImageVO vo = null;
      File f = new File(".");
      File[] ff = f.listFiles(new FilenameFilter() {

        public boolean accept(File dir, String name) {
          return name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".jpg");
        }

      });
      FileInputStream in = null;
      for(int i=0;i<ff.length;i++) {
        vo = new ImageVO();
        in = new FileInputStream(ff[i]);
        byte[] bb = new byte[(int)ff[i].length()];
        in.read(bb);
        in.close();
        vo.setImage(bb);
        vo.setImageName(ff[i].getName());
        list.add(vo);
      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Exception ex) {
      return new ErrorResponse("Error on creating the demo value objecst list:\n"+ex.toString());
    }
  }



}
