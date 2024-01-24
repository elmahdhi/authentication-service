package com.ecoomerce.autorisation.rest.error;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.URI;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorConstants {


    //DE APP_0000 -> APP_0999 => erreurs techniques
    public static final String ERR_T_DEFAULT = "APP_0000";
    public static final String ERR_T_DATA_ACCESS = "APP_0001";
    public static final String ERR_T_HTTP_CONVERSION = "APP_0002";
    public static final String ERR_T_ACCESS_UNAUTHORIZED = "APP_0401";
    public static final String ERR_T_FULL_ACCESS_UNAUTHORIZED = "APP_0401_1";
    public static final String ERR_T_ACCESS_DENIED = "APP_0403";
    public static final String ERR_T_VALIDATION = "APP_0400";
    public static final String ERR_T_RUNTIME = "APP_0999";

    //DE APP_1000 -> APP_1999 => erreurs fonctionnelles
    public static final String ERR_F_USER_NOT_EXIST = "APP_1000";
    public static final String ERR_F_USER_DELETE = "APP_1001";
    public static final String ERR_F_USER_CREATE = "APP_1002";
    public static final String ERR_F_USER_UPDATE = "APP_1003";

    public static final String TITLE_ERR_FONCTIONNEL = "Erreur fonctionnelle";
    public static final String TITLE_ERR_TECHNIQUE = "Erreur technique";

    // la base url du wiki qui contiendra les détails des erreurs
    public static final String PROBLEM_BASE_URL = "";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");

    public static final String CODE_KEY = "code";
    public static final String PATH_KEY = "path";
    public static final String VIOLATIONS_KEY = "violations";

}
