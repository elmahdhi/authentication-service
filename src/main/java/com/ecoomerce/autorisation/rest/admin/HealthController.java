package com.ecoomerce.autorisation.rest.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import fr.grdf.softwarefactory.abj.configuration.ApplicationProperties;
import fr.grdf.softwarefactory.abj.dto.HealthPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Page de vie applicative qui renvoie juste du text au lieu de json
 * <p>
 * Classe HealthResource permet de convertir le type de retour de la page de page de vie personnalisé HealthEndpoint/HealthPage
 * Au lieu de renvoyer un message json on envoie plutôt un text/plain
 *
 */
@Tag(name = "Page de vie", description = "Endpoint exposé pour gérer la page de vie")
@RestController
public class HealthController {

    /**
     * Page de vie.
     */
    private final HealthPage endpoint;
    /**
     * Fichier des properties applicatif
     */
    private final ApplicationProperties props;

    /**
     * Mapper.
     */
    private final JavaPropsMapper mapper = new JavaPropsMapper();


    public HealthController(HealthPage endpoint, ApplicationProperties props) {
        this.endpoint = endpoint;
        this.props = props;
    }

    /**
     * Page de vie.
     *
     * @return un Plain Test pour le contrôleur page de vie.
     * @throws JsonProcessingException erreur json
     */
    @GetMapping(path = {"/pagedevie"}, produces = "text/plain")
    public ResponseEntity<String> health() throws JsonProcessingException {
        var health = endpoint.health();
        var response = mapper.writeValueAsString(health);
        return ResponseEntity.ok().cacheControl(CacheControl.noCache()).body(response);
    }

    /**
     * Version de l'application.
     *
     * @return version.
     */
    @GetMapping(path = "/version", produces = "text/plain")
    public ResponseEntity<String> version() {
        return ResponseEntity.status(HttpStatus.OK).body(props.getApp().getVersion());
    }
}
