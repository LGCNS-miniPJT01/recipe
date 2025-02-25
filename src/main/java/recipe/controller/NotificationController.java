package recipe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import recipe.entity.Recipe;
import recipe.entity.User;
import recipe.repository.RecipeRepository;
import recipe.repository.UserRepository;
import recipe.service.NotificationService;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;  // User 조회용 Repository

    @Autowired
    private RecipeRepository recipeRepository;  // Recipe 조회용 Repository

    @PostMapping("/sendNotification")
    public void sendNotification(@RequestParam String senderName,
                                 @RequestParam String receiverName,
                                 @RequestParam Long recipeId,
                                 @RequestParam String message) {
        // 1️⃣ **DB에서 sender(보낸 사람)와 receiver(받는 사람) 조회**
        User sender = userRepository.findByUsername(senderName)
                .orElseThrow(() -> new IllegalArgumentException("해당 sender 사용자를 찾을 수 없음: " + senderName));

        User receiver = userRepository.findByUsername(receiverName)
                .orElseThrow(() -> new IllegalArgumentException("해당 receiver 사용자를 찾을 수 없음: " + receiverName));

        // 2️⃣ **DB에서 해당 recipeId를 가진 레시피 조회**
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 레시피를 찾을 수 없음: " + recipeId));

        // 3️⃣ **서비스를 통해 알림 전송**
        notificationService.sendNotification(sender, receiver, recipe, message);
    }
}



