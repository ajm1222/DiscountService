package RetailStore.DiscountService.dto;

import lombok.Data;

@Data
public class Item {
    private final ItemType itemType;
    private final String name;
    private final double amount;
    public enum ItemType {
        GROCERY(false),
        BEVERAGE(true);
        public final boolean isDiscountApplicable;

        ItemType(boolean isDiscountApplicable) {
            this.isDiscountApplicable = isDiscountApplicable;
        }
    }
}
