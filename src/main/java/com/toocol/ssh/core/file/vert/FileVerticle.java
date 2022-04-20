package com.toocol.ssh.core.file.vert;

import com.toocol.ssh.common.annotation.PreloadDeployment;
import com.toocol.ssh.common.annotation.RegisterHandler;
import com.toocol.ssh.common.handler.IHandlerMounter;
import com.toocol.ssh.core.file.handlers.CheckFileExistHandler;
import com.toocol.ssh.core.file.handlers.ChooseFileHandler;
import com.toocol.ssh.core.file.handlers.ReadFileHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.WorkerExecutor;

/**
 * read/write file through the local file system.
 *
 * @author ZhaoZhe
 * @email joezane.cn@gmail.com
 * @date 2021/2/19 16:27
 */
@PreloadDeployment(weight = 10, worker = true, poolSize = 2)
@RegisterHandler(handlers = {
        CheckFileExistHandler.class,
        ReadFileHandler.class,
        ChooseFileHandler.class
})
public class FileVerticle extends AbstractVerticle implements IHandlerMounter {

    @Override
    public void start() throws Exception {
        final WorkerExecutor executor = vertx.createSharedWorkerExecutor("file-worker", 2);

        mountHandler(vertx, executor);
    }

}
