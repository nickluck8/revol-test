package com.test.main.config;

import com.test.main.handler.AccountHandler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class RouterConfig {
    private static final Logger logger = LoggerFactory.getLogger(RouterConfig.class);
    private Vertx vertx;
    private AccountHandler accountHandler;

    public RouterConfig(Vertx vertx, AccountHandler accountHandler) {
        this.vertx = vertx;
        this.accountHandler = accountHandler;
    }

    public Router getRouter() {
        logger.debug("Configuring routes");
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/accounts")
                .handler(accountHandler::processGet);

        router.post("/addAccount")
                .handler(accountHandler::processCreate);

        router.put("/updateAccount/:id")
                .handler(accountHandler::updateAccount);

        router.delete("/delete/:id")
                .handler(accountHandler::deleteAccount);

        router.route().handler(routingContext -> routingContext.response().setStatusCode(405).end());
        router.route().failureHandler(routingContext -> routingContext.response().setStatusCode(500).end(routingContext.getBodyAsString()));

        return router;
    }
}
