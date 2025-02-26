package recipe.Initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import recipe.service.RecipeApiService;

@Slf4j
@Component
@Order(2)
public class RecipeDataInitializer implements CommandLineRunner {

	@Autowired
    private RecipeApiService recipeApiService;

    @Override
    public void run(String... args) {
        log.info("🔄 서버 실행 중, 레시피 데이터 확인 중...");
        recipeApiService.fetchRecipesIfNeeded();
        log.info("✅ 레시피 데이터 확인 완료.");
    }
}
