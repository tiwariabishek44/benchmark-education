package com.benchmark.education.dto.Reponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto {

    private boolean success;
    private Object data;
    private String message;

    public static ResponseDto Success(Object data, String message){
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

    public static ResponseDto Failure(Object data, String message){
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
