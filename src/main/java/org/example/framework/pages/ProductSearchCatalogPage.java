package org.example.framework.pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ProductSearchCatalogPage extends BasePage {

    /**
     * Найденные продукты по запросу
     */
    @FindBy(xpath = "//div[@class='n-catalog-product__main']")
    private List<WebElement> productsFound;

    /**
     * Проверка открытия страницы, путём проверки title страницы
     *
     * @return ProductSearchCatalogPage - остаемся на этой странице
     */
    public ProductSearchCatalogPage checkOpenProductSearchCatalogPage(String productName) {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        if (pageTitle.getText().equals("Результаты поиска"))
            Assertions.fail("Ничего не найдено по запросу: " + productName);
        assertThat(pageTitle.getText(), is("Найдено:"));
        return this;
    }

    /**
     * Поиск продукта по имени в результате поиска по ключевому слову
     *
     * @param productName - имя продукта
     * @return ProductPage - при успешном поиске переходит на страницу продукта
     */
    public ProductPage findNeededProductByName(String productName) {
        String productFullName = "";
        wait.until(ExpectedConditions.visibilityOf(productsFound.get(0)));
        for (WebElement product : productsFound) {
            WebElement title = product.findElement(By.xpath(".//a[@class='ui-link']"));
            if (title.getText().trim().toLowerCase().contains(productName)) {
                productFullName = title.getText();
                //scrollToElementJs(title);
                scrollWithOffset(title, 0, -200);
                elementToBeClickable(title);
                title.click();
                return app.getProductPage();
            }
        }
        Assertions.fail("Продукт с именем" + productName + "' не был найден в поиске!");
        return app.getProductPage().checkOpenProductPage(productFullName);
    }
}
