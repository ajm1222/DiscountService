package RetailStore.DiscountService.dto;

import lombok.Data;

@Data
public class BillItem {
    private final Item item;
    private final Integer quantity;

    public final Double fetchTotalAmount() {
        return this.getItem().getAmount()*quantity;
    }
}
