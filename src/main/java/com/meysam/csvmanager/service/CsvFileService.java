package com.meysam.csvmanager.service;


import com.meysam.csvmanager.model.dto.CsvRecordResponseDto;
import org.springframework.data.domain.Page;

public sealed interface CsvFileService permits CsvFIleServiceImpl {

    String upload();

    Page<CsvRecordResponseDto> getAllRecords(int pageNumber, int pageSize);

    CsvRecordResponseDto getRecordByCode(String code);

    void deleteAllRecords();


}
