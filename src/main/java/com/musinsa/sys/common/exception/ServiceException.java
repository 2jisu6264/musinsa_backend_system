package com.musinsa.sys.common.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {

    private final String procCd;

    public ServiceException(String inProcCd) {
        this.procCd = inProcCd;
    }
}
