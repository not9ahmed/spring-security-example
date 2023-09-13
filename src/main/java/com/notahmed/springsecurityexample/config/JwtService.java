package com.notahmed.springsecurityexample.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// annotation to make it as managebean

@Service
public class JwtService {


    // TODO move to config file
    private static final String SECRET_KEY = "D65CCBEBD47F1F725D1EA2A79EF901911A9B40ED3B08AFF024FC4704ECFC18C1";

    /**
     * Method to extract username from a token
     * @param token
     * @return username
     */
    public String extractUsername(String token) {

//        StringBuilder username = new StringBuilder();


        // getSubject of the token
        // subject will be username or email of user

        return extractClaim(token, Claims::getSubject);
    }


    // passing function as parameter aka callback -> function programming
    // function to extract claims from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        // list of all the claims we have
        final Claims claims = extractAllClaims(token);


        return claimsResolver.apply(claims);

    }


    // polymorphism
    // this method will only take userDetails as parameter
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);

    }



    // method to pass extra authorities
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {

        // subject will be username or email
        // for the project will be email

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 ))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

    }


    // method to verify if the token is valid or not
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        boolean isValid = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

        return isValid;
    }


    // check if the token is expired
    public boolean isTokenExpired(String token) {

        boolean isExpired = extractExpiration(token).before(new Date());

        return isExpired;
    }



    public Date extractExpiration(String token) {

        // extracting the expiration date from token
        Date tokenExpirationDate = extractClaim(token, Claims::getExpiration);

        return tokenExpirationDate;
    }



    /**
     * Method to extract all the claims from the token
     * @param token
     * @return Claims objects which contains all the details such as username, subject, expiry date etc
     */
    private Claims extractAllClaims(String token) {

        // setSigningKey() which requires some key
        // create the sign part of jwt
        // ensure message is not changed
        // 256 for the key to create

        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    /**
     * Method to decode the key
     * @return
     */
    private Key getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(keyBytes);
    }


}
