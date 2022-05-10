package com.toocol.ssh.core.mosh.core.network;

import java.io.PipedInputStream;

/**
 * @author ：JoeZane (joezane.cn@gmail.com)
 * @date: 2022/4/28 22:17
 * @version: 0.0.1
 */
public final class MoshInputStream extends PipedInputStream {

    private static final int DEFAULT_BUFFER_SIZE = 32768;

    public MoshInputStream() {
        super(DEFAULT_BUFFER_SIZE);
    }

}
