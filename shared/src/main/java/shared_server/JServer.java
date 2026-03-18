package shared_server;

public interface JServer {
    public int run(int desiredPort);
    public void stop();
    public int getPort();
}
