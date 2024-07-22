package solutions.bellatrix.core.utilities;

import java.time.Duration;

public class Wait {
    public static void retry(Runnable action, int timesToRetry, long sleepInterval, Class<? extends Throwable> ... exceptionsToIgnore) {
        Wait.retry(action, timesToRetry, sleepInterval, true, exceptionsToIgnore);
    }

    public static void retry(Runnable action, int timesToRetry, long sleepInterval,boolean shouldThrowException, Class<? extends Throwable> ... exceptionsToIgnore) {
        int repeat = 0;
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
                        try {
                            Thread.sleep(Duration.ofSeconds(sleepInterval).toMillis());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }

                if (shouldThrowException) {
                    throw exc;
                }
            }
        }
    }

    public static void retry(Runnable action, Class<? extends Throwable> ... exceptionsToIgnore) {
        retry(action, Duration.ofSeconds(30), Duration.ofSeconds(1), exceptionsToIgnore);
    }

    public static boolean retry(Runnable action, Duration timeout, Duration sleepInterval, Boolean shouldThrowException, Class<? extends Throwable> ... exceptionsToIgnore) {
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
                        try {
                            Thread.sleep(sleepInterval.toMillis());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    }
                }

                if (shouldThrowException) {
                    throw exc;
                }
                else {
                    return false;
                }
            }
        }

        return true;
    }

    public static void retry(Runnable action, Duration sleepInterval, Duration timeout, Class<? extends Throwable> ... exceptionsToIgnore) {
        Wait.retry(action, timeout, sleepInterval, true, exceptionsToIgnore);
    }

    public static void forMilliseconds(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
