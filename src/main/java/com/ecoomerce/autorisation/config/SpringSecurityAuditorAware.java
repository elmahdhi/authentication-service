package com.ecoomerce.autorisation.config;


import com.ecoomerce.autorisation.config.auth.SecurityUtils;
import com.ecoomerce.autorisation.utils.Constantes;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation de {@link AuditorAware} basé sur Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().orElse(Constantes.SYSTEM));
    }
}
