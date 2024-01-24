package com.ecoomerce.autorisation.rest.error.exception;

import lombok.Getter;

import static fr.grdf.softwarefactory.abj.web.rest.error.ErrorConstants.TITLE_ERR_FONCTIONNEL;
import static org.zalando.problem.Status.NOT_FOUND;


/**
 * Implementation de l'erreur NotFound
 */
@Getter
public class NotFoundException extends AppException {

    private static final long serialVersionUID = 1L;

    /**
     * Consutructeur de l'exception
     * Status HTTP par défaut 404
     * @param code code
     * @param message message
     */
    public NotFoundException(String code, String message) {
        super(code, DEFAULT_TYPE, TITLE_ERR_FONCTIONNEL, NOT_FOUND, message);
    }

}
