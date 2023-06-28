package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BalanceTest {

    private Balance testBalance;

    @BeforeEach
    void runBefore() {
        testBalance = new Balance();
    }

    @Test
    void testDepositPositive() {
        assertEquals(100, testBalance.deposit(100));
        assertEquals(100, testBalance.deposit(0)); //0
    }

    @Test
    void testDepositNegative() {
        assertEquals(100, testBalance.deposit(100));
        assertEquals(100, testBalance.deposit(-100));
    }

    @Test
    void testDeductToPositive() {
        testBalance.deposit(123.45);
        assertTrue(testBalance.deduct(100.21));
        assertTrue(testBalance.deduct(123.45-100.21)); //0
    }

    @Test
    void testDeductToNegative() {
        testBalance.deposit(50);
        assertFalse(testBalance.deduct(51.0));
    }

    @Test
    void testSetDeposits() {
        assertEquals(0, testBalance.getDeposits().size());
        ArrayList<Double> depositsArray = new ArrayList<>();
        depositsArray.add(900.20);
        depositsArray.add(500.0);
        depositsArray.add(20.9);

        testBalance.setDeposits(depositsArray);
        assertEquals(3, testBalance.getDeposits().size());
        assertEquals(900.20, testBalance.getDeposits().get(0));
        assertEquals(500, testBalance.getDeposits().get(1));
        assertEquals(20.9, testBalance.getDeposits().get(2));
    }

    @Test
    void testGetBalance() {
        assertEquals(0, testBalance.getCurrentBalance());
        testBalance.deposit(323.44);
        assertEquals(323.44, testBalance.getCurrentBalance());
        testBalance.deduct(50);
        assertEquals(323.44-50, testBalance.getCurrentBalance());
        testBalance.deduct(500);
        assertEquals(323.44-50-500, testBalance.getCurrentBalance());
    }

    @Test
    void testGetDeposits() {
        assertEquals(0, testBalance.getDeposits().size());
        testBalance.deposit(900);
        assertEquals(1, testBalance.getDeposits().size());
        assertEquals(900, testBalance.getDeposits().get(0));
        testBalance.deposit(500);
        assertEquals(2, testBalance.getDeposits().size());
        assertEquals(500, testBalance.getDeposits().get(1));
    }

}
