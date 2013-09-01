package com.smartrhomes.greendata.exceptions;

import com.smartrhomes.greendata.exceptions.ErrorContext;
import com.smartrhomes.greendata.exceptions.UncheckedException;

/**
 * User: Gaurav
 * Date: 2/3/13
 * Time: 7:12 PM
 */
public class DAOExceptions extends UncheckedException {

    public DAOExceptions(String code, Object... messageParameters) {
        super(code, messageParameters);
    }

    public DAOExceptions(String code, Throwable cause) {
        super(code, null, cause, (ErrorContext[]) null);
    }

    public DAOExceptions(String code,
                          Object[] messageParameters, Throwable cause) {
        super(code, messageParameters, cause);
    }
}

