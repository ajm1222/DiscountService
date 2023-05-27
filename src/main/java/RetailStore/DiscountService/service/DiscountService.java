package RetailStore.DiscountService.service;

import RetailStore.DiscountService.dto.BillItem;
import RetailStore.DiscountService.dto.CalculateBillRequest;
import RetailStore.DiscountService.dto.CalculateBillResponse;
import RetailStore.DiscountService.dto.Discount;
import RetailStore.DiscountService.repository.UserRepository;
import RetailStore.DiscountService.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DiscountService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    @Autowired
    public DiscountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CalculateBillResponse calculateNetPayable(CalculateBillRequest request) {
        try {
            final Discount userDiscount = Optional.ofNullable(userRepository.getUserById(request.getUserId()))
                    .map(User::getDiscountPercentage)
                    .map(percentage -> getDiscountForUser(percentage, request.getBillItemList()))
                    .orElse(null);
            final Double initialAmount = getTotalInitialAmount(request.getBillItemList());
            final Discount amountDiscount =  getTotalAmountDiscount(initialAmount);

            final List<Discount> discounts = Stream.of(userDiscount, amountDiscount)
                    .filter(discount -> discount != null && discount.getAmountOfDiscount()!=0)
                    .collect(Collectors.toList());
            final Double netPayable = getNetPayable(initialAmount, discounts);
            return new CalculateBillResponse(
                    request.getBillNumber(),
                    initialAmount,
                    discounts,
                    initialAmount - netPayable,
                    netPayable
            );

        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("An error occurred while calculating net payable", e);
            throw e;
        }
    }

    private Double getTotalInitialAmount(List<BillItem> items) {
        return items.stream()
                .mapToDouble(BillItem::fetchTotalAmount)
                .sum();
    }

    private Discount getDiscountForUser(Integer percentage, List<BillItem> items) {
        return new Discount(Discount.DiscountType.PERCENTAGE,
                (items.stream()
                .filter(billItem -> billItem.getItem().getItemType().isDiscountApplicable)
                .mapToDouble(BillItem::fetchTotalAmount)
                .sum())*percentage/100);
    }

    private Discount getTotalAmountDiscount(Double initialAmount) {
        return new Discount(Discount.DiscountType.VOLUME_DISCOUNT, Math.floor(initialAmount / 100) * 5);
    }

    private Double getNetPayable(Double initalAmount, List<Discount> discounts) {
        return initalAmount - discounts.stream()
                .mapToDouble( Discount::getAmountOfDiscount)
                .sum();

    }
}

