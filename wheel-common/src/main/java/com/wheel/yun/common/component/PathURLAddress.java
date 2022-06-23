package com.wheel.yun.common.component;

/**
 * @author jack_yun
 * @version 1.0
 * @description: TODO
 * @date 2022/5/28 19:06
 */
public class PathURLAddress extends URLAddress{
    private String protocol;
    private String username;
    private String password;
    private String path;

    private transient String address;
    private transient String ip;
    public PathURLAddress(String protocol, String username, String password, String path, String host, int port) {
        this(protocol, username, password, path, host, port, null);
    }

    public PathURLAddress(String protocol, String username, String password, String path, String host, int port, String rawAddress) {
        super(host, port, rawAddress);

        this.protocol = protocol;
        this.username = username;
        this.password = password;

        // trim the beginning "/"
        while (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path != null) {
            path = path.intern();
        }
        this.path = path;
    }
    public String getProtocol() {
        return protocol;
    }

    public URLAddress setProtocol(String protocol) {
        return new PathURLAddress(protocol, username, password, path, host, port, rawAddress);
    }

    public String getUsername() {
        return username;
    }

    public URLAddress setUsername(String username) {
        return new PathURLAddress(protocol, username, password, path, host, port, rawAddress);
    }

    public String getPassword() {
        return password;
    }

    public PathURLAddress setPassword(String password) {
        return new PathURLAddress(protocol, username, password, path, host, port, rawAddress);
    }

    public String getPath() {
        return path;
    }

    public PathURLAddress setPath(String path) {
        return new PathURLAddress(protocol, username, password, path, host, port, rawAddress);
    }

    @Override
    public URLAddress setHost(String host) {
        return new PathURLAddress(protocol, username, password, path, host, port, rawAddress);
    }

    @Override
    public URLAddress setPort(int port) {
        return new PathURLAddress(protocol, username, password, path, host, port, rawAddress);
    }

    @Override
    public URLAddress setAddress(String host, int port) {
        return new PathURLAddress(protocol, username, password, path, host, port, rawAddress);
    }

    public String getAddress() {
        if (address == null) {
            address = getAddress(getHost(), getPort());
        }
        return address;
    }
}
