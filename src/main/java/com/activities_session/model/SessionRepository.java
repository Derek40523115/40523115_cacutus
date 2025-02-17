package com.activities_session.model;


import com.activities_promotion.model.PromotionVO;
import com.session_time_period.model.Time_PeriodVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
public interface SessionRepository extends JpaRepository<SessionVO, Integer>{

    @Transactional
    @Modifying
    @Query(value = "from SessionVO where activityDate between :start and :end ")
    List<SessionVO> findByActivityDateBetween(@Param("start") Date start, @Param("end") Date end);

    @Transactional
    @Modifying
    @Query(value = "from SessionVO where ?1 >= enrollStarted and ?2 <= enrollEnd")
    List<SessionVO> findBySignStartedAndEnd(Timestamp enrollStarted, Timestamp enrollEnd);

//    @Transactional
//    @Modifying
//    @Query("SELECT tp.timePeriod FROM SessionVO s JOIN s.time_periodVO tp WHERE s.activityDate = :activityDate")
//    List<Time> findTimePeriodByActivityDate(@Param("activityDate") Date activityDate);

//    @Transactional
//    @Modifying
//    @Query("SELECT tp FROM SessionVO s JOIN s.time_periodVO tp WHERE s.activityDate = :activityDate")
//    List<Time_PeriodVO> findTimePeriodByActivityDate(@Param("activityDate") Date activityDate);

    @Transactional
    @Modifying
    @Query("SELECT tp FROM Time_PeriodVO tp JOIN tp.sessionVO s WHERE s.activityDate = :activityDate")
    List<Time_PeriodVO> findTimePeriodsByActivityDate(@Param("activityDate") Date activityDate);

    @Transactional
    @Modifying
    @Query("SELECT DISTINCT s.activityDate FROM SessionVO s WHERE s.activitySessionState = 0")
    List<Date> findDistinctActivityDates();

    @Transactional
    @Modifying
    List<SessionVO> findByItemVO_ActivityId(Integer activityId);

    @Transactional
    @Modifying
    List<SessionVO> findAllByActivitySessionId(Integer activitySessionId);



    @Query(value = "SELECT * FROM activity_session WHERE activity_id = ?1 AND activity_session_state = 0", nativeQuery = true)
    List<SessionVO> findSessionVOs(Integer activityId);

}
