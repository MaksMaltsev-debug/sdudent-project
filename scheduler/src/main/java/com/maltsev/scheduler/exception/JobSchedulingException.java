package com.maltsev.scheduler.exception;

public class JobSchedulingException extends RuntimeException {
    public JobSchedulingException(String message) {
        super(message);
    }
}
