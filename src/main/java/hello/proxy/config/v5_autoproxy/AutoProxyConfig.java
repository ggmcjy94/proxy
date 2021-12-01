package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    /**
     * 스프링 부트 자동 설정으로 AnnotationAwareAspectJAutoProxyCreator 라는 빈 후처리기가 스프링 빈에 자동으로 등록된다.
     * 이름 그대로 자동으로 프록시를 생성해주는 빈 후처리기이다.
     * 이 빈 후처리기는 스프링 빈으로 등록된 Advisor 들을 자동으로 찾아서 프록시가 필요한 곳에 자동으로 프록시를 적용해준다.
     * Advisor 안에는 Pointcut 과 Advice 가 이미 모두 포함되어 있다. 따라서 Advisor 만 알고 있으면
     * 그 안에있는 Pointcut으로 어떤 스프링빈에 프록시를 적용해야 할지 알수있다.그리고 Advice로부가 기능을 적용하면 된다.
     * > 참고
     * > AnnotationAwareAspectJAutoProxyCreator 는 @AspectJ와 관련된 AOP 기능도 자동으로 찾아서
     * 처리해준다.
     * > Advisor 는 물론이고, @Aspect 도 자동으로 인식해서 프록시를 만들고 AOP를 적용해준다. @Aspect 에
     * 대한 자세한 내용은 뒤에 설명한다.
     */

//    @Bean
    public Advisor advisor1(LogTrace logtrace) {
        //pointcut
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logtrace);
        return new DefaultPointcutAdvisor(pointcut ,advice);
    }

//    @Bean
    public Advisor advisor2(LogTrace logTrace) {
        //AspectJExpressionPointcut 실무에서 많이 쓰임
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut ,advice);
    }
    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        //AspectJExpressionPointcut 실무에서 많이 쓰임
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");
        //advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut ,advice);
    }
}
