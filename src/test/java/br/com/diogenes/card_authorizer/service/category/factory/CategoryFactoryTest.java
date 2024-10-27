package br.com.diogenes.card_authorizer.service.category.factory;

import br.com.diogenes.card_authorizer.repository.balance.BalanceRepository;
import br.com.diogenes.card_authorizer.service.category.Category;
import br.com.diogenes.card_authorizer.service.category.impl.CategoryCash;
import br.com.diogenes.card_authorizer.service.category.impl.CategoryFood;
import br.com.diogenes.card_authorizer.service.category.impl.CategoryMeal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryFactoryTest {

    private CategoryFactory categoryFactory;

    @Mock
    private BalanceRepository balanceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryFactory = new CategoryFactory(balanceRepository);
    }

    @Test
    void testGetCategoryFromMCC_FoodMCC() {
        Category category = categoryFactory.getCategoryFromMCC("5411", "anyMerchant");
        assertTrue(category instanceof CategoryFood, "Expected CategoryFood for MCC 5411");

        category = categoryFactory.getCategoryFromMCC("5412", "anyMerchant");
        assertTrue(category instanceof CategoryFood, "Expected CategoryFood for MCC 5412");
    }

    @Test
    void testGetCategoryFromMCC_MealMCC() {
        Category category = categoryFactory.getCategoryFromMCC("5811", "anyMerchant");
        assertTrue(category instanceof CategoryMeal, "Expected CategoryMeal for MCC 5811");

        category = categoryFactory.getCategoryFromMCC("5812", "anyMerchant");
        assertTrue(category instanceof CategoryMeal, "Expected CategoryMeal for MCC 5812");
    }

    @Test
    void testGetCategoryFromMerchantName_MealKeywords() {
        Category category = categoryFactory.getCategoryFromMCC("0000", "Uber Eats");
        assertTrue(category instanceof CategoryMeal, "Expected CategoryMeal for merchant containing 'eats'");

        category = categoryFactory.getCategoryFromMCC("0000", "Pizza Place");
        assertTrue(category instanceof CategoryMeal, "Expected CategoryMeal for merchant containing 'pizza'");
    }

    @Test
    void testGetCategoryFromMerchantName_FoodKeywords() {
        Category category = categoryFactory.getCategoryFromMCC("0000", "Mercado");
        assertTrue(category instanceof CategoryFood, "Expected CategoryFood for merchant containing 'mercado'");

        category = categoryFactory.getCategoryFromMCC("0000", "Supermercado");
        assertTrue(category instanceof CategoryFood, "Expected CategoryFood for merchant containing 'supermercado'");
    }

    @Test
    void testGetCategoryFromMerchantName_NoRelevantKeywords() {
        Category category = categoryFactory.getCategoryFromMCC("0000", "Random Store");
        assertTrue(category instanceof CategoryCash, "Expected CategoryCash for merchant with no relevant keywords");
    }

    @Test
    void testGetCategoryFromMerchantName_WithNullOrEmptyMerchant() {
        Category category = categoryFactory.getCategoryFromMCC("0000", null);
        assertTrue(category instanceof CategoryCash, "Expected CategoryCash for null merchant name");

        category = categoryFactory.getCategoryFromMCC("0000", "");
        assertTrue(category instanceof CategoryCash, "Expected CategoryCash for empty merchant name");
    }
}