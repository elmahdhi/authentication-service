package com.ecoomerce.autorisation.rest.error.exception;

import lombok.Getter;

import static fr.grdf.softwarefactory.abj.web.rest.error.ErrorConstants.*;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

/**
 * Implementation de l'erreur InternalError
 *
 */
@Getter
public class InternalErrorException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Consutructeur de l'exception
     * Status HTTP par défaut 500
     * @param code code
     * @param message message
     */
    public InternalErrorException(String code, String message) {
        super(code, DEFAULT_TYPE, TITLE_ERR_TECHNIQUE, INTERNAL_SERVER_ERROR, message);
    }


}
