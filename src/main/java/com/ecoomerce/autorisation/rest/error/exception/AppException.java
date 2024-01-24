package com.ecoomerce.autorisation.rest.error.exception;

import lombok.Getter;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import javax.annotation.Nullable;
import java.net.URI;
@Getter
public abstract class AppException extends AbstractThrowableProblem {

    // code permettant à identifier l'exception
    private final String code;
    private final URI type;
    private final String title;
    private final String detail;
    private final Status status;

    protected AppException(final String code, @Nullable final URI type,
                                       @Nullable final String title,
                                       @Nullable final Status status,
                                       @Nullable final String detail) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.code = code;
    }

}
