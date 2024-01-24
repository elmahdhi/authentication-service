package com.ecoomerce.autorisation.config.auth.mock;



import com.ecoomerce.autorisation.config.ApplicationProperties;
import com.ecoomerce.autorisation.services.AccountService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration du filtre mock
 */
public class MockWebSSOConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final ApplicationProperties props;
    private final AccountService accountService;

    public MockWebSSOConfigurer(ApplicationProperties props,
                                AccountService accountService) {
        this.props = props;
        this.accountService = accountService;
    }

    @Override
    public void configure(HttpSecurity http) {
        var customFilter = new MockWebSSOFilter(props, accountService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
