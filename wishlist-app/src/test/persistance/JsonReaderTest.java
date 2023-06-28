package persistance;

import model.Balance;
import model.Category;
import model.PurchaseList;
import model.WishList;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            JSONObject jObject = reader.readFrom();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmpty.json");
        try {
            Balance b = reader.readBalance();
            WishList wl = reader.readWishlist();
            PurchaseList pl = reader.readPurchaseList();

            assertEquals(0, b.getCurrentBalance());
            assertEquals(0, b.getDeposits().size());
            assertEquals(0, wl.size());
            assertEquals(0, pl.size());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneral.json");
        try {
            Balance b = reader.readBalance();
            WishList wl = reader.readWishlist();
            PurchaseList pl = reader.readPurchaseList();

            assertEquals(100.23, b.getCurrentBalance());
            assertEquals(2, b.getDeposits().size());
            assertEquals(500, b.getDeposits().get(0));
            assertEquals(400, b.getDeposits().get(1));
            assertEquals(2, wl.size());
            assertEquals(1, pl.size());

            checkWish(2345.67, "phone", Category.ELECTRONICS,
                    false, 0, wl.getWish(0));
            checkWish(10, "book", Category.OTHER,
                    true, 9, wl.getWish(1));
            checkPurchase(900, "TV", Category.ELECTRONICS, pl.getPurchase(0));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}