package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

public class OrderControllerConcreteProxy extends OrderControllerV2 {

    private final OrderControllerV2 target;
    private final LogTrace logtrace;


    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTrace logtrace) {
        super(null);
        this.target = target;
        this.logtrace = logtrace;
    }


    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = logtrace.begin("OrderController.request()");
            //target 호출
            String result = target.request(itemId);
            logtrace.end(status);
            return result;
        } catch (Exception e) {
            logtrace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return target.noLog();
    }
}
