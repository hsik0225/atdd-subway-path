package wooteco.subway.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.ui.AuthenticationPrincipalArgumentResolver;
import wooteco.subway.member.application.MemberService;

import java.util.List;

@Configuration
public class AuthenticationPrincipalConfig implements WebMvcConfigurer {
    private final AuthService authService;
    private final MemberService memberService;

    public AuthenticationPrincipalConfig(final AuthService authService, final MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(createAuthenticationPrincipalArgumentResolver());
    }

    @Bean
    public AuthenticationPrincipalArgumentResolver createAuthenticationPrincipalArgumentResolver() {
        return new AuthenticationPrincipalArgumentResolver(authService, memberService);
    }
}
