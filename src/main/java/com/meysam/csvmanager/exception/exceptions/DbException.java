package com.meysam.csvmanager.exception.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class DbException extends RuntimeException{

    public DbException(String message){
        super(message);
    }

}
