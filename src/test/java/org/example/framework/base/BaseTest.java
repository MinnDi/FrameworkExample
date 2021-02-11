package org.example.framework.base;

import org.example.framework.managers.InitManager;
import org.example.framework.managers.PageManager;
import org.example.framework.managers.TestPropManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.example.framework.managers.DriverManager.getDriver;
import static org.example.framework.utils.PropConst.APP_URL;

public class BaseTest {

    /**
     * Менеджер страничек
     * @see org.example.framework.managers.PageManager#getPageManager()
     */
    protected PageManager app = PageManager.getPageManager();

    @BeforeAll
    public static void beforeAll() {
        InitManager.initFramework();
    }

    @BeforeEach
    public void beforeEach() {
        getDriver().get(TestPropManager.getTestPropManager().getProperty(APP_URL));
    }

    @AfterAll
    public static void afterAll() {
        InitManager.quitFramework();
    }
}