package org.example.framework.pages;

import org.example.framework.utils.WarrantyOptions;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.example.framework.managers.DriverManager.getDriver;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CartPage extends BasePage {
    /**
     * Список продуктов в корзине
     */
    @FindBy(xpath = "//div[@class='cart-items__product']")
    List<WebElement> cartProducts;

    /**
     * Ярлык корзины
     */
    @FindBy(xpath = "//div[@class = 'total-amount__label']")
    WebElement cartTotal;

    /**
     * Сумма товаров в корзине
     */
    @FindBy(xpath = "//div[@class = 'total-amount__label']//div[contains(@class,'price__block')]/span[@class='price__current']")
    WebElement cartMainSumField;

    /**
     * Ярлык с количеством товаров в корзине
     */
    @FindBy(xpath = "//div[@class = 'total-amount__label']//div[@class='total-amount__count']")
    WebElement cartAmountField;

    /**
     * Метод, возвращающий тип гарантии
     *
     * @param productName - имя продукта
     * @return возвращает тип гарантии {@link WarrantyOptions}
     */
    public WarrantyOptions whatTypeOfWarrantyChecked(String productName) {
        WebElement product = findProduct(productName);
        if (product == null) Assertions.fail(productName + " нет в корзине");

        List<WebElement> warrantyOptions = product.findElements(By.xpath(".//div[contains(@class, 'additional-warranties-row__warranty')]"));
        Assertions.assertFalse(warrantyOptions.isEmpty(), "Для данного товара недоступна опция выбора гарантии");

        WebElement warrantySelected = product.findElement(By.xpath(".//div[contains(@class, 'additional-warranties-row__warranty')]//span[contains(@class, 'base-ui-radio-button__icon_checked')]"));
        String warranty = warrantySelected.getText().replaceAll("\\D", "");
        switch (warranty) {
            case ("12"):
                return WarrantyOptions.ONE_YEAR;
            case ("24"):
                return WarrantyOptions.TWO_YEARS;
            case (""):
                return WarrantyOptions.NOT_SELECTED;
            default:
                throw new IllegalStateException("Unexpected value: " + warranty);
        }
    }

    /**
     * МетодБ возвращающий цену товара в корзине
     *
     * @param productName - имя продукта
     * @return Integer - цена товара
     */
    public Integer productPrice(String productName) {
        WebElement price = findProduct(productName);
        if (price == null) Assertions.fail(productName + " нет в корзине!");
        price.findElement(By.xpath(".//span[@class = 'price__current']"));
        return findProductPrice(price);
    }

    /**
     * Метод, возвращающий сумму товаров в корзине
     *
     * @return Integer - сумма товаров в корзине
     */
    public Integer cartSum() {
        String sum = cartMainSumField.getAttribute("textContent").replaceAll("\\D", "");
        return Integer.parseInt(sum);
    }

//    public CartPage checkSumIsCorrect() {
//        Integer sum = 0;
//        for (WebElement p :
//                cartProducts) {
//            sum += findProductPrice(p);
//        }
//        assertThat(sum, is(cartSum()));
//        return this;
//    }

    public CartPage checkAmountIsCorrect() {
        String cartAmount = cartAmountField.getText().trim();
        Integer amount = 0;
        for (WebElement p :
                cartProducts) {
            amount += Integer.parseInt(p.findElement(By.xpath(".//input")).getAttribute("value").replaceAll(" ", ""));
        }
        cartAmount = cartAmount.substring(cartAmount.indexOf(':'), cartAmount.lastIndexOf('т')).replaceAll("\\D", "");
        assertThat(amount, is(Integer.parseInt(cartAmount)));
        return this;
    }

    public CartPage deleteProduct(String productName) {
        WebElement product = findProduct(productName);
        if (product == null) Assertions.fail(productName + " нет в корзине!");
        Integer cartAmountBeforeDeletion = Integer.parseInt(cartAmount.getText().replaceAll("\\D", ""));
        WebElement deleteButton = product.findElement(By.xpath(".//button[@class = 'menu-control-button' and contains (.,'Удалить')]"));
        scrollWithOffset(deleteButton, 0, -800);
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        deleteButton.click();
        wait.until(ExpectedConditions.numberOfElementsToBeLessThan(By.xpath("//div[@class='cart-items__product']"), cartAmountBeforeDeletion));
        return this;
    }

    /**
     * Метод, возвращающий количество товара в корзине
     *
     * @param productName - имя продукта
     * @return Integer - количество данного продукта в корзине
     */
    public Integer productAmount(String productName) {
        Integer amount = 0;
        WebElement product = findProduct(productName);
        amount = Integer.parseInt(product.findElement(By.xpath(".//input")).getAttribute("value").replaceAll(" ", ""));
        return amount;
    }

    /**
     * Есть ли продукт в корзине
     *
     * @param productName - имя продукта
     * @return boolean - true, если продукт присутствует, false, если нет
     */
    public boolean productIsPresent(String productName) {
        for (WebElement product :
                cartProducts) {
            product = getProductName(product);
            if (product.getText().contains(productName)) return true;
        }
        return false;
    }

    /**
     * Увеличить количество товара в корзине
     *
     * @param productName - имя продукта
     * @return CartPage - остаемся на странице корзины
     */
    public CartPage increaseProductAmount(String productName) {
        WebElement productAmountIncreaseButton = findProduct(productName);
        if (productAmountIncreaseButton == null) Assertions.fail(productName + "нет в корзине!");
        productAmountIncreaseButton.findElement(By.xpath(".//button[@data-commerce-action='CART_ADD']"));
        wait.until(ExpectedConditions.elementToBeClickable(productAmountIncreaseButton));
        scrollToElementJs(productAmountIncreaseButton);
        scrollWithOffset(productAmountIncreaseButton, 0, -200);
        productAmountIncreaseButton.click();
        wait.until(ExpectedConditions.elementToBeClickable(productAmountIncreaseButton));
        return this;
    }

    /**
     * Вернуть последний удаленный продукт
     *
     * @return CartPage - остаемся на странице
     */
    public CartPage restoreLastRemoved() {
        WebElement restoreButton = getDriver().findElement(By.xpath("//div[@class='group-tabs']//span[@class = 'restore-last-removed']"));
        wait.until(ExpectedConditions.elementToBeClickable(restoreButton));
        scrollToElementJs(restoreButton);
        restoreButton.click();
        return this;
    }

    /**
     * Внутренний метод для поиска цены продукта в корзине по имени
     *
     * @param product - имя продукта
     * @return Integer - цена продукта
     */
    private Integer findProductPrice(WebElement product) {
        product = product.findElement(By.xpath(".//span[@class='price__current']"));
        return Integer.parseInt(product.getText().replaceAll("\\D", ""));
    }

    /**
     * Внутренний метод для поиска продукта по имени
     *
     * @param s - имя продукта
     * @return WebElement - возвращает элемент найденного продукта, null - если продукт не найден
     */
    private WebElement findProduct(String s) {
        for (WebElement product :
                cartProducts) {
            String str = product.findElement(By.xpath(".//div[@class='cart-items__product-name']//a")).getAttribute("text").trim();
            if (str.toLowerCase().contains(s.toLowerCase().trim()))
                return product;
        }
        Assertions.fail(s + " нет в корзине!");
        return null;
    }

    /**
     * Внутренний метод, возвращает элемент имени продукта по элементу продукта
     *
     * @param product - элемент продукта
     * @return WebElement - элемент имени продукта
     */
    private WebElement getProductName(WebElement product) {
        return product.findElement(By.xpath(".//div[@class = 'cart-items__product-info']//a[@class = 'cart-items__product-name-link']"));
    }

}
