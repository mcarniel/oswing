package demo8;

import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.gantt.client.*;
import org.openswing.swing.client.*;
import java.awt.event.*;
import java.util.Map;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.gantt.java.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Date;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Internal Frame with a gantt control inside.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 * @author Mauro Carniel
 * @version 1.0
 */
public class GanttFrame extends InternalFrame {
  BorderLayout borderLayout1 = new BorderLayout();
  GanttControl gantt = new GanttControl();


  public GanttFrame() {
    setSize(750,500);
    setTitle("Gantt Sample");
    try {

      jbInit();
      GanttDataLocator loc = new GanttDataLocator() {

        /**
         * Method invoked by the grid to load all rows.
         * @param params gantt parameters
         * @return response from the server: an object of type VOListResponse if data loading was successfully completed, or an ErrorResponse onject if some error occours
         */
        public Response loadData(Map params) {
          ArrayList list = new ArrayList();
          GanttWorkingHoursVO whVO = new GanttWorkingHoursVO();
          Calendar cal = Calendar.getInstance();
          cal.set(cal.HOUR_OF_DAY,8);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          whVO.setMorningStartHour(new java.sql.Timestamp(cal.getTimeInMillis()));
          cal.set(cal.HOUR_OF_DAY,12);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          whVO.setMorningEndHour(new java.sql.Timestamp(cal.getTimeInMillis()));
          cal.set(cal.HOUR_OF_DAY,13);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          whVO.setAfternoonStartHour(new java.sql.Timestamp(cal.getTimeInMillis()));
          cal.set(cal.HOUR_OF_DAY,17);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          whVO.setAfternoonEndHour(new java.sql.Timestamp(cal.getTimeInMillis()));

          HashSet set = new HashSet();
          AppointmentVO appVO = new AppointmentVO();
          appVO.setForegroundColor(Color.black);
          appVO.setDescription("job1");
          cal.set(cal.HOUR_OF_DAY,8);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          appVO.setStartDate(new java.sql.Timestamp(cal.getTimeInMillis()));
          cal.set(cal.HOUR_OF_DAY,12);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          appVO.setEndDate(new java.sql.Timestamp(cal.getTimeInMillis()));
          appVO.setEnableDelete(true);
          appVO.setEnableEdit(true);
          set.add(appVO);

          GanttRowVO vo = new GanttRowVO();
          vo.setAppointmentClass(AppointmentVO.class);
          vo.setLegend(new Object[]{"William Smith"});
          vo.setMondayWorkingHours(whVO);
          vo.setTuesdayWorkingHours(whVO);
          vo.setWednesdayWorkingHours(whVO);
          vo.setThursdayWorkingHours(whVO);
          vo.setFridayWorkingHours(whVO);
          vo.setSaturdayWorkingHours(whVO);
          vo.setSundayWorkingHours(whVO);
          vo.setAppointments(set);
          list.add(vo);


          set = new HashSet();
          appVO = new AppointmentVO();
          appVO.setForegroundColor(Color.lightGray);
          appVO.setDescription("job2");
          cal.set(cal.HOUR_OF_DAY,0);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          appVO.setStartDate(new java.sql.Timestamp(cal.getTimeInMillis()));
          cal.set(cal.HOUR_OF_DAY,24);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          appVO.setEndDate(new java.sql.Timestamp(cal.getTimeInMillis()));
          appVO.setEnableDelete(true);
          appVO.setEnableEdit(true);
          set.add(appVO);

          vo = new GanttRowVO();
          vo.setAppointmentClass(AppointmentVO.class);
          vo.setLegend(new Object[]{"John Doe"});
          vo.setMondayWorkingHours(whVO);
          vo.setTuesdayWorkingHours(whVO);
          vo.setWednesdayWorkingHours(whVO);
          vo.setThursdayWorkingHours(whVO);
          vo.setFridayWorkingHours(whVO);
          vo.setSaturdayWorkingHours(whVO);
          vo.setSundayWorkingHours(whVO);
          vo.setAppointments(set);
          list.add(vo);


          set = new HashSet();
          appVO = new AppointmentVO();
          appVO.setForegroundColor(Color.gray);
          appVO.setDescription("job3");
          cal.set(cal.HOUR_OF_DAY,13);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          appVO.setStartDate(new java.sql.Timestamp(cal.getTimeInMillis()));
          cal.set(cal.DAY_OF_MONTH,cal.get(cal.DAY_OF_MONTH)+2);
          cal.set(cal.HOUR_OF_DAY,12);
          cal.set(cal.MINUTE,0);
          cal.set(cal.SECOND,0);
          cal.set(cal.MILLISECOND,0);
          appVO.setEndDate(new java.sql.Timestamp(cal.getTimeInMillis()));
          appVO.setEnableDelete(true);
          appVO.setEnableEdit(true);
          set.add(appVO);

          vo = new GanttRowVO();
          vo.setAppointmentClass(AppointmentVO.class);
          vo.setLegend(new Object[]{"Frank Porter"});
          vo.setMondayWorkingHours(whVO);
          vo.setTuesdayWorkingHours(whVO);
          vo.setWednesdayWorkingHours(whVO);
          vo.setThursdayWorkingHours(whVO);
          vo.setFridayWorkingHours(whVO);
          vo.setSaturdayWorkingHours(whVO);
          vo.setSundayWorkingHours(whVO);
          vo.setAppointments(set);
          list.add(vo);

          return new VOListResponse(list,false,list.size());
        }

      };
      gantt.setGanttDataLocator(loc);
      gantt.init(new String[]{"Employee"});
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    this.getContentPane().add(gantt, BorderLayout.CENTER);
  }



}

