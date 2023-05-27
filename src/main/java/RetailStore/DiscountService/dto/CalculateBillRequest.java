package RetailStore.DiscountService.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;


@Data
public class CalculateBillRequest {
    @NonNull
    private final Long billNumber;
    @NonNull
    private final String userId;
    @NonNull
    private final List<BillItem> billItemList;
}
