package com.meysam.csvmanager.service;

import com.meysam.csvmanager.config.messages.LocaleMessageSourceService;
import com.meysam.csvmanager.exception.exceptions.BusinessException;
import com.meysam.csvmanager.exception.exceptions.DbException;
import com.meysam.csvmanager.model.dto.CsvRecordResponseDto;
import com.meysam.csvmanager.repository.CsvRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public non-sealed class CsvFIleServiceImpl implements CsvFileService {

    private final CsvRecordRepository csvRecordRepository;


    @Override
    public String upload() {
        return null;
    }

    @Override
    public Page<CsvRecordResponseDto> getAllRecords(Integer pageNumber, Integer pageSize) {
        return csvRecordRepository.findAll(getPageapble(pageNumber,pageSize))
                .map(CsvRecordResponseDto::mapFromCsvRecord);
    }

    @Override
    public CsvRecordResponseDto getRecordByCode(String code) {
        return csvRecordRepository.findByCode(code)
                .map(CsvRecordResponseDto::mapFromCsvRecord)
                .orElseThrow(()->new BusinessException("RECORD_WITH_THIS_CODE_DOES_NOT_EXIST"));
    }

    @Transactional
    @Override
    public void deleteAllRecords() {

        try {
            csvRecordRepository.deleteAll();
        }catch (Exception e){
            log.error("Exception on deleting all data from db at time:{}, exception is{}",System.currentTimeMillis(),e);
            throw new DbException("PROBLEM_ON_DELETING_DATA");
        }

    }





    private Pageable getPageapble(Integer pageNumber, Integer pageSize){

        if(Objects.isNull(pageNumber))
            pageNumber = 0;
        if(Objects.isNull(pageSize))
            pageSize = 20;

        return PageRequest.of(pageNumber,pageSize);
    }
}
