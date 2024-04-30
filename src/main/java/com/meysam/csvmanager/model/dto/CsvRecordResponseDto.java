package com.meysam.csvmanager.model.dto;

public record CsvRecordResponseDto(Long id,
                                   String source,
                                   String codeListCode,
                                   String code,
                                   String displayValue,
                                   String longDescription,
                                   String fromDate,
                                   String toDate,
                                   String sortingPriority) {
}
