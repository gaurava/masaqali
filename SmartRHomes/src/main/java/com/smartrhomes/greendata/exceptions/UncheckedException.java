package com.smartrhomes.greendata.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Gaurav
 * Date: 17/3/13
 * Time: 8:08 PM
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UncheckedException extends RuntimeException {

    private final String code;
    private String message;

    private final Object[] messageParameters;
    private List<ErrorContext> errorContexts = Collections.emptyList();

    public UncheckedException(String code, Object... messageParameters) {
        this(code, messageParameters, null, (ErrorContext[])null);
    }

    public UncheckedException(String code,
                              Object[] messageParameters, ErrorContext[] errorContexts) {
        this(code, messageParameters, null, errorContexts);
    }

    public UncheckedException(String code, Throwable cause) {
        this(code, null, cause, (ErrorContext[])null);
    }

    public UncheckedException(String code,
                              Object[] messageParameters, Throwable cause) {
        this(code, messageParameters, cause, (ErrorContext[])null);
    }

    public UncheckedException(String code, Object[] messageParameters,
                              Throwable cause, ErrorContext... errorContexts) {
        if(cause!=null)
            initCause(cause);
        this.code = code;
        this.message = "ErrorCode:"+code+", ErrorMessage: "+messageParameters.toString();
        this.messageParameters = messageParameters;
        if (errorContexts != null) {
            for (ErrorContext errorContext : errorContexts) {
                addErrorContext(errorContext);
            }
        }
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object[] getMessageParameters() {
        return messageParameters;
    }

    public Collection<ErrorContext> getErrorContexts() {
        return errorContexts;
    }

    public String getContextInfo() {
        StringBuilder contextInfo = new StringBuilder("Context Info : \n");
        for (ErrorContext context : errorContexts) {
            if (context == null) continue;
            contextInfo.append(context.toString()).append("\n");
        }
        return contextInfo.toString();
    }

    public void addErrorContext(ErrorContext context) {
        if (errorContexts == Collections.EMPTY_LIST) { // ref comparison is intentional
            errorContexts = new ArrayList<ErrorContext>();
        }
        errorContexts.add(context);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getName() + "{")
                .append(" code = ").append(code)
                .append(", message = ").append(message)
                .append(", associated contexts = ").append(errorContexts)
                .append("}");
        return builder.toString();
    }
}
