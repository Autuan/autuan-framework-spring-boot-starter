package top.autuan.web.exception;

public class RetryableException {
    private final int maxRetryNum = 3;

    private int retryCount;

    private String id;

    private int intervalSecond;

    private Object overLimitException;
}
