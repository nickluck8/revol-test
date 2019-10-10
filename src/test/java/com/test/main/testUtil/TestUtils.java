package com.test.main.testUtil;

import com.test.main.model.Account;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;

public class TestUtils {
    public static Account getDummyAccount() {
        Account account = new Account();
        account.setId(1L);
        account.setAmount(35D);
        return account;
    }

    public static Buffer getDummyBody() {
        return Json.encodeToBuffer(getDummyAccount());
    }
}
