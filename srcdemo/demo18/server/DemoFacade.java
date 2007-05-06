package demo18.server;

import org.openswing.swing.message.send.java.GridParams;
import org.springframework.dao.DataAccessException;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import demo18.java.EmpVO;
import org.openswing.swing.message.send.java.LookupValidationParams;

/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Demo main facade: contains references to all DAOs.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @version 1.0
 */
public class DemoFacade {

  private TaskDao taskDao;

  private DeptDao deptDao;

  private EmpDao empDao;


  public DemoFacade() {
  }


  public DeptDao getDeptDao() {
    return deptDao;
  }
  public EmpDao getEmpDao() {
    return empDao;
  }
  public TaskDao getTaskDao() {
    return taskDao;
  }
  public void setTaskDao(TaskDao taskDao) {
    this.taskDao = taskDao;
  }
  public void setEmpDao(EmpDao empDao) {
    this.empDao = empDao;
  }
  public void setDeptDao(DeptDao deptDao) {
    this.deptDao = deptDao;
  }


  public Response getTasksList(GridParams gridParams) throws DataAccessException {
    return taskDao.getTasksList(gridParams);
  }


  public Response validateTask(LookupValidationParams lookupParams) throws DataAccessException {
    return taskDao.validateTask(lookupParams);
  }


  public Response insertTasks(ArrayList persistentObjects) throws DataAccessException {
    return taskDao.insertTasks(persistentObjects);
  }


  public Response updateTasks(ArrayList oldPersistentObjects,ArrayList newPersistentObjects) throws DataAccessException {
    return taskDao.updateTasks(oldPersistentObjects,newPersistentObjects);
  }


  public Response deleteTasks(ArrayList persistentObjects) throws DataAccessException {
    return taskDao.deleteTasks(persistentObjects);
  }


  public Response getDeptsList(GridParams gridParams) throws DataAccessException {
    return deptDao.getDeptsList(gridParams);
  }


  public Response validateDept(LookupValidationParams lookupParams) throws DataAccessException {
    return deptDao.validateDept(lookupParams);
  }


  public Response insertDepts(ArrayList persistentObjects) throws DataAccessException {
    return deptDao.insertDepts(persistentObjects);
  }


  public Response updateDepts(ArrayList oldPersistentObjects,ArrayList newPersistentObjects) throws DataAccessException {
    return deptDao.updateDepts(oldPersistentObjects,newPersistentObjects);
  }


  public Response deleteDepts(ArrayList persistentObjects) throws DataAccessException {
    return deptDao.deleteDepts(persistentObjects);
  }


  public Response getEmpsList(GridParams gridParams) throws DataAccessException {
    return empDao.getEmpsList(gridParams);
  }


  public Response getEmp(String empCode) throws DataAccessException {
    return empDao.getEmp(empCode);
  }


  public Response insertEmp(EmpVO vo) throws DataAccessException {
    return empDao.insertEmp(vo);
  }


  public Response updateEmp(EmpVO oldPersistentObject,EmpVO newPersistentObject) throws DataAccessException {
    return empDao.updateEmp(oldPersistentObject,newPersistentObject);
  }


  public Response deleteEmps(ArrayList persistentObjects) throws DataAccessException {
    return empDao.deleteEmps(persistentObjects);
  }


}
