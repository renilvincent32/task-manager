package dev.rvincent.taskmanager.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AtlassianOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String accessToken = userRequest.getAccessToken().getTokenValue();
        JsonNode userInfo = getAtlassianUserInfo(accessToken);
        Map<String, Object> attributes = new HashMap<>();
        assert userInfo != null;
        attributes.put("id", userInfo.get("account_id").asText());
        attributes.put("email", userInfo.get("email").asText());
        attributes.put("name", userInfo.get("name").asText());
        attributes.put("scopes", resolveScopes(userRequest));
        return new AtlassianOAuth2User(attributes);
    }

    private JsonNode getAtlassianUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("https://api.atlassian.com/me", HttpMethod.GET, entity, String.class);
        try {
            return new ObjectMapper().readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Atlassian user info response", e);
        }
    }

    private Collection<? extends GrantedAuthority> resolveScopes(OAuth2UserRequest userRequest) {
        Set<String> scopes = userRequest.getAccessToken().getScopes();
        return scopes.stream()
                .map(scope -> scope.replaceFirst("SCOPE_", ""))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
