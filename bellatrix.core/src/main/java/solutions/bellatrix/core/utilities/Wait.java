package solutions.bellatrix.core.utilities;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

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

    public static void forConditionUntilTimeout(Supplier<Boolean> condition, int totalRunTimeoutMilliseconds, Runnable onTimeout, int sleepTimeMilliseconds) {
        Instant startTime = Instant.now();
        Instant timeout = startTime.plusMillis(totalRunTimeoutMilliseconds);

        while (true) {
            boolean conditionFinished = condition.get();
            if (conditionFinished) {
                break;
            }

            if (Instant.now().compareTo(timeout) >= 0) {
                if (onTimeout != null) {
                    onTimeout.run();
                }

                break;
            }

            try {
                Thread.sleep(sleepTimeMilliseconds);
            } catch (InterruptedException e) {
                // Handle the exception as appropriate
            }
        }
    }
}
