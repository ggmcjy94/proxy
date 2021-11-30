package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
@Slf4j
public class LogTraceFilterHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logtrace;
    private final String[] patterns;


    public LogTraceFilterHandler(Object target, LogTrace logtrace, String[] patterns) {
        this.target = target;
        this.logtrace = logtrace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //메서드 이름 필터
        String methodName = method.getName();
        log.info("methodName = {}", methodName);
        // save, request, reque*, *est
        if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
            return method.invoke(target, args);
        }

        TraceStatus status = null;
        try {
            String message = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = logtrace.begin(message);

            //로직 호출
            Object result = method.invoke(target, args);
            logtrace.end(status);
            return result;
        } catch (Exception e) {
            logtrace.exception(status, e);
            throw e;
        }
    }
}
