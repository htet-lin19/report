/**
 * 
 */
function calculateTime() {
	var std_work_hr = 8 * 60;
	var std_midnightOtTime = 22 * 60;
	var result_work_time="";
	var result_over_time="";
	var result_midnight_overtime="";
	
	//一日中休暇チェックリストがONにした場合は自動計算処理を通さない
	if (document.getElementById("day")!= null && document.getElementById("day").checked)
	{
		return;
	}
	
	// calculate start_time
	var start = (document.getElementById("start_time").value).split(':');
	var stime_tot_min = Number(parseInt(start[0]) * 60)
			+ Number(parseInt(start[1]));

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

		// calculate work_hour
		var workHr_min = (etime_tot_min - (stime_tot_min + btime_tot_min));
		var overtime = 0;
		var midnightOT = 0;

		if (workHr_min > 0) {
			if ((etime_tot_min >= std_midnightOtTime)
					&& (workHr_min > std_work_hr)) {
				overtime = workHr_min - std_work_hr;

				midnightOT = etime_tot_min - std_midnightOtTime;
				if (Number(midnightOT) >= Number(overtime)) {
					midnightOT = overtime;
				} else {
					midnightOT = etime_tot_min - std_midnightOtTime;
				}
			}
			if (midnightOT > 0) {
				overtime = workHr_min - midnightOT - std_work_hr;
				if (overtime <= 0)
					overtime = "";
			} else if (midnightOT <= 0) {
				if (workHr_min > std_work_hr) {
					overtime = workHr_min - std_work_hr;
					midnightOT = "";
				}

				else if (workHr_min <= std_work_hr) {
					overtime = "";
					midnightOT = "";
				}
			}
		} else {
			overtime = "";
			midnightOT = "";
		}
		if (!isNaN(workHr_min)) {
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
	
	document.getElementById("work_hour").value = result_work_time;
	document.getElementById("overtime").value = result_over_time;
	document.getElementById("midnight_overtime").value = result_midnight_overtime;
	
}