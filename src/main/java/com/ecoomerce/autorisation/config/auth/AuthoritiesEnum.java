package com.ecoomerce.autorisation.config.auth;

/**
 * Constantes Spring Security authorities.
 */

public enum AuthoritiesEnum {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    ANONYMOUS("ROLE_ANONYMOUS");

    private final String libelle;

    AuthoritiesEnum(final String libelle) {

        this.libelle = libelle;
    }


    /**
     * Trouve l'état de l'entité associé à un libellé
     *
     * @param libelle libellé de l'état
     * @return état entité correspondant à un libelle; null sinon.
     */
    public static AuthoritiesEnum fromStringValue(String libelle) {
        for (AuthoritiesEnum auth : AuthoritiesEnum.values()) {
            if (auth.libelle.equalsIgnoreCase(libelle)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
