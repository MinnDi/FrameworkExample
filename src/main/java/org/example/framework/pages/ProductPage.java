package org.example.framework.pages;

import org.example.framework.utils.WarrantyOptions;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ProductPage extends BasePage {

    /**
     * Доп. гарантия для продукта
     */
    @FindBy(xpath = "//select[@class='ui-input-select product-warranty__select']")
    WebElement warrantyOption;

    /**
     * Цена продукта
     */
    @FindBy(xpath = "//span[contains(@class, 'product-card-price__current')]")
    WebElement productPriceField;

    /**
     * Купить продукт
     */
    @FindBy(xpath = "//button[contains(.,'Купить')]")
    WebElement buyProductButton;

//    @FindBy(xpath = "")
//    WebElement productName;

    /**
     * Проверка открытия страницы, путём проверки title страницы
     *
     * @return ProductSearchCatalogPage - остаемся на этой странице
     */
    public ProductPage checkOpenProductPage(String productFullName) {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));

        Assertions.assertTrue(pageTitle.getText().toLowerCase().contains(productFullName.toLowerCase()));
        return this;
    }

    /**
     * Выбор дополнительной гарантии
     *
     * @param warranty - параметр доступных опций гарантии {@link WarrantyOptions}
     * @return остаемся на странице
     */
    public ProductPage selectWarrantyForYears(WarrantyOptions warranty) {
        String warrantyXPath = ".//option[@value='%d']";
        WebElement warrantyOptionSelector;
        switch (warranty) {
            case ONE_YEAR:
                warrantyOptionSelector = warrantyOption.findElement(By.xpath(String.format(warrantyXPath, 0)));
                break;
            case TWO_YEARS:
                warrantyOptionSelector = warrantyOption.findElement(By.xpath(String.format(warrantyXPath, 1)));
                break;
            case NOT_SELECTED:
                warrantyOptionSelector = warrantyOption.findElement(By.xpath(".//option[@value='default']"));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + warranty);
        }
        warrantyOption.click();
        wait.until(ExpectedConditions.elementToBeClickable(warrantyOptionSelector));
        warrantyOptionSelector.click();
        if (warranty.equals(WarrantyOptions.NOT_SELECTED))
            wait.until(ExpectedConditions
                    .textToBe(By.xpath("//div[@class='product-card-price__benefit-wrap']/span[contains(@class,'product-card-price__discount')]")
                            , ""));
        else wait.until(ExpectedConditions
                .textToBe(By.xpath("//div[@class='product-card-price__benefit-wrap']/span[contains(@class,'product-card-price__discount')]")
                        , "Цена изменена"));
        return this;
    }

    /**
     * Метод вывода цены продукта
     *
     * @return возвращает цену продукта
     */
    public int productPrice() {
        return Integer.parseInt(productPriceField.getText().trim().replaceAll("\\D", ""));
    }

    /**
     * Метод для добавления продукта в корзину
     *
     * @return остаемся на странице
     */
    public ProductPage buyProduct() {
        Integer cartSumBeforeAddition = getCartPrice();
        Integer cartAmountBeforeAddition = Integer.parseInt(cartAmount.getAttribute("textContent").replaceAll("\\D", ""));

        wait.until(ExpectedConditions.elementToBeClickable(buyProductButton));
        scrollWithOffset(buyProductButton, 0, -200);
        buyProductButton.click();
        //wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(cartSum, cartSumBeforeAddition.toString())));
        //wait.until(ExpectedConditions.visibilityOf(cartSum));
        //wait.until(ExpectedConditions.attributeToBe(By.xpath("//a[@class='ui-link cart-link']//span[contains(@class,'cart-link__badge')]"),"textContent", ""+(cartAmountBeforeAddition+1)));
        try {
            Thread.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Integer cartSumAfterAddition = getCartPrice();

        // Проверка, что в корзине сумма изменилась именно на ту цену, по которой выбран товар
        assertThat(cartSumAfterAddition, is(cartSumBeforeAddition + Integer.parseInt(productPriceField.getText().replaceAll("\\D", ""))));
        return this;
    }

}
