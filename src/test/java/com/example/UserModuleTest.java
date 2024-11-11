package com.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

public class UserModuleTest {
    @Test
    public void testUserModuleBindings() {
        Injector injector = Guice.createInjector(new UserModule());

        // Verify that Guice provides the expected dependencies
        UserResource userResource = injector.getInstance(UserResource.class);
        assertNotNull(userResource);

        UserDao userDao = injector.getInstance(UserDao.class);
        assertNotNull(userDao);
    }
}
