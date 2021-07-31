package com.beb.coffeeshop.presentation.result;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.beb.coffeeshop.model.Order;
import com.beb.coffeeshop.model.OrderItem;
import com.beb.coffeeshop.model.OrderStates;
import com.beb.coffeeshop.model.Topping;
import com.beb.coffeeshop.presentation.result.common.ApiResult;
import com.beb.coffeeshop.presentation.result.common.Result;

import org.springframework.http.ResponseEntity;

/**
 * Order info
 */
public class OrderResult {

    public static ResponseEntity<ApiResult> build(Order order) {
        ApiResult result = ApiResult.blank();

        result.add("orderNumber", order.getOrderNumber());
        result.add("state", order.getOrderState());

        // Total price without discount
        // Calculations are done outside of the anemic classes
        BigDecimal beverageTotal = order.getItems().stream().map(o -> o.getBeverage().getPrice())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        BigDecimal toppingTotal = order.getItems().stream().flatMap(o -> o.getToppings().stream())
                .map(t -> t.getPrice()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        BigDecimal totalPrice = beverageTotal.add(toppingTotal);

        List<BeverageData> beverages = order.getItems().stream().map(i -> new BeverageData(i))
                .sorted(Comparator.comparing(BeverageData::getName)).collect(Collectors.toList());
        result.add("beverages", beverages);

        // Price is rounded half up
        result.add("sumOfProductPrices", totalPrice.setScale(2, RoundingMode.HALF_UP));

        // Discount info
        if (order.getOrderState() != OrderStates.COMPLETED)
            result.add("discount",
                    "Order is not completed yet. Discount will be calculated after completing the order.");
        else if (order.getDiscount() == null)
            result.add("discount", "No discount is applicable.");
        else {
            Map<String, String> discount = new HashMap<>();
            discount.put("info", order.getDiscount().getDiscountInfo());
            // Discount is rounded down
            discount.put("price",
                    String.format("%.2f", order.getDiscount().getDiscountAmount().setScale(3, RoundingMode.DOWN)));

            result.add("discount", discount);
        }

        result.add("totalPrice", order.getOrderTotal() != null ? order.getOrderTotal().setScale(2, RoundingMode.HALF_UP)
                : "Order total will be calculated after completing the order.");

        return Result.ok(result);

    }

    public static class BeverageData {
        private long order_item_id;
        private String name;
        private String price;
        private List<ToppingData> toppings;

        public BeverageData(OrderItem orderItem) {
            order_item_id = orderItem.getId();
            name = orderItem.getBeverage().getName();
            // BigDecimal output formatting
            price = String.format("%.2f", orderItem.getBeverage().getPrice().setScale(2, RoundingMode.HALF_UP));
            toppings = orderItem.getToppings().stream().map(t -> new ToppingData(t))
                    .sorted(Comparator.comparing(ToppingData::getName)).collect(Collectors.toList());

        }

        public long getOrder_item_id() {
            return order_item_id;
        }

        public void setOrder_item_id(long order_item_id) {
            this.order_item_id = order_item_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public List<ToppingData> getToppings() {
            return toppings;
        }

        public void setToppings(List<ToppingData> toppings) {
            this.toppings = toppings;
        }

    }

    public static class ToppingData {
        private String name;
        private String price;

        public ToppingData(Topping topping) {
            name = topping.getName();
            // BigDecimal output formatting
            price = String.format("%.2f", topping.getPrice().setScale(2, RoundingMode.HALF_UP));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

    }
}
