package deti.tqs.phihub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;

import static org.assertj.core.api.Assertions.assertThat;

class SimpleTests {

    @Test
    @Order(1)
    void testThatMafsIsMafing() {
        //  Quick mafs
        assertThat(2 + 2 == 4).isSameAs(4 - 1 == 3);
	}
}