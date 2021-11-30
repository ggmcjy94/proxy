package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderServiceInterfaceProxy implements OrderServiceV1 {

    private final OrderServiceV1 target;
    private final LogTrace logtrace;


    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = logtrace.begin("OrderService.request()");
            //target 호출
            target.orderItem(itemId);
            logtrace.end(status);
        } catch (Exception e) {
            logtrace.exception(status, e);
            throw e;
        }
    }
}
