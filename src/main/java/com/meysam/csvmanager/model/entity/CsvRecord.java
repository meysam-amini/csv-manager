package com.meysam.csvmanager.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "csv_record", uniqueConstraints = @UniqueConstraint(name = "UNIQUE_CODE", columnNames = {"code"}))
public class CsvRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String source;
    private String codeListCode;
    private String code;
    private String displayValue;
    private String longDescription;
    private String fromDate;
    private String toDate;
    private String sortingPriority;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CsvRecord)) return false;
        CsvRecord csvRecord = (CsvRecord) o;
        return getCode().equals(csvRecord.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
