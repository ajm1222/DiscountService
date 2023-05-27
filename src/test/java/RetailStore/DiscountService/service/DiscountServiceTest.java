package RetailStore.DiscountService.service;

import RetailStore.DiscountService.dto.BillItem;
import RetailStore.DiscountService.dto.CalculateBillRequest;
import RetailStore.DiscountService.dto.CalculateBillResponse;
import RetailStore.DiscountService.dto.Item;
import RetailStore.DiscountService.repository.UserRepository;
import RetailStore.DiscountService.repository.model.User;
import RetailStore.DiscountService.repository.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DiscountServiceTest {

    private UserRepository mockRepository;
    private DiscountService service;
    private User testUser;
    private Item item;
    private List<BillItem> billItemList;
    private CalculateBillRequest request;

    @BeforeEach
    public void setUp() {
        mockRepository = Mockito.mock(UserRepository.class);
        service = new DiscountService(mockRepository);

        testUser = new User("1", "Test User", UserType.EMPLOYEE, Date.from(ZonedDateTime.now(ZoneOffset.UTC).minusYears(3).toInstant()));
        item = new Item(Item.ItemType.BEVERAGE, "thumbs-up", 50.0);
        billItemList = Arrays.asList(new BillItem(item, 100));
        request = new CalculateBillRequest(1l, "1", billItemList);
    }

    @Test
    public void calculateNetPayable_basic() {
        // Arrange
        given(mockRepository.getUserById("1")).willReturn(testUser);

        // Act
        CalculateBillResponse response = service.calculateNetPayable(request);

        // Assert
        assertEquals(1, response.getBillNumber());
        assertEquals(5000.0, response.getInitialAmount());
        assertEquals(1750.0, response.getTotalDiscount()); // EMPLOYEE has 30% discount
        assertEquals(3250.0, response.getNetPayable());
    }

    @Test
    public void calculateNetPayable_noUser() {
        // Arrange
        given(mockRepository.getUserById("1")).willReturn(null);

        // Act
        CalculateBillResponse response = service.calculateNetPayable(request);

        // Assert
        assertEquals(1, response.getBillNumber());
        assertEquals(5000.0, response.getInitialAmount());
        assertEquals(250.0, response.getTotalDiscount());
        assertEquals(4750.0, response.getNetPayable());
    }

    @Test
    public void calculateNetPayable_exceptionThrown() {
        // Arrange
        given(mockRepository.getUserById("1")).willThrow(new IllegalArgumentException("Invalid user id"));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.calculateNetPayable(request);
        });
        assertEquals("Invalid user id", exception.getMessage());
    }
}
