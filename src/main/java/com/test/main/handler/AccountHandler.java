package com.test.main.handler;

import com.test.main.config.RouterConfig;
import com.test.main.model.Account;
import com.test.main.service.Service;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class AccountHandler {
    private static final Logger logger = LoggerFactory.getLogger(RouterConfig.class);

    private Service<Account, String> service;

    public AccountHandler(Service service) {
        this.service = service;
    }

    public AccountHandler() {
    }

    public void deleteAccount(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");

        if (!isIdValid(id)) {
            sendBadRequestResponse(routingContext);
            return;
        }

        Account delete = service.delete(id);
        routingContext.response()
                .setStatusCode(204)
                .end(Json.encodePrettily(delete));
    }

    public void updateAccount(RoutingContext routingContext) {
        logger.debug("Put request received ");
        Pair<? extends Exception, Account> accountPair = decodeValue(routingContext, Account.class);
        if (accountPair.getLeft() != null) {
            sendBadRequestResponse(routingContext);
            return;
        }
        String id = routingContext.request().getParam("id");
        if (!isIdValid(id)) {
            sendBadRequestResponse(routingContext);
            return;
        }
        service.update(id, accountPair.getRight());

        routingContext.response().setStatusCode(204).end();
    }

    public void processCreate(RoutingContext routingContext) {
        logger.debug("Request received post request");
        Pair<? extends Exception, Account> newAccount = decodeValue(routingContext, Account.class);
        if (newAccount.getLeft() != null) {
            sendBadRequestResponse(routingContext);
            return;
        }
        Account created = service.add(newAccount.getRight());
        if (created == null) {
            sendBadRequestResponse(routingContext);
            return;
        }
        routingContext.response()
                .setStatusCode(201)
                .end(Json.encodePrettily(created));
    }

    public void processGet(RoutingContext routingContext) {
        List<Account> accounts = service.get();

        routingContext.response()
                .setStatusCode(200)
                .putHeader("Content-Type", "application/json; charset=UTF8")
                .end(accounts.isEmpty() ? "" : Json.encodePrettily(accounts));
    }

    private boolean isIdValid(String id) {
        return StringUtils.isNotEmpty(id) && StringUtils.isNumeric(id);
    }

    private void sendBadRequestResponse(RoutingContext routingContext) {
        routingContext.response()
                .setStatusCode(400)
                .end();
    }

    private <T> Pair<? extends Exception, T> decodeValue(RoutingContext routingContext, Class<T> t) {
        T decoded;
        try {
            decoded = Json.decodeValue(routingContext.getBody(), t);
        } catch (Exception e) {
            return Pair.of(e, null);
        }

        return Pair.of(null, decoded);
    }
}
