package org.example.kaifangyuanzi.exam.Q11.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 起个别名 q11SecurityConfig，避免和 Q10 的 SecurityConfig（默认叫 securityConfig）撞名
@Configuration("q11SecurityConfig")
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // @Order 必须放在 @Bean 方法上才生效：数字越小，这条过滤器链排得越靠前
    // 这样 /Q11 的请求会先被我们这条规则处理，Q10 的"全部放行"链排在最后兜底
    @Bean
    @Order(-100)
    public SecurityFilterChain q11SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // 只管 /Q11 开头的请求，其他路径（Q10 的）一概不管
                .securityMatcher("/Q11/**")

                // 关掉 CSRF 跨站伪造防护（教学项目统一关，不然 POST 会被拦）
                .csrf(csrf -> csrf.disable())

                // 无状态：不用 session，认 token
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ===== 核心规则：谁能访问什么 =====
                .authorizeHttpRequests(auth -> auth
                        // 1. GET（查看列表、详情）→ 游客也能访问
                        .requestMatchers(HttpMethod.GET, "/Q11/event/**").permitAll()
                        // 2. 第四阶段前端的静态页面 → 游客也能看
                        .requestMatchers("/", "/index.html", "/static/**", "/favicon.ico").permitAll()
                        // 3. 其他请求（POST 发起 / PUT 修改 / DELETE 删除）→ 必须登录
                        .anyRequest().authenticated()
                )

                // 把第 2 步写的"门卫过滤器"挂进来，在判断权限之前先认 token
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
