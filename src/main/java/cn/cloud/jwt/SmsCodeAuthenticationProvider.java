package cn.cloud.jwt;

import cn.cloud.config.JwtProperty;
import io.github.xfuns.java.Fun;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 短信登陆鉴权 Provider，要求实现 AuthenticationProvider 接口
 *
 */
@Component
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(SmsCodeAuthenticationProvider.class);
    @Autowired
    private UserDetailsService userDetailsService;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

        String mobile = (String) authenticationToken.getPrincipal();

//        checkSmsCode(mobile);

        UserDetails userDetails = userDetailsService.loadUserByUsername(mobile);

        // 此时鉴权成功后，应当重新 new 一个拥有鉴权的 authenticationResult 返回
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

//    private void checkSmsCode(String mobile) {
//        RedisCache redisCache = (RedisCache) SpringContextUtil.getBean("redisCache");
//
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String inputCode = request.getParameter("smsCode");
//        String uuid = request.getParameter("uuid");
//
//        String verifyKey = Constants.SMS_CAPTCHA_CODE_KEY + uuid;
//
//        Map<String, Object> smsCode =   redisCache.getCacheObject(verifyKey);
////        redisCache.deleteObject(verifyKey);
//
//        if(smsCode == null) {
//            throw new BadCredentialsException("验证码失效");
//        }
//
//        String applyMobile = (String) smsCode.get("mobile");
//        int code = (int) smsCode.get("code");
//
//        if(!applyMobile.equals(mobile)) {
//            throw new BadCredentialsException("手机号码不一致");
//        }
//        if(code != Integer.parseInt(inputCode)) {
//            throw new BadCredentialsException("验证码错误");
//        }
//    }

    @Override
    public boolean supports(Class<?> authentication) {
        // 判断 authentication 是不是 SmsCodeAuthenticationToken 的子类或子接口
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    private JwtProperty jwtProperty;

    /**
     * 生成字符串令牌
     *
     * @param authentication
     * @return
     */
    public String generateTokenByString(Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(jwtUser.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperty.getExpiration()))
                .signWith(SignatureAlgorithm.HS512, jwtProperty.getSecret())
                .compact();
    }

    /**
     * 生成整型令牌
     *
     * @param authentication
     * @return
     */
    public String generateTokenByLong(Authentication authentication) {
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(Long.toString(jwtUser.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperty.getExpiration()))
                .signWith(SignatureAlgorithm.HS512, jwtProperty.getSecret())
                .compact();
    }

    /**
     * 从令牌 Subject 中获取整型
     *
     * @param token 令牌
     * @return 用户名
     */
    public Long getLongFromToken(String token) {

        Claims claims = getClaimsFromToken(token);
        String id = claims.getSubject();

        return Fun.toLong(id);
    }

    /**
     * 从令牌 Subject 中获取字符串
     *
     * @param token
     * @return
     */
    public String getStringFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * 验证 Token
     *
     * @param token
     * @return
     */
    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperty.getSecret()).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    /**
     * 从令牌中获取数据
     *
     * @param token
     * @return
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperty.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            claims = null;
        }
        return claims;
    }
}
