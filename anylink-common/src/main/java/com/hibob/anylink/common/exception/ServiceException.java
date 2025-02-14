package com.hibob.anylink.common.exception;

import com.hibob.anylink.common.enums.ServiceErrorCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class ServiceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    private ServiceErrorCode serviceErrorCode;

    public ServiceException(ServiceErrorCode serviceErrorCode) {
        super(serviceErrorCode.desc());
    }
}
