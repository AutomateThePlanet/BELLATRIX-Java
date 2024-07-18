package solutions.bellatrix.core.utilities;

import java.time.Duration;

public class Wait {
    public static void retry(Runnable action, int timesToRetry, long sleepInterval, Class<? extends Throwable> ... exceptionsToIgnore) {
        Wait.retry(action, timesToRetry, sleepInterval, true, exceptionsToIgnore);
    }

    public static void retry(Runnable action, int timesToRetry, long sleepInterval, boolean shouldThrowException, Class<? extends Throwable> ... exceptionsToIgnore) {
        var shouldThrow = shouldThrowException;
        int repeat = 0;
        while (repeat <= timesToRetry) {
            try {
                shouldThrow = shouldThrowException;
                action.run();
                break;
            } catch (Exception exc) {
                for (var currentException : exceptionsToIgnore) {
                    if (currentException.isInstance(exc)) {
                        //exc.printStackTrace();
                        repeat++;
                        shouldThrow = false;
                        try {
                            Thread.sleep(Duration.ofSeconds(sleepInterval).toMillis());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }

                if (shouldThrow) {
                    throw exc;
                }
            }
        }
    }

    public static void retry(Runnable action, Class<? extends Throwable> ... exceptionsToIgnore) {
        retry(action, Duration.ofSeconds(30), Duration.ofSeconds(1), exceptionsToIgnore);
    }

    public static boolean retry(Runnable action, Duration timeout, Duration sleepInterval, boolean shouldThrowException, Class<? extends Throwable> ... exceptionsToIgnore) {
        boolean returnValue = true;
        boolean shouldThrow = shouldThrowException;

        long start = System.currentTimeMillis();
        long end = start + timeout.toMillis();
        while (System.currentTimeMillis() < end) {
            try {
                shouldThrow = shouldThrowException;
                action.run();
                break;
            } catch (Exception exc) {
                for (var currentException : exceptionsToIgnore) {
                    if (currentException.isInstance(exc)) {
                        //exc.printStackTrace();
                        shouldThrow = false;
                        try {
                            Thread.sleep(sleepInterval.toMillis());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }

                if (shouldThrow) {
                    throw exc;
                } else {
                    returnValue = false;
                }
            }
        }

        return returnValue;
    }

    public static void retry(Runnable action, Duration timeout, Duration sleepInterval, Class<? extends Throwable> ... exceptionsToIgnore) {
        Wait.retry(action, timeout, sleepInterval, true, exceptionsToIgnore);
    }

    public static boolean forConditionUntilTimeout(Comparator condition, long timeoutInMilliseconds, long pollingIntervalInMilliseconds) {
        boolean isConditionMet = false;

        long startTime = System.currentTimeMillis();

        while (!isConditionMet && (System.currentTimeMillis() - startTime) <= (timeoutInMilliseconds)) {
            try {
                if (condition.evaluate()) {
                    isConditionMet = true;
                } else {
                    Thread.sleep(pollingIntervalInMilliseconds);
                }
            } catch (Exception ignored) {
            }
        }

        return isConditionMet;
    }

    @FunctionalInterface
    public interface Comparator {
        boolean evaluate();
    }
}
