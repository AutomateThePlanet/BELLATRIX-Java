package solutions.bellatrix.core.utilities;

import org.jetbrains.annotations.NotNull;

public class Log {
    static private final Object lock = new Object();

    public static void info(@NotNull String format, Object ... args) {
        Thread waitThread = new Thread(Log::staticWait);
        Thread printThread = new Thread(() -> {
            String info = String.format(format, args);
            System.out.println(info);
        });
        printThread.start();
        waitThread.start();
    }

    public static void error(@NotNull String format, Object ... args) {
        Thread waitThread = new Thread(Log::staticWait);
        Thread printThread = new Thread(() -> {
            String error = String.format(format, args);
            System.err.println(error);
            staticNotify();
        });
        printThread.start();
        waitThread.start();
    }

    private static void staticWait() {
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                DebugInformation.printStackTrace(e);
            }
        }
    }

    private static void staticNotify() {
        synchronized (lock) {
            lock.notify();
        }
    }
}
