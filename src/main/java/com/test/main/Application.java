package com.test.main;

import com.test.main.config.PropertiesConfig;
import com.test.main.config.RouterConfig;
import com.test.main.handler.AccountHandler;
import com.test.main.repository.AccountRepository;
import com.test.main.service.AccountService;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;

import java.util.Properties;

public class Application {
    private static Logger logger = LoggerFactory.getLogger(Application.class);
    private static Vertx vertx = Vertx.vertx();
    private static Properties properties = PropertiesConfig.getProperties();

    public static void main(String[] args) {
        AccountHandler accountHandler = new AccountHandler(new AccountService(new AccountRepository()));
        int port = Integer.parseInt(properties.getProperty("server.port", "8080"));
        logger.debug("Application starting on port {}", port);
        Router router = new RouterConfig(vertx, accountHandler).getRouter();
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(port);
    }

}
