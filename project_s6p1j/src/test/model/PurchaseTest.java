package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTest {

    private Purchase testPurchase1;
    private Purchase testPurchase2;

    @BeforeEach
    void runBefore() {
        testPurchase1 = new Purchase("Phone", 500, Category.ELECTRONICS);
        testPurchase2 = new Purchase("Dog", 1000.00, Category.OTHER);
    }

    @Test
    void testGetName() {
        assertEquals("Phone", testPurchase1.getName());
        assertEquals("Dog", testPurchase2.getName());
    }

    @Test
    void testGetPrice() {
        assertEquals(500, testPurchase1.getPrice());
        assertEquals(1000.00, testPurchase2.getPrice());
    }

    @Test
    void testGetCategory() {
        assertEquals(Category.ELECTRONICS, testPurchase1.getCategory());
        assertEquals(Category.OTHER, testPurchase2.getCategory());
    }
}
