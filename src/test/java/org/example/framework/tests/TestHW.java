package org.example.framework.tests;

import org.example.framework.base.BaseTest;
import org.example.framework.utils.WarrantyOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestHW extends BaseTest {

    @Test
    public void HWTest() {

        String playstation = "playstation";
        String playstationFullName = "playstation 4 slim black";

        int playstationPriceBeforeSelectedOptions = app.getStartPage()
                .searchForProductByName(playstation)
                .findNeededProductByName(playstationFullName)
                .productPrice();

        int playstationPriceAfterSelectedOptions = app.getProductPage()
                .selectWarrantyForYears(WarrantyOptions.TWO_YEARS)
                .productPrice();

        String game = "Detroit";
        String gameFullName = "Игра Detroit: Стать человеком (PS4)";

        int gamePrice = app.getProductPage()
                .buyProduct()
                .searchForProduct(game)
                .checkOpenProductPage(gameFullName)
                .productPrice();

        int cartSumAfterGameAddition = app.getProductPage()
                .buyProduct()
                .getCartPrice();

        assertThat(cartSumAfterGameAddition, is(playstationPriceAfterSelectedOptions + gamePrice));

        WarrantyOptions warranty = app.getCartPage()
                .goToCartPage()
                .whatTypeOfWarrantyChecked(playstationFullName);

        assertThat(warranty, is(WarrantyOptions.TWO_YEARS));

        assertThat(playstationPriceBeforeSelectedOptions, is(app.getCartPage().productPrice(playstationFullName)));

        assertThat(gamePrice, is(app.getCartPage().productPrice(gameFullName)));

        assertThat(cartSumAfterGameAddition, is(gamePrice + playstationPriceAfterSelectedOptions));

        Assertions.assertFalse(app.getCartPage()
                .checkAmountIsCorrect()
                .deleteProduct(gameFullName)
                .productIsPresent(gameFullName));

        int cartSumAfterGameDeletion = app.getCartPage().cartSum();

        assertThat(cartSumAfterGameDeletion, is(cartSumAfterGameAddition - gamePrice));

        app.getCartPage()
                .increaseProductAmount(playstationFullName)
                .increaseProductAmount(playstationFullName)
                .checkAmountIsCorrect();
        int cartSumWithThreePlaystations = app.getCartPage()
                .cartSum();

        assertThat(cartSumWithThreePlaystations, is(playstationPriceAfterSelectedOptions * 3));

        Assertions.assertFalse(app.getCartPage()
                .restoreLastRemoved()
                .productIsPresent(gameFullName), gameFullName + " нет в корзине!");
        int cartSumAfterGameRestore = app.getCartPage()
                .checkAmountIsCorrect()
                .cartSum();
        assertThat(cartSumAfterGameRestore, is(cartSumWithThreePlaystations + gamePrice));
    }
}
