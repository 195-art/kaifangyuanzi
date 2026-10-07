package org.example.kaifangyuanzi.exam.Q11.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.example.kaifangyuanzi.exam.Q11.util.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(@Qualifier("q11JwtUtil") JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 解析请求头中的token并设置认证信息
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        //从请求头获取token
        String token = request.getHeader("token");
        if (token != null && !token.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                //从token中解析用户名
                String username = jwtUtil.getUsername(token);
                if (username != null && !username.isBlank()) {
                    //设置认证信息
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(username, null, List.of()));
                }
            } catch (JwtException | IllegalArgumentException e) {
                //token解析失败，清空上下文
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }
}
