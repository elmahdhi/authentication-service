package com.ecoomerce.autorisation.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Classe utilitaire contenant les constantes utilisées
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constantes {

    public static final String PREFIX_ROLE = "ROLE_";
    public static final String GROUPS_DELIMITER = ",";
    public static final String ORIGINS_DELIMITER = ",";
    public static final String SYSTEM = "system";
    public static final String LIST_DELIMITER = ", ";
    public static final String EMPTY_STRING = "";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class TypeMessage {
        public static final String INFO = "Info";
        public static final String ERROR = "Erreur";
    }

}
