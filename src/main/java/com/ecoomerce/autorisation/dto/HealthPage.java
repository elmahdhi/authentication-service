package com.ecoomerce.autorisation.dto;

import com.ecoomerce.autorisation.config.ApplicationProperties;
import com.ecoomerce.autorisation.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

/**
 * Page de vie de l'application.
 */
@Slf4j
@Component
public class HealthPage implements HealthIndicator {

    /**
     * variable du prologue contenant les infos du hostsname.
     */
    private static final String HOST_NAME = "HOST_NAME";
    private static final String VERSION = "version";
    private static final String DB_STATUS = "database.status";

    /**
     * Service User.
     */
    private final UserRepository userRepository;

    private final ApplicationProperties props;

    /**
     * Constructeur.
     *
     * @param userRepository UserRepository
     * @param props          ApplicationProperties
     */
    public HealthPage(UserRepository userRepository, ApplicationProperties props) {
        this.userRepository = userRepository;
        this.props = props;
    }

    /**
     * Page de vie.
     *
     * @return page de vie en format Json
     */
    @Override
    public Health health() {
        try {
            Health.Builder health = Health.up()
                .withDetail(VERSION, props.getApp().getVersion())
                .withDetail(HOST_NAME, this.getStringEnvt(HOST_NAME));

            // Etat de la base de données
            boolean dbUp = dbUp();
            health = health.withDetail(DB_STATUS, dbUp ? "UP" : "DOWN");
            health = dbUp ? health.up() : health.down();

            return health.build();
        } catch (Exception e) {
            log.error("Erreur inconnue lors de la génération de la page de vie", e);
            return Health
                .down()
                .withDetail("Exception non prévue", e.toString())
                .build();
        }
    }

    /**
     * Permet de récuperer la valeur d'une variable d'environnement.
     *
     * @return info sur l'envt.
     */
    private String getStringEnvt(String sysvar) {
        String envt = System.getenv(sysvar);
        if (envt == null) {
            envt = "variable " + sysvar + " non trouvé, verifier le bashrc.sh ou le prologue.sh !";
        }
        return envt;
    }

    /**
     * Indique si la base de données est up.
     *
     * @return true si up
     */
    private boolean dbUp() {
        try {
            return userRepository.count() > 0;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Retourne le status de l'application Spring.
     *
     * @return Status spring.
     */
    public Status getApplicationStatus() {
        return this.health().getStatus();
    }
}
