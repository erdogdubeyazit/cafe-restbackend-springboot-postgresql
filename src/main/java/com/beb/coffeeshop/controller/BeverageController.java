package com.beb.coffeeshop.controller;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Beverage;
import com.beb.coffeeshop.presentation.payload.admin.BeveragePayload;
import com.beb.coffeeshop.presentation.result.admin.BeverageResult;
import com.beb.coffeeshop.presentation.result.common.ApiResult;
import com.beb.coffeeshop.presentation.result.common.Result;
import com.beb.coffeeshop.security.ApiUserDetails;
import com.beb.coffeeshop.security.CurrentUser;
import com.beb.coffeeshop.service.BeverageService;

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
 * Handles operations on beverages
 * 
 * @author Beyazit
 * @category Controller
 */
@RestController
@RequestMapping("/api/")
public class BeverageController {

    private static final Logger logger = LoggerFactory.getLogger(BeverageController.class);

    @Autowired
    private BeverageService beverageService;

    /**
     * Retrieves beverage list in both paged or unpages style
     * 
     * @param pageNo      page to view
     * @param pageSize    item count per page
     * @param sortColumn  natural ordering for specific column
     * @param currentUser authenticated user
     * @return list of beverages in shape of BeverageResult
     */
    @GetMapping(path = "beverages")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER" })
    public ResponseEntity<ApiResult> getAll(@RequestParam(name = "pageno", required = false) Integer pageNo,
            @RequestParam(name = "pagesize", required = false) Integer pageSize,
            @RequestParam(name = "sortcolumn", required = false) String sortColumn,
            @CurrentUser ApiUserDetails currentUser) {
        // check whether paging is requested
        if (pageNo != null && pageSize != null && sortColumn != null) {
            logger.debug(
                    "`BeverageManagementController.getAll()` with paging parameters : pageNo:%d, pageSize:%d, sortColumn:%s. [User info: %s ]",
                    pageNo, pageSize, sortColumn, currentUser.getUsername());

            Page<Beverage> beverages = beverageService.getAll(pageNo, pageSize, sortColumn);
            return BeverageResult.buildList(beverages);
        } else {
            logger.debug("`BeverageManagementController.getAll()` without paging parameters. [User info: %s ]",
                    currentUser.getUsername());

            List<Beverage> beverages = beverageService.getAll();
            return BeverageResult.buildList(beverages);
        }
    }

    /**
     * Retrieves beverage information
     * 
     * @param id          beverage to retrieve
     * @param currentUser authenticated user
     * @return BeverageResult
     */
    @GetMapping(path = "beverages/{id}")
    @RolesAllowed({ "ROLE_ADMIN", "ROLE_USER" })
    public ResponseEntity<ApiResult> getById(@PathVariable(name = "id", required = true) Long id,
            @CurrentUser ApiUserDetails currentUser) {

        // check wheter beverage exists
        Optional<Beverage> beverage = beverageService.findById(id);
        if (beverage.isPresent())
            return BeverageResult.build(beverage.get());
        else {
            logger.debug("Beverage with id: %d not found. [User info: %s ]", id, currentUser.getUsername());
            return Result.notFound();
        }
    }

    /**
     * Creates beverage
     * 
     * @param payload     BeveragePayload
     * @param currentUser authenticated user
     * @return Example:{id:1}
     */
    @PostMapping(path = "beverages")
    @RolesAllowed({ "ROLE_ADMIN" })
    public ResponseEntity<ApiResult> create(@RequestBody @Valid BeveragePayload payload,
            @CurrentUser ApiUserDetails currentUser) {
        try {
            Beverage beverage = beverageService.save(payload.getName(), payload.getPrice(), payload.getCurrency());
            // Just return id in simple format
            ApiResult result = ApiResult.blank().add("id", beverage.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (ServiceException e) {
            logger.error("Beverage create operation failed. {Details :%s}  [User info: %s ]", payload.toString(),
                    currentUser.getUsername());
            return Result.failure("Beverage can not be created");
        }

    }

    /**
     * Updates beverage
     * 
     * @param id          beverage to update
     * @param payload     BeveragePayload
     * @param currentUser authenticated user
     * @return BeverageResult
     */
    @PatchMapping(path = "/beverages/{id}")
    @RolesAllowed({ "ROLE_ADMIN" })
    public ResponseEntity<ApiResult> update(@PathVariable(name = "id", required = true) Long id,
            @RequestBody @Valid BeveragePayload payload, @CurrentUser ApiUserDetails currentUser) {

        try {
            Beverage beverage = beverageService.save(id, payload.getName(), payload.getPrice(), payload.getCurrency());
            return BeverageResult.build(beverage);
        } catch (Exception e) {
            logger.error("Beverage update operation failed. {Details :%s}  [User info: %s ]", payload.toString(),
                    currentUser.getUsername());
            return Result.failure("Beverage can not be updated");
        }
    }

    /**
     * 
     * @param id          beverage to delete
     * @param currentUser authenticated user
     * @return HttpStatus code
     */
    @DeleteMapping(path = "/beverages/{id}")
    @RolesAllowed({ "ROLE_ADMIN" })
    public ResponseEntity<ApiResult> delete(@PathVariable(name = "id", required = true) Long id,
            @CurrentUser ApiUserDetails currentUser) {
        try {
            beverageService.delete(id);
            return Result.ok();
        } catch (Exception e) {
            logger.error("Beverage delete operation failed. {Details id:%d}  [User info: %s ]", id,
                    currentUser.getUsername());
            return Result.failure("Beverage can not be deleted");
        }
    }

}
