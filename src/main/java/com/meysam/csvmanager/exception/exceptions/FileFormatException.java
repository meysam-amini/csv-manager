package com.meysam.csvmanager.exception.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileFormatException extends RuntimeException{

    public FileFormatException(String message){
        super(message);
    }

}
