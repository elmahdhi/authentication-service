package com.ecoomerce.autorisation.config.auth.mock;

import com.ecoomerce.autorisation.config.ApplicationProperties;
import com.ecoomerce.autorisation.services.AccountService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * Filtre bouchon pour simuler une authentification OKTA
 */
@Slf4j
public class MockWebSSOFilter extends GenericFilterBean {

    private final ApplicationProperties props;
    private final AccountService accountService;

    public MockWebSSOFilter(ApplicationProperties props,
                            AccountService accountService) {
        this.props = props;
        this.accountService = accountService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        if (props.getWebSSO().isMock()) {
            var user = accountService.findByGidAndActiveTrue(props.getWebSSO().getMockedUser());
            if (user.isPresent()) {
                var auth = new AppAuthentication(user.get());
                auth.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                log.error("L'utilisateur {} n'existe pas dans la base de données", props.getWebSSO().getMockedUser());
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
