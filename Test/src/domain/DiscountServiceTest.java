package domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DiscountServiceTest {

    @Test
    void shouldNotAllowPurchaseWithInsufficientBalance() {
        DiscountService service = new DiscountService();

        double original = 1000;
        double balance = 400;

        assertThrows(IllegalStateException.class, () -> {
            service.processPurchase("BLACK50", original, balance);
        });
    }
}