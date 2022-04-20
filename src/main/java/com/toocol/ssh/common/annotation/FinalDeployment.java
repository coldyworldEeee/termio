package com.toocol.ssh.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ZhaoZhe
 * @email joezane.cn@gmail.com
 * @date 2021/2/20 12:55
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FinalDeployment {

    /**
     * whether the verticle is worker verticle.
     *
     * @return is worker
     */
    boolean worker() default false;

    /**
     * the pool size of worker verticle
     *
     * @return pool size
     */
    int poolSize() default 20;
}
