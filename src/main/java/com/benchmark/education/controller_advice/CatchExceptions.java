package com.benchmark.education.controller_advice;

import com.benchmark.education.dto.Reponse.AccessTokenExceptionDto;
import com.benchmark.education.dto.Reponse.ResponseDto;
import com.benchmark.education.dto.Reponse.WrongRefreshTOkenResponse;
import com.benchmark.education.exception.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CatchExceptions {

    @ExceptionHandler(AccountCreationException.class)
    public ResponseDto<String> handleCustomException(AccountCreationException e, HttpServletResponse response)
    {
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    return ResponseDto.Failure("", e.getMessage());
    }

    @ExceptionHandler(GenericWrongRequestException.class)
    public ResponseDto<String> handleCustomException1(GenericWrongRequestException e, HttpServletResponse response)
    {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseDto.Failure("", e.getMessage());
    }

    @ExceptionHandler(LoginException.class)
    public ResponseDto<String> handleCustomException(LoginException e, HttpServletResponse response)
    {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseDto.Failure("", e.getMessage());
    }

    @ExceptionHandler(NoSuchAccountException.class)
    public ResponseDto<String> handleCustomException(NoSuchAccountException e, HttpServletResponse response)
    {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseDto.Failure("", e.getMessage());
    }

    @ExceptionHandler(RestrictedException.class)
    public ResponseDto<String> handleCustomException(RestrictedException e, HttpServletResponse response)
    {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return ResponseDto.Failure("", e.getMessage());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseDto<Object> handleCustomException(TokenExpiredException e, HttpServletResponse response)
    {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return ResponseDto.Failure(e.getData(), e.getMessage());
    }

    @ExceptionHandler(WrongOtpException.class)
    public ResponseDto<Object> handleCustomException(WrongOtpException e, HttpServletResponse response)
    {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResponseDto.Failure(e.getData(), e.getMessage());
    }

    @ExceptionHandler(WrongRefreshToken.class)
    public ResponseDto<WrongRefreshTOkenResponse> handleCustomException(WrongRefreshToken e, HttpServletResponse response)
    {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        WrongRefreshTOkenResponse wrongRefreshTOkenResponse = new WrongRefreshTOkenResponse(true);
        return ResponseDto.Failure(wrongRefreshTOkenResponse, e.getMessage());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseDto<AccessTokenExceptionDto> handleCustomException(UnAuthorizedException e, HttpServletResponse response)
    {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        AccessTokenExceptionDto exceptionDto = new AccessTokenExceptionDto(true);
        return ResponseDto.Failure(exceptionDto, e.getMessage());
    }

}
