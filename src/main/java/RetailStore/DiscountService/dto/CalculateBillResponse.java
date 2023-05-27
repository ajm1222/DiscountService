package RetailStore.DiscountService.dto;

import lombok.Data;

import java.util.List;


@Data
public class CalculateBillResponse {
    private final Long billNumber;
    private final double initialAmount;
    private final List<Discount> discountList;
    private final double totalDiscount;
    private final double netPayable;
}
