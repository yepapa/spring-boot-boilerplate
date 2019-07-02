package com.yethree.springbootboilerplate.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringTest {
    @Test
    void testStringPool() {
        String contentString1 = "yepapa";
        String contentString2 = "yepapa";
        String contentString3 = new String("yepapa");

        assertThat(contentString1).isSameAs(contentString2);
        assertThat(contentString1).isNotSameAs(contentString3);

        String contentString4 = Integer.toString(10);

        assertThat(contentString4).isNotSameAs("10");

        contentString4 = contentString4.intern();
        assertThat(contentString4).isSameAs("10");

        assertThat(DummyObject.contentStringDummy).isSameAs("hello");

        new StringBuilder(contentString1).reverse().toString();


        String str = "\uD835\uDD38BC"; // Three characters: A, B, C (4 chars)
        System.out.println(str); // prints ABC (A is the double-struck A)
    }

    class DummyObject {
        public static final String contentStringDummy = "hello";
    }
}
