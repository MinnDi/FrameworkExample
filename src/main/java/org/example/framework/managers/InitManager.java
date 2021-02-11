package org.example.framework.managers;

import java.util.concurrent.TimeUnit;

import static org.example.framework.managers.DriverManager.getDriver;
import static org.example.framework.managers.DriverManager.quitDriver;
import static org.example.framework.utils.PropConst.*;

/**
 * @author Arkadiy_Alaverdyan
 * Класс для инициализации фреймворка
 */
public class InitManager {

    /**
     * Менеджер properties
     *
     * @see TestPropManager#getTestPropManager()
     */
    private static final TestPropManager props = TestPropManager.getTestPropManager();

    /**
     * Инициализация framework и запуск браузера со страницей приложения
     *
     * @see DriverManager#getDriver()
     * @see TestPropManager#getProperty(String)
     * @see org.example.framework.utils.PropConst
     */
    public static void initFramework() {
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Integer.parseInt(props.getProperty(IMPLICITLY_WAIT)), TimeUnit.SECONDS);
        getDriver().manage().timeouts().pageLoadTimeout(Integer.parseInt(props.getProperty(PAGE_LOAD_TIMEOUT)), TimeUnit.SECONDS);
    }

    /**
     * Завершения работы framework - гасит драйвер и закрывает сессию с браузером
     *
     * @see DriverManager#quitDriver()
     */
    public static void quitFramework() {
        quitDriver();
    }
}
