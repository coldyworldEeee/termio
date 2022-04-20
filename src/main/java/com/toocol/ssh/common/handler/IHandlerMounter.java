package com.toocol.ssh.common.handler;

import com.toocol.ssh.common.annotation.RegisterHandler;
import com.toocol.ssh.common.utils.ICastable;
import com.toocol.ssh.core.term.core.Printer;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * @author ZhaoZhe (joezane.cn@gmail.com)
 * @date 2022/3/30 11:02
 */
public interface IHandlerMounter extends ICastable {
    /**
     * assemble the handler to the eventBus
     *
     * @param vertx the vertx system object
     * @param context the verticle's executor
     * @param parallel whether the handlers is handle parallel
     * @param injects objs to inject
     * @param <T>  generic type
     */
    @SuppressWarnings("all")
    default <T> void mountHandler(Vertx vertx, Context context, boolean parallel) {
        Class<? extends IHandlerMounter> clazz = this.getClass();
        RegisterHandler registerHandler = clazz.getAnnotation(RegisterHandler.class);
        if (registerHandler == null) {
            return;
        }

        Arrays.stream(registerHandler.handlers()).forEach(handlerClass -> {
            try {

                Constructor<? extends AbstractMessageHandler<?>> declaredConstructor = cast(handlerClass.getDeclaredConstructor(Vertx.class, Context.class, boolean.class));
                declaredConstructor.setAccessible(true);
                AbstractMessageHandler<?> commandHandler = declaredConstructor.newInstance(vertx, context, parallel);
                vertx.eventBus().consumer(commandHandler.consume().address(), commandHandler::handle);

            } catch (Exception e) {
                e.printStackTrace();
                Printer.printErr("Assemble handler failed, message = " + e.getMessage());
                System.exit(-1);
            }
        });
    }

    /**
     * assemble the handler to the eventBus
     *
     * @param vertx the vertx system object
     * @param context the verticle's executor
     */
    default void mountHandler(Vertx vertx, Context context) {
        mountHandler(vertx, context,false);
    }
}
