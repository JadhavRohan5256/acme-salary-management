package com.acme.salarymanagement.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BadRequestExceptionTest {
    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Invalid sort field: abc";
        BadRequestException exception = new BadRequestException(message);

        assertEquals(message, exception.getMessage());
    }
}