package services.access_controll;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zeroc.Ice.Current;

import java.util.Date;
import java.util.Map;

public class TokenManager {

    // BEST PRODUCTION-LEVEL PRACTISE
    private static String SECTET = "krowa";
    private static Algorithm ALGORITHM = Algorithm.HMAC256(TokenManager.SECTET);
    private static int EXPIRATION_TIME = 20 * 60;
    private static String PESEL_FIELD_NAME = "PESEL";
    private static String PREMIUM_ACCOUNT_FLAG_NAME = "premiumAccount";
    private static String TOKEN_FIELD_NAME = "token";

    public String generateToken(String PESEL, boolean premiumAccount){
        return JWT.create()
                            .withExpiresAt(new Date(System.currentTimeMillis() + (TokenManager.EXPIRATION_TIME * 1000)))
                            .withClaim(TokenManager.PESEL_FIELD_NAME, PESEL)
                            .withClaim(TokenManager.PREMIUM_ACCOUNT_FLAG_NAME, premiumAccount)
                            .withIssuer("auth0")
                            .sign(TokenManager.ALGORITHM);
    }

    public boolean isTokenValid(String token){
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ignored){
            return false;
        }
    }

    public boolean isPremiumTokenValid(String token){
        if (this.isTokenValid(token)){
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .build();
            DecodedJWT decoded = verifier.verify(token);
            Map<String, Claim> claims = decoded.getClaims();
            if(claims.containsKey(TokenManager.PREMIUM_ACCOUNT_FLAG_NAME)){
                return claims.get(TokenManager.PREMIUM_ACCOUNT_FLAG_NAME).asBoolean();
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    public String getPESELFromToken(String token){
        JWTVerifier verifier = JWT.require(ALGORITHM)
                .build();
        DecodedJWT decoded = verifier.verify(token);
        Map<String, Claim> claims = decoded.getClaims();
        return claims.get(TokenManager.PESEL_FIELD_NAME).asString();
    }

    public String getTokenFromContext(Current current) throws TokenNotPresentInContextException {
        Map<String, String> context = current.ctx;
        System.out.println(context);
        if(!context.containsKey(TokenManager.TOKEN_FIELD_NAME)){
            throw new TokenNotPresentInContextException();
        }
        return context.get(TokenManager.TOKEN_FIELD_NAME);
    }


}
