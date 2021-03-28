package me.johara.picocli.monitor;

public interface PinStateMonitor<T> {

    String getName();

    void initialize();

    void terminate();

    void start();

    void stop();

}
