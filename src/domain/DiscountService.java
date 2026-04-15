package domain;

public class DiscountService {

    private static final double DISCOUNT_RATE = 0.5;

    public double applyDiscount(String coupon, double originalAmount) {
        if (!isValidCoupon(coupon)) {
            return originalAmount;
        }
        return originalAmount * (1 - DISCOUNT_RATE);
    }

    public void validateTransaction(double finalAmount, double userBalance) {
        if (userBalance < finalAmount) {
            throw new IllegalStateException("Saldo insuficiente para concluir a compra.");
        }
    }

    public double processPurchase(String coupon, double originalAmount, double userBalance) {
        double discountedAmount = applyDiscount(coupon, originalAmount);
        validateTransaction(discountedAmount, userBalance);
        return discountedAmount;
    }

    private boolean isValidCoupon(String coupon) {
        return "BLACK50".equalsIgnoreCase(coupon);
    }
}