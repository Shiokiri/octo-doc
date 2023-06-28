package com.werun.onlinenote_backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;

public class JwtUtils {

    /**
     * 签发对象：这个用户的id
     * 签发时间：现在
     * 有效时间：30分钟
     * 载荷内容：暂时设计为：这个人的名字，这个人的昵称
     * 加密密钥：这个人的id加上一串字符串
     */
    public static String createToken(String userId, String realName, String userName) {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE,30);
        Date expiresDate = nowTime.getTime();

        return JWT.create().withAudience(userId)
                .withIssuedAt(new Date())
                .withExpiresAt(expiresDate)
                .withClaim("userName", userName)
                .withClaim("realName", realName)
                .sign(Algorithm.HMAC256(userId+"luoshuitianyi"));
    }

    /**
     * 检验合法性，其中secret参数就应该传入的是用户的id
     */
    public static void verifyToken(String token, String secret)  {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret+"luoshuitianyi")).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            System.out.println("failed");
        }
    }

    /**
     * 获取签发对象
     */
    public static String getAudience(String token) {
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            return "failed";
        }
        return audience;
    }


    /**
     * 通过载荷名字获取载荷的值
     */
    public static Claim getClaimByName(String token, String name){
        return JWT.decode(token).getClaim(name);
    }
}

