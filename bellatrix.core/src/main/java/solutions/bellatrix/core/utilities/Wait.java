package solutions.bellatrix.core.utilities;

import java.time.Duration;

public class Wait {
    public static void retry(Runnable action, int timesToRetry, long sleepInterval, Class<? extends Throwable> ... exceptionsToIgnore) throws InterruptedException {
        int repeat = 0;
        boolean shouldThrowException = true;
        while(repeat <= timesToRetry) {
            try {
                shouldThrowException = true;
                action.run();
                break;
            } catch(Exception exc) {
                for (var currentException : exceptionsToIgnore) {
                    if (currentException.isInstance(exc)) {
                        //exc.printStackTrace();
                        repeat++;
                        shouldThrowException = false;
                        Thread.sleep(Duration.ofSeconds(sleepInterval).toMillis());
                        break;
                    }
                }

                if (shouldThrowException) {
                    throw exc;
                }
            }
        }
    }

    public static void retry(Runnable action, Class<? extends Throwable> ... exceptionsToIgnore) throws InterruptedException {
        retry(action, Duration.ofSeconds(30), Duration.ofSeconds(1), exceptionsToIgnore);
    }

    public static void retry(Runnable action, Duration timeout, Duration sleepInterval, Class<? extends Throwable> ... exceptionsToIgnore) throws InterruptedException {
        boolean shouldThrowException = true;
        long start = System.currentTimeMillis();
        long end = start + timeout.toMillis();
        while(System.currentTimeMillis() < end) {
            try {
                shouldThrowException = true;
                action.run();
                break;
            } catch(Exception exc) {
                for (var currentException : exceptionsToIgnore) {
                    if (currentException.isInstance(exc)) {
                        //exc.printStackTrace();
                        shouldThrowException = false;
                        Thread.sleep(sleepInterval.toMillis());
                        break;
                    }
                }

                if (shouldThrowException) {
                    throw exc;
                }
            }
        }
    }
}
