package ffxiv.housim.app;

import com.google.common.eventbus.EventBus;

/**
 * desc:
 *
 * @author yanmaoyuan
 * @date 2021/9/1
 */
public enum EventUtil {
    INSTANCE;

    private EventBus eventBus;

    EventUtil() {
        eventBus = new EventBus("ffxiv-housim");
    }

    public void post(Object event) {
        eventBus.post(event);
    }

    public void register(Object object) {
        eventBus.register(object);
    }

}
