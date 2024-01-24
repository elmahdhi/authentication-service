package com.ecoomerce.autorisation.rest.error;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@ToString
public class FieldError implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String objet;

    private final String champ;

    private final String message;

}








