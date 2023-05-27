package RetailStore.DiscountService.controller;

import RetailStore.DiscountService.dto.CalculateBillRequest;
import RetailStore.DiscountService.dto.CalculateBillResponse;
import RetailStore.DiscountService.service.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final DiscountService discountService;
    private final Logger logger = LoggerFactory.getLogger(BillController.class);

    @Autowired
    public BillController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculateBillResponse> calculateNetPayable(@RequestBody CalculateBillRequest request) {
        try {
            return ResponseEntity.ok(discountService.calculateNetPayable(request));
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            logger.error("An error occurred while calculating net payable", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
