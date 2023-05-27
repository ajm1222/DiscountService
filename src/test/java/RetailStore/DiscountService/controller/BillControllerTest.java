package RetailStore.DiscountService.controller;

import RetailStore.DiscountService.controller.BillController;
import RetailStore.DiscountService.dto.BillItem;
import RetailStore.DiscountService.dto.CalculateBillRequest;
import RetailStore.DiscountService.dto.CalculateBillResponse;
import RetailStore.DiscountService.dto.Item;
import RetailStore.DiscountService.service.DiscountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class BillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DiscountService discountService;

    @Autowired
    private ObjectMapper objectMapper;

    private Item item;
    private List<BillItem> billItemList;
    private CalculateBillRequest request;
    private CalculateBillResponse response;

    @BeforeEach
    public void setUp() {
        item = new Item(Item.ItemType.GROCERY,"thumbs-up",50.0);
        billItemList = Arrays.asList(new BillItem(item, 100));
        request = new CalculateBillRequest(1l, "1", billItemList);
        response = new CalculateBillResponse(1l, 100.0, new ArrayList<>(), 30.0, 70.0);
    }

    @Test
    public void calculateNetPayable_OK() throws Exception {
        // Arrange
        Mockito.when(discountService.calculateNetPayable(Mockito.any(CalculateBillRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/bills/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.billNumber").value(1))
                .andExpect(jsonPath("$.initialAmount").value(100.0))
                .andExpect(jsonPath("$.totalDiscount").value(30.0))
                .andExpect(jsonPath("$.netPayable").value(70.0));
    }

    @Test
    public void calculateNetPayable_BadRequest() throws Exception {
        // Arrange
        Mockito.when(discountService.calculateNetPayable(Mockito.any(CalculateBillRequest.class))).thenThrow(new IllegalArgumentException("Invalid request"));

        // Act & Assert
        mockMvc.perform(post("/api/bills/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void calculateNetPayable_InternalServerError() throws Exception {
        // Arrange
        Mockito.when(discountService.calculateNetPayable(Mockito.any(CalculateBillRequest.class))).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        mockMvc.perform(post("/api/bills/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}
