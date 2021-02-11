package org.example.framework.pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class StartPage extends BasePage{
    @FindBy(xpath = "//body//div[@class='menu-desktop']")
    WebElement mainMenu;

    public StartPage checkOpenStartPagePage() {
        wait.until(ExpectedConditions.visibilityOf(searchField));

        Assertions.assertTrue(mainMenu.isDisplayed());
        return this;
    }
}
