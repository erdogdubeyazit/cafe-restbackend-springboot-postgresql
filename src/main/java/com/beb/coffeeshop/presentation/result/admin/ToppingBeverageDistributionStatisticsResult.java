package com.beb.coffeeshop.presentation.result.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.beb.coffeeshop.model.projection.ToppingBeverageDistributionProjection;
import com.beb.coffeeshop.presentation.result.common.ApiResult;
import com.beb.coffeeshop.presentation.result.common.Result;

import org.springframework.http.ResponseEntity;

/**
 * @category Statistics
 */
public class ToppingBeverageDistributionStatisticsResult {

    public static ResponseEntity<ApiResult> build(List<ToppingBeverageDistributionProjection> data) {
        Map<String, List<ToppingBeverageDistributionProjection>> groupedData = data.stream()
                .collect(Collectors.groupingBy(ToppingBeverageDistributionProjection::getToppingName));
        List<ToppingData> stats = groupedData.entrySet().stream().map(i -> new ToppingData(i.getKey(), i.getValue()))
                .collect(Collectors.toList());

        ApiResult result = ApiResult.blank();
        result.add("statisticsResult", stats);

        return Result.ok(result);
    }

    public static class ToppingData {
        private String toppingName;
        private Long totalCount;
        private List<BeverageData> beverages = new ArrayList<>();

        public ToppingData(String toppingName, List<ToppingBeverageDistributionProjection> beverages) {
            this.toppingName = toppingName;
            this.beverages = beverages.stream().map(b -> new BeverageData(b.getBeverageName(), b.getItemCount()))
                    .collect(Collectors.toList());
            this.totalCount = this.beverages.stream().map(b -> b.getCount()).reduce(0L, (a, b) -> a + b);
        }

        public String getToppingName() {
            return toppingName;
        }

        public void setToppingName(String toppingName) {
            this.toppingName = toppingName;
        }

        public Long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Long totalCount) {
            this.totalCount = totalCount;
        }

        public List<BeverageData> getBeverages() {
            return beverages;
        }

        public void setBeverages(List<BeverageData> beverages) {
            this.beverages = beverages;
        }
    }

    public static class BeverageData {
        private String beverageName;
        private Long count;

        public BeverageData(String beverageName, Long count) {
            this.beverageName = beverageName;
            this.count = count;
        }

        public String getBeverageName() {
            return beverageName;
        }

        public void setBeverageName(String beverageName) {
            this.beverageName = beverageName;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }

    }
}
