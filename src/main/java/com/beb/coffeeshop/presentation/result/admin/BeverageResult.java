package com.beb.coffeeshop.presentation.result.admin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import com.beb.coffeeshop.model.Beverage;
import com.beb.coffeeshop.presentation.result.common.ApiResult;
import com.beb.coffeeshop.presentation.result.common.Result;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

/**
 * Data structure for beverage info. Holds paging details
 */
public class BeverageResult {

    public static ResponseEntity<ApiResult> buildList(Page<Beverage> pageable) {

        ApiResult result = ApiResult.blank();
        // Paging
        result.add("pageno", pageable.getPageable().getPageNumber());
        result.add("pagesize", pageable.getPageable().getPageSize());
        result.add("count", pageable.getNumberOfElements());
        result.add("totalpages", pageable.getTotalPages());
        result.add("totalelements", pageable.getTotalElements());

        List<Beverage> beverages = pageable.getContent();

        result = bindBeverageListToResult(result, beverages);

        return Result.ok(result);
    }

    public static ResponseEntity<ApiResult> buildList(List<Beverage> beverages) {
        ApiResult result = ApiResult.blank();
        result = bindBeverageListToResult(result, beverages);
        return Result.ok(result);
    }

    public static ResponseEntity<ApiResult> build(Beverage beverage) {
        ApiResult result = ApiResult.blank().add("beverage",
                new BeverageData(beverage.getId(), beverage.getName(), beverage.getPrice()));
        return Result.ok(result);
    }

    private static ApiResult bindBeverageListToResult(ApiResult result, List<Beverage> beverages) {
        List<BeverageData> beverageDataList = beverages.stream()
                .map(b -> new BeverageData(b.getId(), b.getName(), b.getPrice())).collect(Collectors.toList());

        return result.add("beverages", beverageDataList);
    }

    public static class BeverageData {
        private long id;
        private String name;
        private String price;

        public BeverageData(long id, String name, BigDecimal price) {
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
