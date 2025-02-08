package dev.rvincent.taskmanager.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public record AtlassianOAuth2User(Map<String, Object> attributes) implements OAuth2User {

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (Collection<? extends GrantedAuthority> ) attributes.get("scopes");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
