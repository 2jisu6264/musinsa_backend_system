package com.musinsa.sys.common.handler;

import com.musinsa.sys.common.dto.ProcessResult;
import com.musinsa.sys.common.enums.ProcessCode;
import com.musinsa.sys.common.exception.ServiceException;
import com.musinsa.sys.common.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    //@Valid를 통해 발견된 MethodArgumentNotValidException 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProcessResult<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return new ProcessResult<>(null, ProcessCode.HCO998.getProcCd());
    }

    // 404 Not Found 처리
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(NoHandlerFoundException ex) {
        return new ResponseEntity<>("리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleException(ServiceException ex) {
        log.info(StringUtil.getStackTraceToString(ex));
        return new ResponseEntity<>(new ProcessResult<>(null, ex.getProcCd()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 그 외의 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        log.info(StringUtil.getStackTraceToString(ex));
        return new ResponseEntity<>(new ProcessResult<>(null, ProcessCode.HCO999.getProcCd()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
