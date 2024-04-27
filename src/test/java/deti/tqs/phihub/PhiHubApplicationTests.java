package deti.tqs.phihub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

import static org.assertj.core.api.Assertions.assertThat;

class CachingTests {

    @Test
    @Order(1)
    void testThatCachIsEmpty() {
        assertThat(true).isTrue();
	}
}