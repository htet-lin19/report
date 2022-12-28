// calculate overtime after work time 8 hrs
// calculate midnight within 10PM ~ 5AM
function calculateTime() {
	var std_work_hr = 8 * 60;
	var std_midnightOtTime = 22 * 60;
	var result_work_time="00:00";
	var result_over_time="";
	var result_midnight_overtime="";

	//一日中休暇チェックリストがONにした場合は自動計算処理を通さない
	if (document.getElementById("day")!= null && document.getElementById("day").checked)
	{
		return;
	}
	
	// 勤怠登録対象日
	var chooseDate = document.getElementById("choose_date").value;
	// to get shift employee or not (0 = not shift employee, 1 = shift employee), added by GICM NC 2020/10/21
	var shift_work = document.getElementById("shift_work").value;
	// to convert Date object 
	var day=new Date(chooseDate);
	// calculate start_time
	var start = (document.getElementById("start_time").value).split(':');
	var stime_hr = parseInt(start[0]);
	if (stime_hr == 0) {
		stime_hr = 24;
	}
	var stime_tot_min = Number(stime_hr * 60) + Number(parseInt(start[1]));
	
	// calculate end_time
	var end = (document.getElementById("end_time").value).split(':');
	var etime_hr = parseInt(end[0]);
	if (etime_hr == 0) {
		etime_hr = 24;
	}
	var etime_tot_min = Number(etime_hr * 60) + Number(parseInt(end[1]));
	
	if ((!isNaN(stime_tot_min)) && (!isNaN(etime_tot_min))) {
		if ((document.getElementById("break_time").value) == null
				|| (document.getElementById("break_time").value) == '') {
			break_time = "00:00";
		}
		// calculate break_time
		var btime = (document.getElementById("break_time").value).split(':');
		var btime_tot_min = Number(parseInt(btime[0]) * 60)
				+ Number(parseInt(btime[1]));

		// day duty employees
		if (etime_tot_min > stime_tot_min) {
			
			var midnight_stime = stime_tot_min;
			var midnight_etime = etime_tot_min;
			
			// 12 ~ 5 calculate midnight end time
			tot_midnightEndTime = 5 * 60;
			
			// calculate work_hour
			var workHr_min = (etime_tot_min - (stime_tot_min + btime_tot_min));
			var overtime = 0;
			var midnightOT = 0;

			if (workHr_min > 0) {
				// calculate overtime for day duty employees (Monday ~ Saturday), Added By GICM NC 2020/10/21
				if (workHr_min > std_work_hr) {
					overtime = workHr_min - std_work_hr;
				} else {
					overtime = "";
				}

				// calculate midnight work time
				// start time less than 5 hrs
				if ( midnight_stime < tot_midnightEndTime) {
					if (midnight_etime > std_midnightOtTime) {
						midnight_etime = midnight_etime - std_midnightOtTime;
						midnight_stime  = tot_midnightEndTime - midnight_stime;
						midnightOT = midnight_stime + midnight_etime;
					} else if (midnight_etime <= tot_midnightEndTime) {
						midnightOT = midnight_etime - midnight_stime;
					} else {
						midnightOT = tot_midnightEndTime - midnight_stime;
					}	
					// start time greater than 10 hrs
				} else if ( midnight_stime > std_midnightOtTime){
					midnightOT = midnight_etime - midnight_stime;
				} else {
					// start time not include 10 ~ 5
					if (midnight_etime > std_midnightOtTime) {
						midnightOT = midnight_etime - std_midnightOtTime;
					} else {
						midnightOT = "";
					}
				}			
			} else {
				overtime = "";
				midnightOT = "";
			}
			// night duty employees
		} else if (stime_tot_min > etime_tot_min) {
			// 10 ~ 12 calculate midnight start time
			change_12hr_min = 24 * 60;
			// 12 ~ 5 calculate midnight end time
			tot_midnightEndTime = 5 * 60;
			
			var midnight_stime = stime_tot_min;
			var midnight_etime = etime_tot_min;
			
			stime_hr = stime_hr - 12;
			
			stime_tot_min = Number(stime_hr * 60) + Number(parseInt(start[1]));
			
			etime_hr = etime_hr + 12;
			
			etime_tot_min = Number(etime_hr * 60) + Number(parseInt(end[1]));		
			
			workHr_min = (etime_tot_min - (stime_tot_min + btime_tot_min));
			
			if (workHr_min > 0) {
				// calculate overtime for day duty employees (Monday ~ Saturday), Added By GICM NC 2020/10/21
				if ( workHr_min > std_work_hr) {
					overtime = workHr_min - std_work_hr;
				} else {
					overtime = "";
				}

				// calculate midnight work time
				if (midnight_stime >= change_12hr_min) {
					// greater than 12PM need to reduce 12 ~ 5 time
					midnight_stime = change_12hr_min - midnight_stime;
				} else if (midnight_stime <= std_midnightOtTime) {
					// less than 10PM assign 2 hours
					midnight_stime = 120;
				} else {
					// within 10PM && 12PM
					worktime_over10 = midnight_stime - std_midnightOtTime;
					midnight_stime = 120 - worktime_over10;
				}
				 
				if (midnight_etime > tot_midnightEndTime) {
					 var next_midnight_etime = 0;
					if (midnight_etime > std_midnightOtTime) {
						next_midnight_etime = midnight_etime - std_midnightOtTime;
					}
					midnight_etime = next_midnight_etime + tot_midnightEndTime;
				} 
				
					midnightOT = midnight_stime + midnight_etime;
			} else {
				overtime = "";
				midnightOT = "";
			}			
		} else {
			overtime = "";
			midnightOT = "";
		}
		
		// calculate overtime for shift employees, added by GICM NC 2020/10/21
		if (shift_work==1 && chooseDate!="" && day.getDay()==0 && workHr_min > std_work_hr) {
			overtime = workHr_min-std_work_hr;
		}
		
		if (!isNaN(workHr_min) && (workHr_min > 0)) {
		result_work_time = (('0' + Math.floor(workHr_min / 60)).slice(-2))
				+ ":" + (('0' + (workHr_min % 60)).slice(-2));
		}
		if (overtime != "") {
			result_over_time = (('0' + Math.floor(overtime / 60)).slice(-2))
					+ ":" + (('0' + (overtime % 60)).slice(-2));
		}
		if (midnightOT != "") {
			result_midnight_overtime = (('0' + Math.floor(midnightOT / 60))
					.slice(-2))
					+ ":" + (('0' + (midnightOT % 60)).slice(-2));
		}
	}

	//2020/10/14 GICM NC 勤務時間登録機能追加対応 Start
	document.getElementById("work_hour").value = result_work_time;
	if (day.getDay()==0 && workHr_min > 0 && shift_work==0) { //When Sunday and not shift employee
		document.getElementById("overtime").value ='' ;
		document.getElementById("midnight_overtime").value =result_midnight_overtime;
		document.getElementById("overtime_sunday").value=result_work_time;
	} else {												 //When Monday ~ Saturday and shift employee
		document.getElementById("overtime_sunday").value='';
		document.getElementById("overtime").value = result_over_time;
		document.getElementById("midnight_overtime").value = result_midnight_overtime;
	}
	//2020/10/14 GICM NC 勤務時間登録機能追加対応 End
}
