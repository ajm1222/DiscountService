package RetailStore.DiscountService.dto;


import RetailStore.DiscountService.repository.model.UserType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserInfo {
    private String name;
    private String id;
    private LocalDate registrationDate;
    private UserType userType;
}
