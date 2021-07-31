package com.beb.coffeeshop.service;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import com.beb.coffeeshop.exception.ServiceException;
import com.beb.coffeeshop.model.Beverage;
import com.beb.coffeeshop.model.Currency;
import com.beb.coffeeshop.repository.BeverageRepository;
import com.beb.coffeeshop.service.impl.BeverageServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
public class BeverageServiceTest {

    private BeverageService beverageService;

    @Mock
    private BeverageRepository beverageRepositoryMock;

    @Before
    public void setUp() {
        beverageService = new BeverageServiceImpl(beverageRepositoryMock);
    }

    @Test
    public void testGetAllWithoutPaging() {
        beverageService.getAll();
        verify(beverageRepositoryMock, times(1)).findAll();
    }

    @Test
    public void getAll_with_some_missing_paramters_shoul_fail() {
        assertThrows(Exception.class, () -> beverageService.getAll(null, 0, ""));
        assertThrows(Exception.class, () -> beverageService.getAll(0, null, ""));
        assertThrows(Exception.class, () -> beverageService.getAll(0, 0, null));
    }

    @Test
    public void save_with_missing_parameters_should_fail() {
        assertThrows(Exception.class, () -> beverageService.save(null, "name", BigDecimal.ZERO, "Currency.EUR"));
        assertThrows(Exception.class, () -> beverageService.save(null, null, BigDecimal.ZERO, "Currency.EUR"));
        assertThrows(Exception.class, () -> beverageService.save(null, null, null, "Currency.EUR"));
        assertThrows(Exception.class, () -> beverageService.save(null, null, null, null));
    }

    @Test(expected = ServiceException.class)
    public void save_with_incompatible_currency_should_fail() throws ServiceException {

        when(beverageRepositoryMock.findById(anyLong())).thenReturn(Optional.of(new Beverage()));

        try {
            beverageService.save(0l, "name", BigDecimal.ZERO, "-");
        } catch (ServiceException e) {
            assertEquals("Incompatible currentcy type", e.getMessage());
            throw e;
        }

        fail("Service Exception with the message `Incompatible currentcy type` was not thrown");
    }

    @Test
    public void testSave() {
        when(beverageRepositoryMock.findById(anyLong())).thenReturn(Optional.of(new Beverage()));
        when(beverageRepositoryMock.save(any())).thenReturn(new Beverage());
        try {
            beverageService.save(0l, "name", BigDecimal.ZERO, Currency.EUR.name());
            verify(beverageRepositoryMock, times(1)).save(any());
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void delete_with_missing_id_should_fail() {
        assertThrows(Exception.class, () -> beverageService.delete(null));

    }

    @Test()
    public void testDelete() throws ServiceException {
        beverageService.delete(-1l);
        verify(beverageRepositoryMock, times(1)).deleteById(anyLong());
    }
}
