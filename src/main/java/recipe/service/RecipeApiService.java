package recipe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import recipe.dto.RecipeApiDto;
import recipe.repository.RecipeRepository;
import recipe.repository.RecipeStepRepository;
import recipe.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeApiService {
    
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeApiDataService recipeApiDataService;

    @Value("${api.key}")
    private String apiKey;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.endpoint}")
    private String apiEndpoint;

    @Value("${api.response-format}")
    private String responseFormat;

    @Value("${api.page-size}")
    private int pageSize;

    // fetch가 필요한지 확인 후 API 호출 여부 결정
    public void fetchRecipesIfNeeded() {
        long adminRecipeCount = recipeRepository.countRecipesByAdminUser();

        if (adminRecipeCount >= 1000) {
            log.info("✅ 현재 DB에 관리자(user_id = 1)의 레시피 데이터가 {}개 존재. API 호출 불필요.", adminRecipeCount);
            return;
        }

        log.info("⚠️ 현재 DB에 관리자(user_id = 1)의 레시피 데이터가 부족함 (현재 {}개). API 호출 시작.", adminRecipeCount);
        fetchAndSaveRecipes();
    }

    // API에서 데이터를 가져와 저장
    public void fetchAndSaveRecipes() {
        int page = 1;
        int totalCount = 0;
        boolean hasMoreData = true;

        while (hasMoreData) {
            String requestUrl = String.format("%s%s/%s/%s/%d/%d",
                    apiUrl, apiKey, apiEndpoint, responseFormat, (page - 1) * pageSize + 1, page * pageSize);
            
            log.info("📌 API 요청 URL: {}", requestUrl);

            try {
                RecipeApiDto response = restTemplate.getForObject(requestUrl, RecipeApiDto.class);
                
                log.info("📌 API 응답 데이터: {}", response);


                if (response != null && response.getCookRcp01() != null && response.getCookRcp01().getRow() != null) {
                    int fetchedCount = response.getCookRcp01().getRow().size();
                    totalCount += fetchedCount;

                    log.info("✅ {}개의 데이터 가져옴 (현재까지 총 {}개)", fetchedCount, totalCount);

                    recipeApiDataService.saveRecipes(response);

                    if (fetchedCount < pageSize) {
                        hasMoreData = false;
                    }
                } else {
                    log.warn("⚠️ 더 이상 데이터가 없음");
                    hasMoreData = false;
                }
            } catch (Exception e) {
                log.error("❌ API 호출 중 오류 발생: {}", e.getMessage(), e);
                hasMoreData = false;
            }

            page++;
        }

        log.info("🎉 총 가져온 레시피 개수: {}", totalCount);
    }
    
    
    
   
}
