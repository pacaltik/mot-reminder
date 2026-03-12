package com.motchecker.mot_reminder.repository;

import com.motchecker.mot_reminder.model.MotRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MotRecordRepository extends JpaRepository<MotRecord, Long> {

    // sort in order
    List<MotRecord> findByCarIdOrderByInspectionDateDesc(Long carId);
}