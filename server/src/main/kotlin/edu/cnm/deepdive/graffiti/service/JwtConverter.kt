package edu.cnm.deepdive.graffiti.service

import edu.cnm.deepdive.graffiti.model.entity.User
import java.util.Collections
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service

@Service
@Profile("service")
class JwtConverter @Autowired constructor(
    private val service: UserService
) : Converter<Jwt, UsernamePasswordAuthenticationToken> {

    override fun convert(source: Jwt): UsernamePasswordAuthenticationToken {
        val grants = Collections.singleton(SimpleGrantedAuthority("ROLE_USER"))
        val user = User(
            displayName = source.getClaimAsString("name") as String,
            oauthKey = source.subject as String,
        ).let { service.getOrAddUser(it) }
        return UsernamePasswordAuthenticationToken(user, source.tokenValue, grants)
    }

}