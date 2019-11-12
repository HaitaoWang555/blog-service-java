package com.wht.blog.util;

import com.wht.blog.exception.TipException;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author wht
 * @since 2019-11-12 16:53
 */
@Slf4j
public class JwtUtil {

    @Resource
    private static StringRedisTemplate stringRedisTemplate;

    /**
     * 创建 jwt
     * @param userId userId
     * @return jwt
     */
    private String createJWT (Integer userId) {
        Date now = new Date();
        String id = userId.toString();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, "xkcodingsfdgsdg");
        Long ttl = 120000L;
        builder.setExpiration(new Date(now.getTime() + ttl));
        String jwt = builder.compact();
        // 将生成的JWT保存至Redis
        stringRedisTemplate.opsForValue()
                .set(Const.REDIS_JWT_KEY_PREFIX + id, jwt, ttl, TimeUnit.MILLISECONDS);
        return jwt;
    }

    /**
     * 解析JWT
     * @param jwt String
     * @return Claims jwt
     */
    private static Claims parseJWT(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("xkcodingsfdgsdg")
                    .parseClaimsJws(jwt)
                    .getBody();

            // 校验redis中的JWT是否存在
            String redisKey = Const.REDIS_JWT_KEY_PREFIX + claims.getId();
            Long expire = stringRedisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS);
            if (Objects.isNull(expire) || expire <= 0) {
                throw new TipException(ErrorCode.TOKEN_EXPIRED);
            }

            // 校验redis中的JWT是否与当前的一致，不一致则代表用户已注销/用户在不同设备登录，均代表JWT已过期
            String redisToken = stringRedisTemplate.opsForValue()
                    .get(redisKey);
            if (!jwt.equals(redisToken)) {
                log.error("Token 已过期");
                throw new TipException(ErrorCode.TOKEN_EXPIRED);
            }
            return claims;
        } catch (ExpiredJwtException e) {
            log.error("Token 已过期");
            throw new TipException(ErrorCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("不支持的 Token");
            throw new TipException(ErrorCode.TOKEN_PARSE_ERROR);
        } catch (MalformedJwtException e) {
            log.error("Token 无效");
            throw new TipException(ErrorCode.TOKEN_PARSE_ERROR);
        } catch (SignatureException e) {
            log.error("无效的 Token 签名");
            throw new TipException(ErrorCode.TOKEN_PARSE_ERROR);
        } catch (IllegalArgumentException e) {
            log.error("Token 参数不存在");
            throw new TipException(ErrorCode.TOKEN_PARSE_ERROR);
        }
    }

    /**
     * 获取已登录用户 userId
     * @param jwt String
     * @return userId Integer
     */
    public static Integer getLoginUserId(String jwt) {
        Claims claims = parseJWT(jwt);
        return Integer.valueOf(claims.getId());
    }

    /**
     * 设置JWT过期
     * @param jwt String
     * @return 是否清除 Boolean
     */
    public Boolean invalidateJWT(String jwt) {
        String userId = getLoginUserId(jwt).toString();
        // 从redis中清除JWT
        String redisKey = Const.REDIS_JWT_KEY_PREFIX + userId;
        return stringRedisTemplate.delete(redisKey);
    }
    /**
     * 从 request 的 header 中获取 JWT
     * @param request 请求
     * @return JWT
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
