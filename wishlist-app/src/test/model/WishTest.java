package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WishTest {

    private Wish testWish1;
    private Wish testWish2;

    @BeforeEach
    void runBefore() {

        testWish1 = new Wish("Laptop", 1499.99, Category.ELECTRONICS);
        testWish2 = new Wish("TV", 2001.15, Category.ELECTRONICS);
    }

    @Test
    void testAllocate() {
        assertEquals(100, testWish1.allocate(100));
        assertEquals(201, testWish1.allocate(101));

    }
    @Test
    void testAllocateExceeds() {
        assertEquals(1499.99, testWish1.allocate(2000));
    }

    @Test
    void testAllocateExact() {
        assertEquals(2001.15, testWish2.allocate(2001.15));
    }

    @Test
    void testSetBought() {
        assertFalse(testWish1.isBought());
        testWish1.setBought();
        assertTrue(testWish1.isBought());

        testWish1.setBought();
        assertTrue(testWish1.isBought());
    }

    @Test
    void testGetName() {
        assertEquals("Laptop", testWish1.getName());
        assertEquals("TV", testWish2.getName());
    }

    @Test
    void testGetPrice() {
        assertEquals(1499.99, testWish1.getPrice());
        assertEquals(2001.15, testWish2.getPrice());
    }

    @Test
    void testGetCategory() {
        assertEquals(Category.ELECTRONICS, testWish1.getCategory());
        assertEquals(Category.ELECTRONICS, testWish2.getCategory());
    }

    @Test
    void testIsBought() {
        assertFalse(testWish1.isBought());
        testWish1.setBought();
        assertTrue(testWish1.isBought());
    }

    @Test
    void testGetAllocated() {
        assertEquals(0, testWish2.getAllocated());
        testWish2.allocate(129.00);
        assertEquals(129.00, testWish2.getAllocated());
        testWish2.allocate(300.23);
        assertEquals(129.00 + 300.23, testWish2.getAllocated());
        testWish2.allocate(2000);
        assertEquals(2001.15, testWish2.getAllocated());
    }





}