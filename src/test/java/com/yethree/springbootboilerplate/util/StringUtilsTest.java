package com.yethree.springbootboilerplate.util;

import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsTest {

    @Test
    void testNotNull() {
        String whiteSpaceString = "       ";

        assertEquals(true, StringUtils.hasLength(whiteSpaceString));
        assertEquals(false, StringUtils.hasText(whiteSpaceString));
    }
}
