package com.ecoomerce.autorisation.config.auth;

import fr.grdf.softwarefactory.abj.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class AppAuthentication implements Authentication {

    private static final long serialVersionUID = -3682205314153637413L;

    private final User user;

    private String password;

    private Collection<? extends GrantedAuthority> grants;

    private boolean authenticated = false;

    public AppAuthentication(final User user) {
        this.user = user;
    }
    public AppAuthentication(final String gid, final String password) {
        this.user = User.builder().gid(gid).password(password).build();
    }

    public AppAuthentication(final String gid) {
        this.user = User.builder().gid(gid).build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.user.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return user.getGid();
    }

    @Override
    public Object getDetails() {
        return user.getFirstName();
    }

    @Override
    public Object getPrincipal() {
        return user;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return user.getLastName();
    }

    public void setAuthorities(final Collection<? extends GrantedAuthority> grants) {
        this.grants = grants;
    }

    public String getPassword() {
        return user.getPassword();
    }
}
