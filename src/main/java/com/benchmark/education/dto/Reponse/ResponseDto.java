package com.benchmark.education.dto.Reponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto<K> {

    private boolean success;
    private K data;
    private String message;

    public static <T> ResponseDto<T> Success(T data, String message){
        ResponseDto success = new ResponseDto();
        success.setSuccess(true);
        success.setData(data);
        if(message==null){
            success.setMessage("Successfully Completed");
        }else{
            success.setMessage(message);
        }
        return success;
    }

    public static <T> ResponseDto<T> Failure(T data, String message){
        ResponseDto failure = new ResponseDto();
        failure.setSuccess(false);
        failure.setData(data);
        if(message==null){
            failure.setMessage("Operation Failed");
        }else{
            failure.setMessage(message);
        }
        return failure;
    }
}
