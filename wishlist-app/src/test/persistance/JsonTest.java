package persistance;


import model.Category;
import model.Purchase;
import model.PurchaseList;
import model.Wish;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {


    protected void checkWish(double price, String name, Category category,
                             boolean isBought, double allocated, Wish wish){
        assertEquals(price, wish.getPrice());
        assertEquals(name, wish.getName());
        assertEquals(category, wish.getCategory());
        assertEquals(isBought, wish.isBought());
        assertEquals(allocated, wish.getAllocated());

    }
    protected void checkPurchase(double price, String name, Category category, Purchase purchase) {
        assertEquals(price, purchase.getPrice());
        assertEquals(name, purchase.getName());
        assertEquals(category, purchase.getCategory());

    }
}
