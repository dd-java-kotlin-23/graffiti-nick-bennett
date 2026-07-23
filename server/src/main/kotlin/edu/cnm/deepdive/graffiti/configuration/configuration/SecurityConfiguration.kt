package edu.cnm.deepdive.graffiti.configuration.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.jwt.JwtClaimValidator
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtDecoders
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@Profile("service")
class SecurityConfiguration @Autowired constructor(
    private val converter: Converter<Jwt, out AbstractAuthenticationToken>,
    @param:Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private val issuerUri: String,
    @param:Value("\${spring.security.oauth2.resourceserver.jwt.client-id}")
    private val clientId: String,
) {

    @Bean
    fun provideSecurityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .oauth2ResourceServer { customizer ->
                customizer.jwt { it.jwtAuthenticationConverter(converter) }
            }
            .build();
    }

    @Bean
    fun provideDecoder(): JwtDecoder {
        val audienceValidator =
            JwtClaimValidator<List<String>>(JwtClaimNames.AUD) { it.contains(clientId) }
        val issuerValidator = JwtValidators.createDefaultWithIssuer(issuerUri)
        val combinedValidator = DelegatingOAuth2TokenValidator(audienceValidator, issuerValidator)

        return JwtDecoders.fromIssuerLocation<NimbusJwtDecoder>(issuerUri).apply {
            setJwtValidator(combinedValidator)
        }
    }

}