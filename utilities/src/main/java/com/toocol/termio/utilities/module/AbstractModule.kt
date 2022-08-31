package com.toocol.termio.utilities.module

import com.toocol.termio.utilities.log.Loggable
import io.vertx.core.AbstractVerticle

/**
 * @author ZhaoZhe (joezane.cn@gmail.com)
 * @date 2022/8/16 16:37
 */
abstract class AbstractModule : AbstractVerticle(), IHandlerMounter, Loggable