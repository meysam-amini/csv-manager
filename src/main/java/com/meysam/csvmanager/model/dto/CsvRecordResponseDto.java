package com.meysam.csvmanager.model.dto;

import com.meysam.csvmanager.model.entity.CsvRecord;

public record CsvRecordResponseDto(Long id,
                                   String source,
                                   String codeListCode,
                                   String code,
                                   String displayValue,
                                   String longDescription,
                                   String fromDate,
                                   String toDate,
                                   String sortingPriority) {


    public static CsvRecordResponseDto mapFromCsvRecord(CsvRecord csvRecord){
        return new CsvRecordResponseDto(csvRecord.getId(),
                csvRecord.getSource(),
                csvRecord.getCodeListCode(),
                csvRecord.getCode(),
                csvRecord.getDisplayValue(),
                csvRecord.getLongDescription(),
                csvRecord.getFromDate(),
                csvRecord.getToDate(),
                csvRecord.getSortingPriority()
                );
    }
}
