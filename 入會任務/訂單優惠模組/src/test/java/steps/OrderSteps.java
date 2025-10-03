package steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

import com.example.order.Order;
import com.example.order.OrderItem;
import com.example.order.OrderService;
import com.example.order.Product;

public class OrderSteps {
    
    private OrderService orderService;
    private Order result;
    
    @Given("no promotions are applied")
    public void no_promotions_are_applied() {
        orderService = new OrderService();
    }
    
    @When("a customer places an order with:")
    public void a_customer_places_an_order_with(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        List<OrderItem> items = new ArrayList<>();
        
        for (Map<String, String> row : rows) {
            String productName = row.get("productName");
            int quantity = Integer.parseInt(row.get("quantity"));
            double unitPrice = Double.parseDouble(row.get("unitPrice"));
            String category = row.get("category");
            
            Product product = new Product(productName, unitPrice, category != null ? category : "");
            OrderItem item = new OrderItem(product, quantity);
            items.add(item);
        }
        
        result = orderService.checkout(items);
    }
    
    @Then("the order summary should be:")
    public void the_order_summary_should_be(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> expectedSummary = rows.get(0);
        
        if (expectedSummary.containsKey("totalAmount")) {
            double expectedTotal = Double.parseDouble(expectedSummary.get("totalAmount"));
            assertEquals(expectedTotal, result.getTotalAmount(), "Total amount should match");
        }
        
        if (expectedSummary.containsKey("originalAmount")) {
            double expectedOriginal = Double.parseDouble(expectedSummary.get("originalAmount"));
            assertEquals(expectedOriginal, result.getOriginalAmount(), "Original amount should match");
        }
        
        if (expectedSummary.containsKey("discount")) {
            double expectedDiscount = Double.parseDouble(expectedSummary.get("discount"));
            assertEquals(expectedDiscount, result.getDiscount(), "Discount should match");
        }
    }
    
    @And("the customer should receive:")
    public void the_customer_should_receive(DataTable dataTable) {
        List<Map<String, String>> expectedItems = dataTable.asMaps(String.class, String.class);
        List<OrderItem> actualItems = result.getItems();
        
        assertEquals(expectedItems.size(), actualItems.size(), "Number of items should match");
        
        for (int i = 0; i < expectedItems.size(); i++) {
            Map<String, String> expected = expectedItems.get(i);
            OrderItem actual = actualItems.get(i);
            
            assertEquals(expected.get("productName"), actual.getProduct().getName(), "Product name should match");
            assertEquals(Integer.parseInt(expected.get("quantity")), actual.getQuantity(), "Quantity should match");
        }
    }
    
    @Given("the threshold discount promotion is configured:")
    public void the_threshold_discount_promotion_is_configured(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        Map<String, String> config = rows.get(0);
        
        double threshold = Double.parseDouble(config.get("threshold"));
        double discount = Double.parseDouble(config.get("discount"));
        
        // 如果還沒有OrderService，創建一個新的
        if (orderService == null) {
            orderService = new OrderService();
        }
        orderService.configureThresholdDiscount(threshold, discount);
    }
    
    @Given("the buy one get one promotion for cosmetics is active")
    public void the_buy_one_get_one_promotion_for_cosmetics_is_active() {
        // 如果還沒有OrderService，創建一個新的
        if (orderService == null) {
            orderService = new OrderService();
        }
        orderService.configureBuyOneGetOneForCosmetics();
    }
}