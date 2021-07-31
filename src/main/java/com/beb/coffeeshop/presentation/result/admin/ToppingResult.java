package com.beb.coffeeshop.presentation.result.admin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import com.beb.coffeeshop.model.Topping;
import com.beb.coffeeshop.presentation.result.common.ApiResult;
import com.beb.coffeeshop.presentation.result.common.Result;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

/**
 * Data structure for topping info. Holds paging details
 */
public class ToppingResult {

    public static ResponseEntity<ApiResult> buildList(Page<Topping> pageable) {

        ApiResult result = ApiResult.blank();
        // Paging
        result.add("pageno", pageable.getPageable().getPageNumber());
        result.add("pagesize", pageable.getPageable().getPageSize());
        result.add("count", pageable.getNumberOfElements());
        result.add("totalpages", pageable.getTotalPages());
        result.add("totalelements", pageable.getTotalElements());

        List<Topping> toppings = pageable.getContent();
        result = bindToppingListToResult(result, toppings);

        return Result.ok(result);

    }

    public static ResponseEntity<ApiResult> buildList(List<Topping> toppings) {
        ApiResult result = ApiResult.blank();
        result = bindToppingListToResult(result, toppings);

        return Result.ok(result);

    }

    public static ResponseEntity<ApiResult> build(Topping topping) {
        ApiResult result = ApiResult.blank().add("topping",
                new ToppingData(topping.getId(), topping.getName(), topping.getPrice()));
        return Result.ok(result);

    }

    private static ApiResult bindToppingListToResult(ApiResult result, List<Topping> toppings) {
        List<ToppingData> beverageDataList = toppings.stream()
                .map(b -> new ToppingData(b.getId(), b.getName(), b.getPrice())).collect(Collectors.toList());

        return result.add("toppings", beverageDataList);
    }

    public static class ToppingData {
        private long id;
        private String name;
        private String price;

        public ToppingData(long id, String name, BigDecimal price) {
            this.id = id;
            this.name = name;
            // BigDecimal output formatting
            this.price = String.format("%.2f", price.setScale(2, RoundingMode.HALF_UP));
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
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
