package com.session_time_period.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;



import com.activities_session.model.SessionVO;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "session_time_period")
public class Time_PeriodVO implements Serializable{


	private static final long serialVersionUID = 3110286673838412074L;

	@Id
	@Column(name = "session_time_period_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sessionTimePeriodId;

	@Column(name = "time_period")
//	@DateTimeFormat(pattern = "HH:mm")
	private Time timePeriod;


	@ManyToOne
	@JoinColumn(name = "activity_session_id" , referencedColumnName = "activity_session_id")
	private SessionVO sessionVO;

	
	public Time_PeriodVO() {
		
	}

	public Integer getSessionTimePeriodId() {
		return sessionTimePeriodId;
	}

	public void setSessionTimePeriodId(Integer sessionTimePeriodId) {
		this.sessionTimePeriodId = sessionTimePeriodId;
	}

	public void setTimePeriod(Time timePeriod) {
		this.timePeriod = timePeriod;
	}

	public Time getTimePeriod() {
		return timePeriod;
	}

	public SessionVO getSessionVO() {
		return sessionVO;
	}

	public void setSessionVO(SessionVO sessionVO) {
		this.sessionVO = sessionVO;
	}

	@Override
	public String toString() {
		return "Time_PeriodVO{" +
				"sessionTimePeriodId=" + sessionTimePeriodId +
				", timePeriod=" + timePeriod +
				", sessionVO=" + sessionVO +
				'}';
	}
}
