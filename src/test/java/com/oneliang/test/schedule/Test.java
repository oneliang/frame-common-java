package com.oneliang.test.schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.oneliang.util.common.TimeUtil;

public class Test {
	public static void main(String[] args) throws Exception{
		List<String> dateList=new ArrayList<String>();
		List<Long> timeList=new ArrayList<Long>();
		Calendar beginCalendar=Calendar.getInstance();
		Calendar endCalendar=Calendar.getInstance();
		String begin="2011-01-22";
		String end="2011-02-02";
		try {
			Date beginDate=TimeUtil.stringToDate(begin,TimeUtil.YEAR_MONTH_DAY);
			Date endDate=TimeUtil.stringToDate(end, TimeUtil.YEAR_MONTH_DAY);
			beginCalendar.setTime(beginDate);
			endCalendar.setTime(endDate);
			Calendar tempCalendar=Calendar.getInstance();
			tempCalendar.setTime(beginDate);
			while(endCalendar.compareTo(tempCalendar)>=0){
				dateList.add(tempCalendar.get(Calendar.YEAR)+"-"+(tempCalendar.get(Calendar.MONTH)+1)+"-"+tempCalendar.get(Calendar.DATE));
				timeList.add(tempCalendar.getTimeInMillis());
				tempCalendar.add(Calendar.DATE, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar calendar=Calendar.getInstance();
		List<UserTask> userTaskList=new ArrayList<UserTask>();
		UserTask lwx=new UserTask();
		lwx.setName("lwx");
		Task one=new Task();
		one.setName("ONE");
		one.setBeginDate(TimeUtil.stringToDate("2011-01-25",TimeUtil.YEAR_MONTH_DAY));
		one.setEndDate(TimeUtil.stringToDate("2011-01-27",TimeUtil.YEAR_MONTH_DAY));
		calendar.setTime(one.getBeginDate());
		one.setBeginDateInMillis(calendar.getTimeInMillis());
		calendar.setTime(one.getEndDate());
		one.setEndDateInMillis(calendar.getTimeInMillis());
		lwx.addTask(one);
		
		UserTask pjq=new UserTask();
		pjq.setName("pjq");
		Task two=new Task();
		two.setName("TWO");
		two.setBeginDate(TimeUtil.stringToDate("2011-01-26",TimeUtil.YEAR_MONTH_DAY));
		two.setEndDate(TimeUtil.stringToDate("2011-01-29",TimeUtil.YEAR_MONTH_DAY));
		calendar.setTime(two.getBeginDate());
		two.setBeginDateInMillis(calendar.getTimeInMillis());
		calendar.setTime(two.getEndDate());
		two.setEndDateInMillis(calendar.getTimeInMillis());
		pjq.addTask(two);
		
		userTaskList.add(lwx);
		userTaskList.add(pjq);
	}
}
