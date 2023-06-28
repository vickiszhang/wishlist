package persistance;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {

            Balance b = new Balance();
            WishList wl = new WishList();
            PurchaseList pl = new PurchaseList();

            JsonWriter writer = new JsonWriter("./data/testWriterEmpty.json");
            writer.open();
            writer.writeAll(b, wl, pl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmpty.json");
            b = reader.readBalance();
            wl = reader.readWishlist();
            pl = reader.readPurchaseList();

            assertEquals(0, b.getCurrentBalance());
            assertEquals(0, b.getDeposits().size());
            assertEquals(0, wl.size());
            assertEquals(0, pl.size());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {

            Balance b = new Balance();
            WishList wl = new WishList();
            PurchaseList pl = new PurchaseList();

            Wish w = new Wish("laptop", 1200.22, Category.ELECTRONICS);
            Purchase p = new Purchase("headphones", 20, Category.ELECTRONICS);

            b.deposit(500);
            wl.addWish(w);
            pl.addPurchase(p);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneral.json");
            writer.open();
            writer.writeAll(b, wl, pl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneral.json");
            b = reader.readBalance();
            wl = reader.readWishlist();
            pl = reader.readPurchaseList();

            assertEquals(500, b.getCurrentBalance());
            assertEquals(1, b.getDeposits().size());
            assertEquals(500, b.getDeposits().get(0));
            assertEquals(1, wl.size());
            assertEquals(1, pl.size());

            checkWish(1200.22, "laptop", Category.ELECTRONICS,
                    false, 0, wl.getWish(0));
            checkPurchase(20, "headphones", Category.ELECTRONICS, pl.getPurchase(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}