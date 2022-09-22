package com.justcodeit.moyeo.study.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class JwtProviderTest {

  int countChar(String str, char c) {
    Pattern pattern = Pattern.compile("[" + c + "]");
    Matcher matcher = pattern.matcher(str);
    int count = 0;
    while (matcher.find()) {
      count++;
    }
    return count;
  }

  @Test
  void generate() {

    var jwtProvider = new JwtProvider(10000l, Jwts.builder(), Jwts.parserBuilder().build());
    var claims = new HashMap<String, Object>() {{
      put("username", "user");
      put("role", "ROLE_SUPERVISOR");
    }};
    var tokenString = jwtProvider.generate(claims);

    assertNotNull(tokenString);
    assertFalse(tokenString.isEmpty());
    assertTrue(countChar(tokenString, '.') == 2);

  }

  @Test
  void claims() {
    Key k = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    var builder = Jwts.builder().signWith(k);
    var parser = Jwts.parserBuilder().setSigningKey(k).build();
    var jwtProvider = new JwtProvider(10000l, builder, parser);
    var claimsMap = new HashMap<String, Object>() {{
      put("username", "user");
      put("role", "ROLE_SUPERVISOR");
    }};
    var tokenString = jwtProvider.generate(claimsMap);
    var claims = jwtProvider.claims(tokenString);
    var name = claims.get("username");
    assertNotNull(name);
  }

  @Test
  void validate() {
    Key k = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    var builder = Jwts.builder().signWith(k);
    var parser = Jwts.parserBuilder().setSigningKey(k).build();
    var jwtProvider = new JwtProvider(1000, builder, parser);
    var claims = new HashMap<String, Object>() {{
      put("username", "user");
      put("role", "ROLE_SUPERVISOR");
    }};
    var validToken = jwtProvider.generate(claims);
    assertTrue(jwtProvider.validate(validToken));
    assertFalse(jwtProvider.validate("thisis.not.jwtstring"));

  }
}