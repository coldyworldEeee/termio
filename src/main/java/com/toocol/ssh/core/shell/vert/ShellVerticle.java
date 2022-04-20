package com.toocol.ssh.core.shell.vert;

import com.toocol.ssh.common.annotation.FinalDeployment;
import com.toocol.ssh.common.annotation.RegisterHandler;
import com.toocol.ssh.common.handler.IHandlerMounter;
import com.toocol.ssh.core.shell.handlers.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.WorkerExecutor;

/**
 * @author ZhaoZhe (joezane.cn@gmail.com)
 * @date 2022/3/31 11:30
 */
@FinalDeployment(worker = true, poolSize = 10)
@RegisterHandler(handlers = {
        EstablishSessionShellChannelHandler.class,
        ShellDisplayHandler.class,
        ShellReceiveHandler.class,
        ExecuteSingleCommandHandler.class,
        ExecuteCommandInCertainShellHandler.class,
        DfHandler.class,
        UfHandler.class
})
public class ShellVerticle extends AbstractVerticle implements IHandlerMounter {

    @Override
    public void start() throws Exception {
        WorkerExecutor executor = vertx.createSharedWorkerExecutor("ssh-shell-worker", 10);

        mountHandler(vertx, executor, true);
    }

}
