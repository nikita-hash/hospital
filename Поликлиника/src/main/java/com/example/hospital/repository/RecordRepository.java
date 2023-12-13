package com.example.hospital.repository;

import com.example.hospital.model.Doctor;
import com.example.hospital.model.Record;
import com.example.hospital.model.RecordStatus;
import com.example.hospital.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface RecordRepository extends JpaRepository<Record,Long> {

    @Query("SELECT r FROM Record r WHERE r.date = ?1 AND r.doctor = ?2 and r.recordStatus = ?3")
    List<Record> findRecordsByDateAndDoctorAndRecordStatus(LocalDate date, Doctor doctor,RecordStatus recordStatus);

    @Query("SELECT r FROM Record r WHERE r.date = ?1 AND r.user = ?2 and r.recordStatus = ?3")
    List<Record> findRecordsByDateAndUserAndRecordStatus(LocalDate date, User user,RecordStatus recordStatus);

    @Query("SELECT r FROM Record r WHERE r.date = ?1 AND r.doctor = ?2")
    List<Record> findRecordsByDateAndDoctor(LocalDate date, Doctor doctor);

    @Query("select r from Record r where r.doctor =?1 and r.recordStatus = ?2"
            +" order by  r.date desc ")
    List<Record> findAllByDoctorAndRecordStatusOrderByDateDesc(Doctor doctor, RecordStatus recordStatus);

    @Query("select r from Record r where r.user =?1 and r.recordStatus = ?2"
            +" order by  r.date desc ")
    List<Record> findAllByUserAndRecordStatusOrderByDateDesc(User user, RecordStatus recordStatus);

    List<Record>findAllByRecordStatus(RecordStatus recordStatus);

    List<Record>findAllByRecordStatusAndUserId(RecordStatus recordStatus,Long id);

    @Modifying
    @Query("UPDATE Record r SET r.recordStatus = :status WHERE r.id = :id")
    void changeStatus(@Param("status") RecordStatus status, @Param("id") Long id);

    @Modifying
    @Query("UPDATE Record r SET r.recordStatus = :status WHERE r.id = :id")
    void updateRecordStatusById(@Param("status") RecordStatus status, @Param("id") Long id);

    @Query(value = "SELECT week_day.day_of_week, COUNT(record.date) AS count FROM ( SELECT 'Monday' AS day_of_week" +
            " UNION SELECT 'Tuesday'" +
            " UNION SELECT 'Wednesday'" +
            " UNION SELECT 'Thursday'" +
            " UNION SELECT 'Friday'" +
            " UNION SELECT 'Saturday'" +
            " UNION SELECT 'Sunday' ) AS week_day" +
            " LEFT JOIN record ON DATE_FORMAT(record.date, '%W') = week_day.day_of_week" +
            " AND DATE(record.date) >= CURDATE() - INTERVAL WEEKDAY(CURDATE()) DAY" +
            " AND DATE(record.date) < CURDATE() + INTERVAL (6 - WEEKDAY(CURDATE())) DAY + INTERVAL 1 DAY" +
            " GROUP BY week_day.day_of_week", nativeQuery = true)
    List<Map<String, Long>> getCountByDayOfWeek();

    List<Record>findAllByDateAndRecordStatus(LocalDate dateNow,RecordStatus recordStatus);

    List<Record>findAllByDateAndUser(LocalDate date, User user);

}
