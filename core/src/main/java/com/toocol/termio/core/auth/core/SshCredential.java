package com.toocol.termio.core.auth.core;

import com.toocol.termio.core.shell.core.ShellProtocol;
import com.toocol.termio.utilities.functional.Switchable;
import io.vertx.core.json.JsonObject;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author ZhaoZhe
 * @email joezane.cn@gmail.com
 * @date 2021/2/19 16:57
 */
public class SshCredential implements Serializable, Switchable {

    @Serial
    private static final long serialVersionUID = 1184930928749870706L;

    /**
     * the ip of target server.
     */
    private String host;
    /**
     * the user of target server.
     */
    private String user;
    /**
     * the password of target server.
     */
    private String password;
    /**
     * the port of target server.
     */
    private int port;
    /**
     * whether the connection is a JumpServer
     */
    private boolean jumpServer;

    public SshCredential() {
    }

    public SshCredential(String host, String user, String password, int port, boolean jumpServer) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
        this.jumpServer = jumpServer;
    }

    public SshCredential(String host, String user, String password, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public static SshCredential transFromJson(JsonObject jsonObject) {
        return new SshCredential(
                jsonObject.getString("host"),
                jsonObject.getString("user"),
                jsonObject.getString("password"),
                jsonObject.getInteger("port"),
                jsonObject.getBoolean("jumpServer", false)
        );
    }

    public static SshCredentialBuilder builder() {
        return new SshCredentialBuilder();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>(4);
        map.put("host", host);
        map.put("user", user);
        map.put("password", password);
        map.put("port", port);
        return map;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isJumpServer() {
        return jumpServer;
    }

    public void setJumpServer(boolean jumpServer) {
        this.jumpServer = jumpServer;
    }

    @Override
    public String uri() {
        return user + "@" + host;
    }

    @Override
    public String protocol() {
        return ShellProtocol.SSH.name();
    }

    @Override
    public String currentPath() {
        return "*";
    }

    @Override
    public boolean alive() {
        return false;
    }

    @Override
    public int weight() {
        return 1;
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

    public static final class SshCredentialBuilder {
        private String host;
        private String user;
        private String password;
        private int port;
        private boolean jumpServer;

        private SshCredentialBuilder() {
        }

        public SshCredentialBuilder host(String host) {
            this.host = host;
            return this;
        }

        public SshCredentialBuilder user(String user) {
            this.user = user;
            return this;
        }

        public SshCredentialBuilder password(String password) {
            this.password = password;
            return this;
        }

        public SshCredentialBuilder port(int port) {
            this.port = port;
            return this;
        }

        public SshCredentialBuilder jumpServer(boolean jumpServer) {
            this.jumpServer = jumpServer;
            return this;
        }

        public SshCredential build() {
            SshCredential sshCredential = new SshCredential();
            sshCredential.setHost(host);
            sshCredential.setUser(user);
            sshCredential.setPassword(password);
            sshCredential.setPort(port);
            sshCredential.setJumpServer(jumpServer);
            return sshCredential;
        }
    }
}