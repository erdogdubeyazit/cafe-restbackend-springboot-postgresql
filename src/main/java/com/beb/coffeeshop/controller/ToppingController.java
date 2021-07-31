package com.beb.coffeeshop.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Topping;
import com.beb.coffeeshop.presentation.payload.admin.ToppingPayload;
import com.beb.coffeeshop.presentation.result.admin.ToppingResult;
import com.beb.coffeeshop.presentation.result.common.ApiResult;
import com.beb.coffeeshop.presentation.result.common.Result;
import com.beb.coffeeshop.security.ApiUserDetails;
import com.beb.coffeeshop.security.CurrentUser;
import com.beb.coffeeshop.service.ToppingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles operations on toppings
 * 
 * @author Beyazit
 * @category Controller
 */
@RestController
@RequestMapping("/api/")
public class ToppingController {

    private static final Logger logger = LoggerFactory.getLogger(ToppingController.class);

    @Autowired
    private ToppingService toppingService;

    /**
     * Retrieves topping list in both paged or unpages style
     * 
     * @param pageNo      page to view
     * @param pageSize    item count per page
     * @param sortColumn  natural ordering for specific column
     * @param currentUser authenticated user
     * @return list of toppings in shape of ToppingResult
     */
    @GetMapping(path = "toppings")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER" })
    public ResponseEntity<ApiResult> getAll(@RequestParam(name = "pageno", required = false) Integer pageNo,
            @RequestParam(name = "pagesize", required = false) Integer pageSize,
            @RequestParam(name = "sortcolumn", required = false) String sortColumn,
            @CurrentUser ApiUserDetails currentUser) {

        if (pageNo != null && pageSize != null && sortColumn != null) {
            logger.debug(
                    "`ToppingManagementController.getAll()` with paging parameters : pageNo:%d, pageSize:%d, sortColumn:%s. [User info: %s ]",
                    pageNo, pageSize, sortColumn, currentUser.getUsername());

            Page<Topping> toppings = toppingService.getAll(pageNo, pageSize, sortColumn);
            return ToppingResult.buildList(toppings);
        } else {
            logger.debug("`ToppingManagementController.getAll()` without paging parameters. [User info: %s ]",
                    currentUser.getUsername());

            List<Topping> toppings = toppingService.getAll();
            return ToppingResult.buildList(toppings);
        }
    }

    /**
     * Retrieves topping information
     * 
     * @param id          topping to retrieve
     * @param currentUser authenticated user
     * @return ToppingResult
     */
    @GetMapping(path = "toppings/{id}")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER" })
    public ResponseEntity<ApiResult> getById(@PathVariable(name = "id", required = true) Long id,
            @CurrentUser ApiUserDetails currentUser) {

        Optional<Topping> topping = toppingService.findById(id);
        if (topping.isPresent())
            return ToppingResult.build(topping.get());
        else {
            logger.debug("Topping with id: %d not found. [User info: %s ]", id, currentUser.getUsername());
            return Result.notFound();
        }
    }

    /**
     * Creates topping
     * 
     * @param payload     ToppingPayload
     * @param currentUser authenticated user
     * @return Example:{id:1}
     */
    @PostMapping(path = "/toppings")
    @RolesAllowed({ "ROLE_ADMIN" })
    public ResponseEntity<ApiResult> create(@RequestBody @Valid ToppingPayload payload,
            @CurrentUser ApiUserDetails currentUser) {
        try {
            Topping topping = toppingService.save(payload.getName(), payload.getPrice(), payload.getCurrency());
            ApiResult result = ApiResult.blank().add("id", topping.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (ServiceException e) {
            logger.error("Topping create operation failed. {Details :%s}  [User info: %s ]", payload.toString(),
                    currentUser.getUsername());
            return Result.failure("Topping can not be created");
        }

    }

    /**
     * Updates topping
     * 
     * @param id          topping to update
     * @param payload     ToppingPayload
     * @param currentUser authenticated user
     * @return ToppingResult
     */
    @PatchMapping(path = "/toppings/{id}")
    @RolesAllowed({ "ROLE_ADMIN" })
    public ResponseEntity<ApiResult> update(@PathVariable(name = "id", required = true) Long id,
            @RequestBody @Valid ToppingPayload payload, @CurrentUser ApiUserDetails currentUser) {

        try {
            Topping topping = toppingService.save(id, payload.getName(), payload.getPrice(), payload.getCurrency());
            return ToppingResult.build(topping);
        } catch (Exception e) {
            logger.error("Topping update operation failed. {Details :%s}  [User info: %s ]", payload.toString(),
                    currentUser.getUsername());
            return Result.failure("Topping can not be updated");
        }
    }

    /**
     * 
     * @param id          topping to delete
     * @param currentUser authenticated user
     * @return HttpStatus code
     */
    @DeleteMapping(path = "/toppings/{id}")
    @RolesAllowed({ "ROLE_ADMIN" })
    public ResponseEntity<ApiResult> delete(@PathVariable(name = "id", required = true) Long id,
            @CurrentUser ApiUserDetails currentUser) {
        try {
            toppingService.delete(id);
            return Result.ok();
        } catch (Exception e) {
            logger.error("Topping delete operation failed. {Details id:%d}  [User info: %s ]", id,
                    currentUser.getUsername());
            return Result.failure("Topping can not be deleted");
        }
    }

}
