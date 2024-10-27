package br.com.diogenes.card_authorizer.service.category.factory;

import br.com.diogenes.card_authorizer.repository.balance.BalanceRepository;
import br.com.diogenes.card_authorizer.service.category.Category;
import br.com.diogenes.card_authorizer.service.category.impl.CategoryCash;
import br.com.diogenes.card_authorizer.service.category.impl.CategoryFood;
import br.com.diogenes.card_authorizer.service.category.impl.CategoryMeal;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CategoryFactory {

    private final BalanceRepository balanceRepository;

    public CategoryFactory(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public Category getCategoryFromMCC(String mcc, String merchan) {
        switch (mcc) {
            case "5411", "5412":
                return new CategoryFood(balanceRepository);
            case "5811", "5812":
                return new CategoryMeal(balanceRepository);
            default:
                break;
        }

        var relevantWord = getRelevantWord(merchan);
        var listMerchantMeal = List.of("eats", "comida", "lanche", "restaurante", "pizza", "burger", "japonesa", "padaria");
        var listMerchantFood = List.of("mercado", "supermercado", "mercearia", "atacadista", "atacado", "feira", "quitanda");
        var isMerchantNameMeal = listMerchantMeal.stream().anyMatch(relevantWord::contains);
        var isMerchantNameFood = listMerchantFood.stream().anyMatch(relevantWord::contains);
        if (isMerchantNameMeal) {
            return new CategoryMeal(balanceRepository);
        } else if (isMerchantNameFood) {
            return new CategoryFood(balanceRepository);
        } else {
            return new CategoryCash(balanceRepository);
        }

    }

    private String getRelevantWord(String merchantName){
        if (merchantName == null || merchantName.isEmpty()) {
            return "";
        }

        String pattern = "^\\S+(?:\\s[^\\s]+)+|^\\S+(?:\\*\\S+)?";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(merchantName);

        if (matcher.find()) {
            return matcher.group().trim().toLowerCase();
        }

        return "";
    }
}
