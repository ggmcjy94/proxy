package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace logtrace;

    public LogTraceBasicHandler(Object target, LogTrace logtrace) {
        this.target = target;
        this.logtrace = logtrace;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

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
