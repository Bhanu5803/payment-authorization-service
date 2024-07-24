//package com.bridgeline.authorize.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.oidc.OidcScopes;
//
//@Configuration
//public class OAuth2ClientConfig {
//    @Bean
//    public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext) {
//        return new OAuth2RestTemplate(clientRegistration(), oauth2ClientContext);
//    }
//
//    @Bean
//    public ClientRegistration clientRegistration() {
//        return ClientRegistration.withClientRegistrationId("my-client-id")
//                .clientId("your-client-id")
//                .clientSecret("your-client-secret")
//                .authorizationUri("http://localhost:8080/oauth/authorize")
//                .tokenUri("http://localhost:8080/oauth/token")
//                .userInfoUri("http://localhost:8080/user")
//                .scope(OidcScopes.OPENID)
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .build();
//    }
//}
