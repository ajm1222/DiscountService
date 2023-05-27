package RetailStore.DiscountService.dto;

import lombok.Data;

@Data
public class Discount {
    private final DiscountType discountType;
    private final double amountOfDiscount;

     public enum DiscountType {
        PERCENTAGE,
         VOLUME_DISCOUNT;
    }
}
