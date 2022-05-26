package bgu.spl.mics.application.passiveObjects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
Ewok e;

    @BeforeEach
    public void setUp(){
        e = new Ewok(0);
    }

    @Test
    void acquireTest() {
        assertTrue(e.isAvailable());
        e.acquire();
        assertFalse(e.isAvailable());
    }

    @Test
    void releaseTest() {
        e.available=false;
        e.release();
        assertTrue(e.isAvailable());
    }
}