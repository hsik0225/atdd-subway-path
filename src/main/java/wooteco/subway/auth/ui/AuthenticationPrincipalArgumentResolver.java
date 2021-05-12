package wooteco.subway.auth.ui;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.domain.AuthenticationPrincipal;
import wooteco.subway.auth.infrastructure.AuthorizationExtractor;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;
    private final MemberService memberService;

    public AuthenticationPrincipalArgumentResolver(final AuthService authService, final MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    // parameter에 @AuthenticationPrincipal이 붙어있는 경우 동작
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        final String accessToken =
                AuthorizationExtractor.extract(Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class)));
        authService.verifyToken(accessToken);
        final MemberResponse member = memberService.findMemberByEmail(authService.parseEmail(accessToken));
        // TODO: 유효한 로그인인 경우 LoginMember 만들어서 응답하기
        return LoginMember.from(member);
    }
}
