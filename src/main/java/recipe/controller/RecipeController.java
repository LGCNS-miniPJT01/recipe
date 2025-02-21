package recipe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import recipe.entity.Recipe;
import recipe.entity.User;
import recipe.service.RecipeService;
import recipe.service.UserServiceImpl;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private UserServiceImpl userService;

	@PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
        Recipe savedRecipe = recipeService.saveRecipe(recipe);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRecipe);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }
    
    //Soft Delete API: 본인 레시피만 삭제 가능 (관리자는 모두 삭제 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecipe(@PathVariable Long recipeId, @RequestParam Long userId) {
        User user = userService.getUserById(userId);  // (가정) 유저 정보 불러오기
        recipeService.softDeleteRecipe(recipeId, user);
        return ResponseEntity.ok("레시피가 삭제되었습니다.");
    }
    
}