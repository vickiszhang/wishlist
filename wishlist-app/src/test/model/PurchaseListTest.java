package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseListTest {

    private PurchaseList testList;
    private Purchase p1;
    private Purchase p2;
    private Purchase p3;
    private Purchase p4;

    @BeforeEach
    void runBefore() {
        testList = new PurchaseList();
        p1 = new Purchase("TV", 2000.00, Category.ELECTRONICS);
        p2 = new Purchase("Car", 100000.00, Category.LIFESTYLE);
        p3 = new Purchase("Bike", 200, Category.LIFESTYLE);
        p4 = new Purchase("Phone", 200, Category.ELECTRONICS);

    }

    @Test
    void testAddPurchase() {
        assertTrue(testList.addPurchase(p1));
        assertTrue(testList.addPurchase(p2));
    }
    @Test
    void testRemovePurchase() {
        fillList();

        assertTrue(testList.removePurchase(p1));
        assertTrue(testList.removePurchase(p2));
    }

    @Test
    void testRemovePurchaseNotIn() {
        assertFalse(testList.removePurchase(p1));
        testList.addPurchase(p2);
        assertFalse(testList.removePurchase(p3));
    }

    @Test
    void testGetPurchase() {
        fillList();
        assertEquals(p1, testList.getPurchase(0));
        assertEquals(p4, testList.getPurchase(3));
    }

    private void fillList() {
        testList.addPurchase(p1);
        testList.addPurchase(p2);
        testList.addPurchase(p3);
        testList.addPurchase(p4);
    }

}
