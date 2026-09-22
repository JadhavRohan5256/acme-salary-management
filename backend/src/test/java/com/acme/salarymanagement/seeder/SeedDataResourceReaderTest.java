package com.acme.salarymanagement.seeder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SeedDataResourceReaderTest {
    private SeedDataResourceReader reader;
    private List<List<String>> results;
    
    @BeforeAll
    void beforeEach() {
    	this.reader = new SeedDataResourceReader();
    	this.results = List.of(
    			this.reader.read("first-names.txt"),
    		    this.reader.read("last-names.txt"),
    		    this.reader.read("departments.txt"),
    		    this.reader.read("designations.txt"),
    		    this.reader.read("currencies.txt"),
    		    this.reader.read("countries.txt")
    	);
    }
    
    @Test
    void shouldReadExistingSeedResource() {
        this.results.forEach(result -> {
        	assertNotNull(result);
            assertFalse(result.isEmpty());
        });
    }

    @Test
    void shouldTrimValuesAndIgnoreBlankLines() {
    	this.results.forEach(result -> {    		
    		assertTrue(result.stream().allMatch(value -> value.equals(value.trim())));
    		assertTrue(result.stream().noneMatch(String::isBlank));
    	});
    }

    @Test
    void shouldThrowExceptionWhenResourceDoesNotExist() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> this.reader.read("file-that-does-not-exist.txt"));

        assertTrue(exception.getMessage().contains("Failed to read seed resource"));
        assertTrue(exception.getMessage().contains("file-that-does-not-exist.txt"));
        assertNotNull(exception.getCause());
    }
}