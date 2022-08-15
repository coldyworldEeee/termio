package com.toocol.termio.core.mosh.core;

import com.toocol.termio.core.mosh.core.network.MoshInputStream;
import com.toocol.termio.core.mosh.core.network.MoshOutputStream;
import com.toocol.termio.core.mosh.core.network.Transport;
import com.toocol.termio.core.mosh.core.statesnyc.CompleteTerminal;
import com.toocol.termio.core.mosh.core.statesnyc.UserEvent;
import com.toocol.termio.utilities.functional.Switchable;
import com.toocol.termio.utilities.log.Loggable;
import com.toocol.termio.utilities.utils.IpUtil;
import io.vertx.core.Vertx;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.eventbus.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Objects;

/**
 * @author ZhaoZhe (joezane.cn@gmail.com)
 * @date 2022/4/25 19:58
 */
public final class MoshSession implements Loggable, Switchable {

    private final IO io;
    private final Vertx vertx;
    private final Transport<CompleteTerminal> transport;
    private final long sessionId;
    private final String host;
    private DatagramSocket socket;

    private volatile String user;
    private volatile boolean connected = false;

    public MoshSession(Vertx vertx, long sessionId, String host, String user, int port, String key) {
        this.vertx = vertx;
        this.io = new IO();
        this.sessionId = sessionId;
        this.host = host;
        this.user = user;
        this.transport = new Transport<>(host, port, key, new CompleteTerminal());
    }

    // handle by event loop.
    public <T> void connect(Message<T> message) {
        try {
            IpUtil.getLocalIp4Address().ifPresent(localIpv4 -> {
                try {
                    this.socket = vertx.createDatagramSocket(new DatagramSocketOptions());
                    this.transport.connect(socket);

                    socket.listen(transport.addr.port(), localIpv4.toString().replaceFirst("/", ""), result -> {
                        if (result.succeeded()) {
                            socket.handler(this.transport::receivePacket);
                            this.connected = true;
                            info("Mosh-client success to listened local port: {}", transport.addr.port());
                            message.reply(null);
                        } else {
                            error("Mosh-client fail to listened local port: {}", transport.addr.port());
                            message.fail(-1, "Mosh fail to listened local port" + transport.addr.port());
                        }
                    });

                } catch (Exception e) {
                    message.fail(-1, e.getMessage());
                }
            });
        } catch (SocketException e) {
            message.fail(-1, e.getMessage());
        }
    }

    public void tick() {
        transport.tick();
    }

    public void resize(int width, int height) {
        transport.pushBackEvent(new UserEvent.Resize(width, height));
    }

    public synchronized InputStream getInputStream() throws IOException {
        if (!connected) {
            throw new IOException("Mosh session is not connected, sessionId = " + sessionId);
        }
        if (this.io.nonNull()) {
            this.io.close();
        }
        this.io.inputStream = new MoshInputStream();
        setOutputStream(new MoshOutputStream(this.io.inputStream, transport));
        return this.io.inputStream;
    }

    public synchronized OutputStream getOutputStream() throws IOException {
        if (!connected) {
            throw new IOException("Mosh session is not connected, sessionId = " + sessionId);
        }
        return this.io.outputStream;
    }

    private void setOutputStream(MoshOutputStream outputStream) {
        if (this.io.outputStream != null) {
            try {
                this.io.outputStream.close();
            } catch (IOException e) {
                // do nothing
            }
        }
        this.io.outputStream = outputStream;
        this.io.outputStream.waitReading();
    }

    public void close() {
        try {
            this.connected = false;
            this.socket.close();
            this.io.outputStream.close();
            this.io.inputStream.close();
        } catch (Exception e) {
            // do nothing
        }
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public boolean isConnected() {
        return connected;
    }

    public long getSessionId() {
        return sessionId;
    }

    @Override
    public String uri() {
        return null;
    }

    @Override
    public String protocol() {
        return null;
    }

    @Override
    public String currentPath() {
        return null;
    }

    @Override
    public boolean alive() {
        return false;
    }

    @Override
    public int weight() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || (getClass() != o.getClass() && !(o instanceof Switchable))) return false;
        Switchable that = (Switchable) o;
        return Objects.equals(uri(), that.uri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, user);
    }

    private static class IO {
        private MoshOutputStream outputStream;
        private MoshInputStream inputStream;

        public boolean nonNull() {
            return outputStream != null && inputStream != null;
        }

        public void close() {
            try {
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                // do nothing
            }
        }
    }
}