package com.cisco.jwt_spring_boot.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.MacProvider;

import javax.crypto.SecretKey;

public class SecurityConstants {
    static SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS256);
    public static  final String SECRET = TextCodec.BASE64.encode(key.getEncoded());
    public static final long EXPIRATION_TIME = 864_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
