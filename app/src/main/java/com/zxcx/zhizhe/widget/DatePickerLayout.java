package com.zxcx.zhizhe.widget;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.zxcx.zhizhe.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by liuwan on 2016/9/28.
 * 时间选择器控件
 */
public class DatePickerLayout extends FrameLayout {
	
	private static final int MAX_MINUTE = 59;
	private static final int MAX_HOUR = 23;
	private static final int MIN_MINUTE = 0;
	private static final int MIN_HOUR = 0;
	private static final int MAX_MONTH = 12;
	@BindView(R.id.year_pv)
	DatePickerView mYearPv;
	@BindView(R.id.month_pv)
	DatePickerView mMonthPv;
	@BindView(R.id.day_pv)
	DatePickerView mDayPv;
	@BindView(R.id.hour_pv)
	DatePickerView mHourPv;
	@BindView(R.id.hour_text)
	TextView mHourText;
	@BindView(R.id.minute_pv)
	DatePickerView mMinutePv;
	@BindView(R.id.minute_text)
	TextView mMinuteText;
	private Unbinder mUnbinder;
	private int scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
	private boolean canAccess = false;
	private ArrayList<String> year, month, day, hour, minute;
	private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
	private boolean spanYear, spanMon, spanDay, spanHour, spanMin;
	private Calendar selectedCalender, startCalendar, endCalendar;
	
	public DatePickerLayout(@NonNull Context context) {
		super(context);
		initView();
	}
	
	public DatePickerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	public DatePickerLayout(@NonNull Context context, @Nullable AttributeSet attrs,
		int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		mUnbinder.unbind();
		super.onDetachedFromWindow();
	}
	
	public void setStartAndEndTime(String startDate, String endDate) {
		if (isValidDate(startDate, "yyyy-MM-dd HH:mm") && isValidDate(endDate,
			"yyyy-MM-dd HH:mm")) {
			canAccess = true;
			selectedCalender = Calendar.getInstance();
			startCalendar = Calendar.getInstance();
			endCalendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
			try {
				startCalendar.setTime(sdf.parse(startDate));
				endCalendar.setTime(sdf.parse(endDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void initView() {
		View layout = LayoutInflater.from(getContext()).inflate(R.layout.date_picker_layout, this);
		mUnbinder = ButterKnife.bind(layout);
	}
	
	public String getSelectTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
		return sdf.format(selectedCalender.getTime());
	}
	
	private void initParameter() {
		startYear = startCalendar.get(Calendar.YEAR);
		startMonth = startCalendar.get(Calendar.MONTH) + 1;
		startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
		startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
		startMinute = startCalendar.get(Calendar.MINUTE);
		endYear = endCalendar.get(Calendar.YEAR);
		endMonth = endCalendar.get(Calendar.MONTH) + 1;
		endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
		endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
		endMinute = endCalendar.get(Calendar.MINUTE);
		spanYear = startYear != endYear;
		spanMon = (!spanYear) && (startMonth != endMonth);
		spanDay = (!spanMon) && (startDay != endDay);
		spanHour = (!spanDay) && (startHour != endHour);
		spanMin = (!spanHour) && (startMinute != endMinute);
		selectedCalender.setTime(startCalendar.getTime());
	}
	
	private void initTimer() {
		initArrayList();
		if (spanYear) {
			for (int i = startYear; i <= endYear; i++) {
				year.add(String.valueOf(i));
			}
			for (int i = startMonth; i <= MAX_MONTH; i++) {
				month.add(formatTimeUnit(i));
			}
			for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				i++) {
				day.add(formatTimeUnit(i));
			}
			
			if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
				hour.add(formatTimeUnit(startHour));
			} else {
				for (int i = startHour; i <= MAX_HOUR; i++) {
					hour.add(formatTimeUnit(i));
				}
			}
			
			if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
				minute.add(formatTimeUnit(startMinute));
			} else {
				for (int i = startMinute; i <= MAX_MINUTE; i++) {
					minute.add(formatTimeUnit(i));
				}
			}
		} else if (spanMon) {
			year.add(String.valueOf(startYear));
			for (int i = startMonth; i <= endMonth; i++) {
				month.add(formatTimeUnit(i));
			}
			for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				i++) {
				day.add(formatTimeUnit(i));
			}
			
			if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
				hour.add(formatTimeUnit(startHour));
			} else {
				for (int i = startHour; i <= MAX_HOUR; i++) {
					hour.add(formatTimeUnit(i));
				}
			}
			
			if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
				minute.add(formatTimeUnit(startMinute));
			} else {
				for (int i = startMinute; i <= MAX_MINUTE; i++) {
					minute.add(formatTimeUnit(i));
				}
			}
		} else if (spanDay) {
			year.add(String.valueOf(startYear));
			month.add(formatTimeUnit(startMonth));
			for (int i = startDay; i <= endDay; i++) {
				day.add(formatTimeUnit(i));
			}
			
			if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
				hour.add(formatTimeUnit(startHour));
			} else {
				for (int i = startHour; i <= MAX_HOUR; i++) {
					hour.add(formatTimeUnit(i));
				}
			}
			
			if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
				minute.add(formatTimeUnit(startMinute));
			} else {
				for (int i = startMinute; i <= MAX_MINUTE; i++) {
					minute.add(formatTimeUnit(i));
				}
			}
		} else if (spanHour) {
			year.add(String.valueOf(startYear));
			month.add(formatTimeUnit(startMonth));
			day.add(formatTimeUnit(startDay));
			
			if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
				hour.add(formatTimeUnit(startHour));
			} else {
				for (int i = startHour; i <= endHour; i++) {
					hour.add(formatTimeUnit(i));
				}
			}
			
			if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
				minute.add(formatTimeUnit(startMinute));
			} else {
				for (int i = startMinute; i <= MAX_MINUTE; i++) {
					minute.add(formatTimeUnit(i));
				}
			}
		} else if (spanMin) {
			year.add(String.valueOf(startYear));
			month.add(formatTimeUnit(startMonth));
			day.add(formatTimeUnit(startDay));
			hour.add(formatTimeUnit(startHour));
			
			if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
				minute.add(formatTimeUnit(startMinute));
			} else {
				for (int i = startMinute; i <= endMinute; i++) {
					minute.add(formatTimeUnit(i));
				}
			}
		}
		loadComponent();
	}
	
	/**
	 * 将“0-9”转换为“00-09”
	 */
	private String formatTimeUnit(int unit) {
		return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
	}
	
	private void initArrayList() {
		if (year == null) {
			year = new ArrayList<>();
		}
		if (month == null) {
			month = new ArrayList<>();
		}
		if (day == null) {
			day = new ArrayList<>();
		}
		if (hour == null) {
			hour = new ArrayList<>();
		}
		if (minute == null) {
			minute = new ArrayList<>();
		}
		year.clear();
		month.clear();
		day.clear();
		hour.clear();
		minute.clear();
	}
	
	private void loadComponent() {
		mYearPv.setData(year);
		mMonthPv.setData(month);
		mDayPv.setData(day);
		mHourPv.setData(hour);
		mMinutePv.setData(minute);
		mYearPv.setSelected(0);
		mMonthPv.setSelected(0);
		mDayPv.setSelected(0);
		mHourPv.setSelected(0);
		mMinutePv.setSelected(0);
		executeScroll();
	}
	
	private void addListener() {
		mYearPv.setOnSelectListener(new DatePickerView.onSelectListener() {
			@Override
			public void onSelect(String text) {
				selectedCalender.set(Calendar.YEAR, Integer.parseInt(text));
				monthChange();
			}
		});
		
		mMonthPv.setOnSelectListener(new DatePickerView.onSelectListener() {
			@Override
			public void onSelect(String text) {
				selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
				selectedCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1);
				dayChange();
			}
		});
		
		mDayPv.setOnSelectListener(new DatePickerView.onSelectListener() {
			@Override
			public void onSelect(String text) {
				selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text));
				hourChange();
			}
		});
		
		mHourPv.setOnSelectListener(new DatePickerView.onSelectListener() {
			@Override
			public void onSelect(String text) {
				selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text));
				minuteChange();
			}
		});
		
		mMinutePv.setOnSelectListener(new DatePickerView.onSelectListener() {
			@Override
			public void onSelect(String text) {
				selectedCalender.set(Calendar.MINUTE, Integer.parseInt(text));
			}
		});
	}
	
	private void monthChange() {
		month.clear();
		int selectedYear = selectedCalender.get(Calendar.YEAR);
		if (selectedYear == startYear) {
			for (int i = startMonth; i <= MAX_MONTH; i++) {
				month.add(formatTimeUnit(i));
			}
		} else if (selectedYear == endYear) {
			for (int i = 1; i <= endMonth; i++) {
				month.add(formatTimeUnit(i));
			}
		} else {
			for (int i = 1; i <= MAX_MONTH; i++) {
				month.add(formatTimeUnit(i));
			}
		}
		selectedCalender.set(Calendar.MONTH, Integer.parseInt(month.get(0)) - 1);
		mMonthPv.setData(month);
		mMonthPv.setSelected(0);
		executeAnimator(mMonthPv);
		
		mMonthPv.postDelayed(new Runnable() {
			@Override
			public void run() {
				dayChange();
			}
		}, 100);
	}
	
	private void dayChange() {
		day.clear();
		int selectedYear = selectedCalender.get(Calendar.YEAR);
		int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
		if (selectedYear == startYear && selectedMonth == startMonth) {
			for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
				i++) {
				day.add(formatTimeUnit(i));
			}
		} else if (selectedYear == endYear && selectedMonth == endMonth) {
			for (int i = 1; i <= endDay; i++) {
				day.add(formatTimeUnit(i));
			}
		} else {
			for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
				day.add(formatTimeUnit(i));
			}
		}
		selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.get(0)));
		mDayPv.setData(day);
		mDayPv.setSelected(0);
		executeAnimator(mDayPv);
		
		mDayPv.postDelayed(new Runnable() {
			@Override
			public void run() {
				hourChange();
			}
		}, 100);
	}
	
	private void hourChange() {
		if ((scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value) {
			hour.clear();
			int selectedYear = selectedCalender.get(Calendar.YEAR);
			int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
			int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
			if (selectedYear == startYear && selectedMonth == startMonth
				&& selectedDay == startDay) {
				for (int i = startHour; i <= MAX_HOUR; i++) {
					hour.add(formatTimeUnit(i));
				}
			} else if (selectedYear == endYear && selectedMonth == endMonth
				&& selectedDay == endDay) {
				for (int i = MIN_HOUR; i <= endHour; i++) {
					hour.add(formatTimeUnit(i));
				}
			} else {
				for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
					hour.add(formatTimeUnit(i));
				}
			}
			selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour.get(0)));
			mHourPv.setData(hour);
			mHourPv.setSelected(0);
			executeAnimator(mHourPv);
		}
		
		mHourPv.postDelayed(new Runnable() {
			@Override
			public void run() {
				minuteChange();
			}
		}, 100);
	}
	
	private void minuteChange() {
		if ((scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value) {
			minute.clear();
			int selectedYear = selectedCalender.get(Calendar.YEAR);
			int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
			int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
			int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
			if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay
				&& selectedHour == startHour) {
				for (int i = startMinute; i <= MAX_MINUTE; i++) {
					minute.add(formatTimeUnit(i));
				}
			} else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay
				&& selectedHour == endHour) {
				for (int i = MIN_MINUTE; i <= endMinute; i++) {
					minute.add(formatTimeUnit(i));
				}
			} else {
				for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
					minute.add(formatTimeUnit(i));
				}
			}
			selectedCalender.set(Calendar.MINUTE, Integer.parseInt(minute.get(0)));
			mMinutePv.setData(minute);
			mMinutePv.setSelected(0);
			executeAnimator(mMinutePv);
		}
		executeScroll();
	}
	
	private void executeAnimator(View view) {
		PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
		PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
		PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
		ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
	}
	
	private void executeScroll() {
		mYearPv.setCanScroll(year.size() > 1);
		mMonthPv.setCanScroll(month.size() > 1);
		mDayPv.setCanScroll(day.size() > 1);
		mHourPv.setCanScroll(
			hour.size() > 1 && (scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value);
		mMinutePv.setCanScroll(
			minute.size() > 1
				&& (scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value);
	}
	
	private int disScrollUnit(SCROLL_TYPE... scroll_types) {
		if (scroll_types == null || scroll_types.length == 0) {
			scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
		} else {
			for (SCROLL_TYPE scroll_type : scroll_types) {
				scrollUnits ^= scroll_type.value;
			}
		}
		return scrollUnits;
	}
	
	public void setNow(String time) {
		if (canAccess) {
			if (isValidDate(time, "yyyy-MM-dd")) {
				if (startCalendar.getTime().getTime() < endCalendar.getTime().getTime()) {
					canAccess = true;
					initParameter();
					initTimer();
					addListener();
					setSelectedTime(time);
				}
			} else {
				canAccess = false;
			}
		}
	}
	
	/**
	 * 设置日期控件是否显示时和分
	 */
	public void showSpecificTime(boolean show) {
		if (canAccess) {
			if (show) {
				disScrollUnit();
				mHourPv.setVisibility(View.VISIBLE);
				mHourText.setVisibility(View.VISIBLE);
				mMinutePv.setVisibility(View.VISIBLE);
				mMinuteText.setVisibility(View.VISIBLE);
			} else {
				disScrollUnit(SCROLL_TYPE.HOUR, SCROLL_TYPE.MINUTE);
				mHourPv.setVisibility(View.GONE);
				mHourText.setVisibility(View.GONE);
				mMinutePv.setVisibility(View.GONE);
				mMinuteText.setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * 设置日期控件是否可以循环滚动
	 */
	public void setIsLoop(boolean isLoop) {
		if (canAccess) {
			this.mYearPv.setIsLoop(isLoop);
			this.mMonthPv.setIsLoop(isLoop);
			this.mDayPv.setIsLoop(isLoop);
			this.mHourPv.setIsLoop(isLoop);
			this.mMinutePv.setIsLoop(isLoop);
		}
	}
	
	/**
	 * 设置日期控件默认选中的时间
	 */
	public void setSelectedTime(String time) {
		if (canAccess) {
			String[] str = time.split(" ");
			String[] dateStr = str[0].split("-");
			
			mYearPv.setSelected(dateStr[0]);
			selectedCalender.set(Calendar.YEAR, Integer.parseInt(dateStr[0]));
			
			month.clear();
			int selectedYear = selectedCalender.get(Calendar.YEAR);
			if (selectedYear == startYear) {
				for (int i = startMonth; i <= MAX_MONTH; i++) {
					month.add(formatTimeUnit(i));
				}
			} else if (selectedYear == endYear) {
				for (int i = 1; i <= endMonth; i++) {
					month.add(formatTimeUnit(i));
				}
			} else {
				for (int i = 1; i <= MAX_MONTH; i++) {
					month.add(formatTimeUnit(i));
				}
			}
			mMonthPv.setData(month);
			mMonthPv.setSelected(dateStr[1]);
			selectedCalender.set(Calendar.MONTH, Integer.parseInt(dateStr[1]) - 1);
			executeAnimator(mMonthPv);
			
			day.clear();
			int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
			if (selectedYear == startYear && selectedMonth == startMonth) {
				for (int i = startDay;
					i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
					day.add(formatTimeUnit(i));
				}
			} else if (selectedYear == endYear && selectedMonth == endMonth) {
				for (int i = 1; i <= endDay; i++) {
					day.add(formatTimeUnit(i));
				}
			} else {
				for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
					i++) {
					day.add(formatTimeUnit(i));
				}
			}
			mDayPv.setData(day);
			mDayPv.setSelected(dateStr[2]);
			selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr[2]));
			executeAnimator(mDayPv);
			
			if (str.length == 2) {
				String[] timeStr = str[1].split(":");
				
				if ((scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value) {
					hour.clear();
					int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
					if (selectedYear == startYear && selectedMonth == startMonth
						&& selectedDay == startDay) {
						for (int i = startHour; i <= MAX_HOUR; i++) {
							hour.add(formatTimeUnit(i));
						}
					} else if (selectedYear == endYear && selectedMonth == endMonth
						&& selectedDay == endDay) {
						for (int i = MIN_HOUR; i <= endHour; i++) {
							hour.add(formatTimeUnit(i));
						}
					} else {
						for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
							hour.add(formatTimeUnit(i));
						}
					}
					mHourPv.setData(hour);
					mHourPv.setSelected(timeStr[0]);
					selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr[0]));
					executeAnimator(mHourPv);
				}
				
				if ((scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value) {
					minute.clear();
					int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
					int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
					if (selectedYear == startYear && selectedMonth == startMonth
						&& selectedDay == startDay
						&& selectedHour == startHour) {
						for (int i = startMinute; i <= MAX_MINUTE; i++) {
							minute.add(formatTimeUnit(i));
						}
					} else if (selectedYear == endYear && selectedMonth == endMonth
						&& selectedDay == endDay
						&& selectedHour == endHour) {
						for (int i = MIN_MINUTE; i <= endMinute; i++) {
							minute.add(formatTimeUnit(i));
						}
					} else {
						for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
							minute.add(formatTimeUnit(i));
						}
					}
					mMinutePv.setData(minute);
					mMinutePv.setSelected(timeStr[1]);
					selectedCalender.set(Calendar.MINUTE, Integer.parseInt(timeStr[1]));
					executeAnimator(mMinutePv);
				}
			}
			executeScroll();
		}
	}
	
	/**
	 * 验证字符串是否是一个合法的日期格式
	 */
	private boolean isValidDate(String date, String template) {
		boolean convertSuccess = true;
		// 指定日期格式
		SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
			format.setLenient(false);
			format.parse(date);
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}
	
	public enum SCROLL_TYPE {
		HOUR(1),
		MINUTE(2);
		
		public int value;
		
		SCROLL_TYPE(int value) {
			this.value = value;
		}
	}
	
}
