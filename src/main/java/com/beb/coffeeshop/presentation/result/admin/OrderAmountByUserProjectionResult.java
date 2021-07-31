package com.beb.coffeeshop.presentation.result.admin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import com.beb.coffeeshop.model.projection.OrderAmountByUserProjection;
import com.beb.coffeeshop.presentation.result.common.ApiResult;
import com.beb.coffeeshop.presentation.result.common.Result;

import org.springframework.http.ResponseEntity;

/**
 * @category Statistics
 */
public class OrderAmountByUserProjectionResult {

    public static ResponseEntity<ApiResult> build(List<OrderAmountByUserProjection> data) {
        ApiResult result = ApiResult.blank();
        List<OrderAmountByUserData> stats = data.stream()
                .map(o -> new OrderAmountByUserData(o.getUserName(), o.getAmount())).collect(Collectors.toList());
        result.add("statisticsResult", stats);

        return Result.ok(result);
    }

    public static class OrderAmountByUserData {
        private String userName;
        private String amount;

        public OrderAmountByUserData(String userName, BigDecimal amount) {
            this.userName = userName;
            // BigDecimal output formatting
            this.amount = String.format("%.2f", amount.setScale(2, RoundingMode.HALF_UP));
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

    }

}
