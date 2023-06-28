package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WishListTest {
    private WishList testList;

    private Wish w1;
    private Wish w2;
    private Wish w3;
    private Wish w4;

    @BeforeEach
    void runBefore() {
        testList = new WishList();
        w1 = new Wish("TV", 2000.00, Category.ELECTRONICS);
        w2 = new Wish("Car", 100000.00, Category.LIFESTYLE);
        w3 = new Wish("Bike", 200, Category.LIFESTYLE);
        w4 = new Wish("Phone", 200, Category.ELECTRONICS);

    }

    @Test
    void testAddWish() {
        assertTrue(testList.addWish(w1));
        assertTrue(testList.addWish(w2));
    }

    @Test
    void testRemoveWish() {
        fillList();
        assertTrue(testList.removeWish(w2));
        assertTrue(testList.removeWish(w1));
    }

    @Test
    void testRemoveWishNotIn() {
        assertFalse(testList.removeWish(w1));
        testList.addWish(w2);
        assertFalse(testList.removeWish(w3));
    }

    @Test
    void testGetWish() {
        fillList();
        assertEquals(w1, testList.getWish(0));
        assertEquals(w4, testList.getWish(3));
    }

    @Test
    void testSize() {
        fillList();
        assertEquals(4, testList.size());
        testList.removeWish(w3);
        assertEquals(3, testList.size());

    }

    private void fillList(){
        testList.addWish(w1);
        testList.addWish(w2);
        testList.addWish(w3);
        testList.addWish(w4);
    }

}
