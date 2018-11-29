package com.um.util;


import com.um.common.enums.ErrorCodeEnum;
import com.um.domain.common.Jwt;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


/**
 * @description JWT工具类，使用开源jjwt
 * @author ws
 */
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);


    //服务端私钥
    private static final String JWT_SECRET = "ZFN#k%Mx16jC5x#f";

    //jwt过期时间
    private static final int JWT_TTL = 8*60*60*1000;  //millisecond//8h




    //加密算法
    private static SignatureAlgorithm signatureAlgorithm;

    //生成签名的时候使用的秘钥secret,这个方法本地封装了的，一般可以从本地配置文件中读取，切记这个秘钥不能外露哦。它就是你服务端的私钥，在任何场景都不应该流露出去。一旦客户端得知这个secret, 那就意味着客户端是可以自我签发jwt了。
    private static Key secretKey;


    static {
        signatureAlgorithm = SignatureAlgorithm.HS512;
        secretKey = generalKey();
    }

    public static SignatureAlgorithm getSignatureAlgorithm() {
        return signatureAlgorithm;
    }


    /**
     * 创建jwt
     * @param jwt
     * @return
     * @throws Exception
     */
    public static String createJWT(Jwt jwt) throws Exception {

        //生成JWT的时间
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder()
                //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .claim("userId",jwt.getUserId())
                //jwt的签发时间
                .setIssuedAt(now)
                //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid之类的，作为什么用户的唯一标志。
                .setSubject(jwt.getUserId().toString())
                //设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey);

        if (jwt.getExpiredMillisecond() >= 0) {
            long expMillis = nowMillis + jwt.getExpiredMillisecond();
            Date exp = new Date(expMillis);
            //设置过期时间
            builder.setExpiration(exp);
        }

        return builder.compact();



    }

    /**
     * 解密jwt
     * @param token
     * @return
     * @throws Exception
     */
    public static Jwt parseJWT(String token) {
        Jwt jwt = new Jwt();
        try{

            Claims claims = Jwts.parser()  //得到DefaultJwtParser
                    .setSigningKey(secretKey)         //设置签名的秘钥
                    .parseClaimsJws(token).getBody();//设置需要解析的jwt
            jwt.setUserId((Integer) claims.get("userId"));
            jwt.setResult(1);
        }catch (SignatureException e){
            log.error("token解析失败",e);
            jwt.setResult(0);
            jwt.setParseResCode(ErrorCodeEnum.FALI_SIGNATURE.errorCode);
        }catch(ExpiredJwtException e){
            log.error("token过期",e);
            jwt.setResult(0);
            jwt.setParseResCode(ErrorCodeEnum.EXPIRED_SIGNATURE.errorCode);
        }catch(Exception e){
            log.error("token解析异常",e);
            jwt.setResult(0);
            jwt.setParseResCode(ErrorCodeEnum.EXCEPTION_SIGNATURE.errorCode);
        }
        return jwt;
    }



    /**
     * 由字符串生成加密key
     * @return
     */
    public static Key generalKey(){
        byte[] decodedKey = Base64.getEncoder().encode(JWT_SECRET.getBytes());
        // 根据给定的字节数组本token加密算法构造一个密钥
        Key key = new SecretKeySpec(decodedKey, getSignatureAlgorithm().getJcaName());
        return key;
    }

}
