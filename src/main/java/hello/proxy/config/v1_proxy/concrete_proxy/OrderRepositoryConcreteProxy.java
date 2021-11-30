package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryConcreteProxy extends OrderRepositoryV2 {


    private final OrderRepositoryV2 target;
    private final LogTrace logtrace;

    @Override
    public void save(String itemId) {
        TraceStatus status = null;
        try {
            status = logtrace.begin("OrderRepository.request()");
            //target 호출
            target.save(itemId);
            logtrace.end(status);
        } catch (Exception e) {
            logtrace.exception(status, e);
            throw e;
        }
    }
}
