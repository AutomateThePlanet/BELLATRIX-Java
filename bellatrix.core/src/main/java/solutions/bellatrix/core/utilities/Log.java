package solutions.bellatrix.core.utilities;

import org.jetbrains.annotations.NotNull;
import solutions.bellatrix.core.plugins.EventListener;

public class Log {
    private static final Object LOCK = new Object();
    public final static EventListener<LoggerActionEventArgs> LOGGED_ENTRY = new EventListener<>();
    public final static EventListener<LoggerActionEventArgs> LOGGED_INFO = new EventListener<>();
    public final static EventListener<LoggerActionEventArgs> LOGGED_ERROR = new EventListener<>();

    public static void info(@NotNull String format, Object... args) {
        Thread waitThread = new Thread(Log::staticWait);
        Thread printThread = new Thread(() -> {
            String info = String.format(format, args);
            LOGGED_ENTRY.broadcast(new LoggerActionEventArgs(info));
            LOGGED_INFO.broadcast(new LoggerActionEventArgs(info));
            System.out.println(info);
        });
        printThread.start();
        waitThread.start();
    }

    public static void error(@NotNull String format, Object... args) {
        Thread waitThread = new Thread(Log::staticWait);
        Thread printThread = new Thread(() -> {
            String error = String.format(format, args);
            LOGGED_ENTRY.broadcast(new LoggerActionEventArgs(error));
            LOGGED_ERROR.broadcast(new LoggerActionEventArgs(error));
            System.err.println(error);
            staticNotify();
        });
        printThread.start();
        waitThread.start();
    }

    private static void staticWait() {
        synchronized (LOCK) {
            try {
                LOCK.wait();
            } catch (InterruptedException e) {
                DebugInformation.printStackTrace(e);
            }
        }
    }

    private static void staticNotify() {
        synchronized (LOCK) {
            LOCK.notify();
        }
    }
}
