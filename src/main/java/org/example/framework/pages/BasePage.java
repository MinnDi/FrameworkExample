package org.example.framework.pages;

import org.example.framework.managers.PageManager;
import org.example.framework.managers.TestPropManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.example.framework.managers.DriverManager.getDriver;
import static org.example.framework.utils.PropConst.IMPLICITLY_WAIT;

/**
 * @author Arkadiy_Alaverdyan
 * Базовый класс всех страничек
 */
public class BasePage {

    /**
     * Поле поиска  {@link FindBy}
     */
    @FindBy(xpath = "//nav[@id='header-search']//input[@type='search']")
    WebElement searchField;

    /**
     * Заголовок страницы {@link FindBy}
     */
    @FindBy(xpath = "//h1[not(@jstcache)]")
    WebElement pageTitle;

    /**
     * Ссылка на корзину
     */
    @FindBy(xpath = "//a[@class='ui-link cart-link']")
    WebElement cartLink;

    /**
     * Сумма корзины
     */
    @FindBy(xpath = "//a[@class='ui-link cart-link']//span[@class='cart-link__price']")
    WebElement cartSumField;

    /**
     * Количество продуктов, добавленных в корзину
     */
    @FindBy(xpath = "//a[@class='ui-link cart-link']//span[contains(@class,'cart-link__badge')]")
    WebElement cartAmount;

    /**
     * Менеджер страничек
     *
     * @see PageManager
     */
    protected PageManager app = PageManager.getPageManager();


    /**
     * Объект для выполнения любого js кода
     *
     * @see JavascriptExecutor
     */
    protected JavascriptExecutor js = (JavascriptExecutor) getDriver();


    /**
     * Объект явного ожидания
     * При применении будет ожидать заданного состояния 10 секунд с интервалом в 1 секунду
     *
     * @see WebDriverWait
     */
    protected WebDriverWait wait = new WebDriverWait(getDriver(), 10, 1000);


    /**
     * Менеджер properties
     *
     * @see TestPropManager#getTestPropManager()
     */
    private final TestPropManager props = TestPropManager.getTestPropManager();


    /**
     * Конструктор позволяющий инициализировать все странички и их элементы помеченные аннотацией {@link FindBy}
     * Подробнее можно просмотреть в класс {@link org.openqa.selenium.support.PageFactory}
     *
     * @see FindBy
     * @see PageFactory
     * @see PageFactory#initElements(WebDriver, Object)
     */
    public BasePage() {
        PageFactory.initElements(getDriver(), this);
    }


    /**
     * Функция позволяющая производить scroll до любого элемента с помощью js
     *
     * @param element - веб-элемент странички
     * @see JavascriptExecutor
     */
    protected void scrollToElementJs(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }


    /**
     * Функция позволяющая производить scroll до любого элемента с помощью js со смещение
     * Смещение задается количеством пикселей по вертикали и горизонтали, т.е. смещение до точки (x, y)
     *
     * @param element - веб-элемент странички
     * @param x       - параметр координаты по горизонтали
     * @param y       - параметр координаты по вертикали
     * @see JavascriptExecutor
     */
    public void scrollWithOffset(WebElement element, int x, int y) {
        String code = "window.scroll(" + (element.getLocation().x + x) + ","
                + (element.getLocation().y + y) + ");";
        ((JavascriptExecutor) getDriver()).executeScript(code, element, x, y);
    }


    /**
     * Явное ожидание состояния clickable элемента
     *
     * @param element - веб-элемент который требует проверки clickable
     * @return WebElement - возвращаем тот же веб элемент что был передан в функцию
     * @see WebDriverWait
     * @see org.openqa.selenium.support.ui.FluentWait
     * @see org.openqa.selenium.support.ui.Wait
     * @see ExpectedConditions
     */
    protected WebElement elementToBeClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }


    /**
     * Общий метод по заполнения полей ввода
     *
     * @param field - веб-элемент поле ввода
     * @param value - значение вводимое в поле
     */
    protected void fillInputField(WebElement field, String value) {
        scrollToElementJs(field);
        elementToBeClickable(field).click();
        field.sendKeys(value);
    }


    /**
     * Общий метод по заполнению полей с датой
     *
     * @param field - веб-элемент поле с датой
     * @param value - значение вводимое в поле с датой
     */
    protected void fillDateField(WebElement field, String value) {
        scrollToElementJs(field);
        field.sendKeys(value);
    }


    /**
     * @param by - Объект задающий локатор поиска {@link By}
     * @return boolean - true если элемент присутствует, false если элемент отсутствует
     */
    public boolean isElementExist(By by) {
        boolean flag = false;
        try {
            getDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            getDriver().findElement(by);
            flag = true;
        } catch (NoSuchElementException ignore) {
        } finally {
            getDriver().manage().timeouts().implicitlyWait(Integer.parseInt(props.getProperty(IMPLICITLY_WAIT)), TimeUnit.SECONDS);
        }
        return flag;
    }

    /**
     * Метод для поиска продукта для дальнейшего выбора из найденных вариантов
     *
     * @param productName - краткое название продукта для поиска
     * @return ProductSearchCatalogPage - переходит на страницу выбора продукта в найденных результатах {@link ProductSearchCatalogPage}
     */
    public ProductSearchCatalogPage searchForProductByName(String productName){
        fillInputField(searchField,productName+ Keys.ENTER);
        return app.getProductSearchCatalogPage().checkOpenProductSearchCatalogPage(productName);
    }

    /**
     * Метод для поиска продукта с переходом на страницу продукта
     *
     * @param productName - краткое название продукта для поиска
     * @return ProductPage - переходит на страницу выбора продукта в найденных результатах {@link ProductPage}
     */
    public ProductPage searchForProduct(String productName){
        fillInputField(searchField,productName+ Keys.ENTER);
        return app.getProductPage().checkOpenProductPage(productName);
    }

    /**
     * Метод суммы корзины
     * @return Integer - сумма всех продуктов в корзине
     */
    public Integer getCartPrice(){
        if (cartAmount.getAttribute("textContent").replaceAll("\\D", "").equals("0")){
            return 0;
        }
        else {
            return Integer.parseInt(cartSumField.getText().replaceAll("\\D", ""));
        }
    }

    public CartPage goToCartPage(){
        wait.until(ExpectedConditions.elementToBeClickable(cartLink));
        cartLink.click();
        return app.getCartPage();
    }
}
