package org.example.framework.managers;

import org.example.framework.pages.CartPage;
import org.example.framework.pages.ProductPage;
import org.example.framework.pages.ProductSearchCatalogPage;
import org.example.framework.pages.StartPage;

/**
 * @author Arkadiy_Alaverdyan
 * Класс для управления страничками
 */
public class PageManager {

    /**
     * Менеджер страниц
     */
    private static PageManager pageManager;

    /**
     * Стартовая страница
     */
    private StartPage startPage;

    /**
     * Страница результата поиска
     */
    private ProductSearchCatalogPage productSearchCatalogPage;

    /**
     * Страница найденного продукта
     */
    private ProductPage productPage;

    /**
     * Страница корзины
     */
    private CartPage cartPage;

    /**
     * Конструктор специально был объявлен как private (singleton паттерн)
     *
     * @see PageManager#getPageManager()
     */
    private PageManager() {
    }

    /**
     * Ленивая инициализация PageManager
     *
     * @return PageManager
     */
    public static PageManager getPageManager() {
        if (pageManager == null) {
            pageManager = new PageManager();
        }
        return pageManager;
    }

    /**
     * Ленивая инициализация {@link org.example.framework.pages.StartPage}
     *
     * @return StartPage
     */
    public StartPage getStartPage() {
        if (startPage == null) {
            startPage = new StartPage();
        }
        return startPage;
    }

    /**
     * Ленивая инициализация {@link ProductSearchCatalogPage}
     *
     * @return ProductSearchCatalog
     */
    public ProductSearchCatalogPage getProductSearchCatalogPage() {
        if (productSearchCatalogPage == null) {
            productSearchCatalogPage = new ProductSearchCatalogPage();
        }
        return productSearchCatalogPage;
    }

    /**
     * Ленивая инициализация {@link ProductPage}
     *
     * @return ProductPage
     */
    public ProductPage getProductPage() {
        if (productPage == null) {
            productPage = new ProductPage();
        }
        return productPage;
    }

    /**
     * Ленивая инициализация {@link org.example.framework.pages.CartPage}
     *
     * @return CartPage
     */
    public CartPage getCartPage() {
        if (cartPage == null) {
            cartPage = new CartPage();
        }
        return cartPage;
    }


}
