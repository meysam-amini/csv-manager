package com.meysam.csvmanager.repository;

import com.meysam.csvmanager.model.entity.CsvRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CsvRecordRepository extends JpaRepository<CsvRecord,Long> {

    Page<CsvRecord> findAll(Pageable pageable);
    Optional<CsvRecord> findByCode(String code);
    void deleteAll();

}
