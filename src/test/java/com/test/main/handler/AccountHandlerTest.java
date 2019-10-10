package com.test.main.handler;

import com.test.main.model.Account;
import com.test.main.service.Service;
import com.test.main.testUtil.TestUtils;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith( {MockitoExtension.class})
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountHandlerTest {

    private AccountHandler accountHandler;

    @Mock
    private RoutingContext routingContext;

    @Mock
    private HttpServerRequest request;

    @Mock
    private HttpServerResponse response;

    @Mock
    private Service<Account, String> service;

    private Account account;

    private Buffer body;

    @BeforeEach
    void setup() {
        account = TestUtils.getDummyAccount();
        body = TestUtils.getDummyBody();
        when(routingContext.request()).thenReturn(request);
        when(routingContext.response()).thenReturn(response);
        when(response.setStatusCode(anyInt())).thenReturn(response);
        accountHandler = new AccountHandler(service);
    }

    @Test
    @DisplayName("Delete: Test valid data")
    void testDeleteAccountSuccessful() {
        when(routingContext.request().getParam("id")).thenReturn("1");
        accountHandler.deleteAccount(routingContext);
        verify(service, times(1)).delete("1");
    }

    @Test
    @DisplayName("Delete: Test id is not present")
    void testDeleteIdIsNotPresent() {
        accountHandler.deleteAccount(routingContext);
        verify(service, times(0)).delete(any());
    }

    @Test
    @DisplayName("Delete: Test id is invalid")
    void testDeleteIdIsInvalid() {
        when(routingContext.request().getParam("id")).thenReturn("abc");
        accountHandler.deleteAccount(routingContext);
        verify(service, times(0)).delete(any());
    }


    @Test
    @DisplayName("Update: Test valid data")
    void testUpdateAccountSuccessful() {
        when(routingContext.getBody()).thenReturn(body);
        when(routingContext.request().getParam("id")).thenReturn("1");
        accountHandler.updateAccount(routingContext);
        verify(service, times(1)).update("1", account);
    }

    @Test
    @DisplayName("Update: Test id is not present")
    void testUpdateIdIsNotPresent() {
        when(routingContext.getBody()).thenReturn(body);
        accountHandler.updateAccount(routingContext);
        verify(service, times(0)).update(any(), eq(account));
    }

    @Test
    @DisplayName("Update: Test body is invalid")
    void testUpdateBodyIsInvalid() {
        accountHandler.updateAccount(routingContext);
        verify(service, times(0)).update(any(), any());
    }

    @Test
    @DisplayName("Update: Test id is invalid")
    void testUpdateIdIsInvalid() {
        when(routingContext.getBody()).thenReturn(body);
        when(routingContext.request().getParam("id")).thenReturn("abc");
        accountHandler.updateAccount(routingContext);
        verify(service, times(0)).update(any(), any());
    }

    @Test
    @DisplayName("Get: Test get is valid")
    void testGet() {
        List<Account> accountList = new ArrayList<>();
        accountList.add(account);
        when(service.get()).thenReturn(accountList);
        when(response.putHeader(anyString(), anyString())).thenReturn(response);
        accountHandler.processGet(routingContext);
        verify(service, times(1)).get();
    }

    @Test
    @DisplayName("Get: Test get is valid with empty body")
    void testGetBodyIsEmpty() {
        when(service.get()).thenReturn(Collections.emptyList());
        when(response.putHeader(anyString(), anyString())).thenReturn(response);
        accountHandler.processGet(routingContext);
        verify(service, times(1)).get();
    }

    @Test
    @DisplayName("Create: Test create is valid")
    void testCreateIsValid() {
        when(routingContext.getBody()).thenReturn(body);
        accountHandler.processCreate(routingContext);
        verify(service, times(1)).add(account);
    }

    @Test
    @DisplayName("Create: Test create is invalid with empty body")
    void testCreateDataIsInvalid() {
        accountHandler.processCreate(routingContext);
        verify(service, times(0)).get();
    }
}
