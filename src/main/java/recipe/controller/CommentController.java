package recipe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import recipe.entity.Comment;
import recipe.entity.Recipe;
import recipe.entity.User;
import recipe.repository.RecipeRepository;
import recipe.service.CommentService;
import recipe.service.NotificationService;
import recipe.service.UserService;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "댓글 API", description = "댓글 작성, 수정, 삭제, 조회 API")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private RecipeRepository recipeRepository;
    
    @Autowired
    private NotificationService notificationService;

    // ✅ 댓글 작성 (userId 직접 입력받음)
    @PostMapping("/{recipeId}")
    @Operation(summary = "댓글 작성", description = "특정 레시피에 댓글을 작성합니다.")
    public ResponseEntity<Comment> addComment(
            @Parameter(description = "레시피 ID") @PathVariable("recipeId") Long recipeId,
            @Parameter(description = "댓글 내용") @RequestParam(value = "content") String content,
            @Parameter(description = "작성자 ID") @RequestParam(value = "userId") Long userId
    ) {
        User user = userService.getUserById(userId);
        Comment comment = commentService.saveComment(recipeId, content, user);
        
        // 댓글이 달리 레시피의 작성자에게 알림 전송 - shy
        Recipe recipe = recipeRepository.findById(recipeId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));
        User recipeOwner = recipe.getUser(); // 레시피 작성자
        
        // 본인 댓글이 아닐 경우 알림 전송
        if (!user.equals(recipeOwner)) {
            String message = user.getUsername() + "님이 당신의 레시피에 댓글을 남겼습니다.";

            // DB에 알림 저장 및 웹소켓 알림 전송
            notificationService.sendNotification(user, recipeOwner, recipe, message);
        }
        
        return ResponseEntity.ok(comment);
    }

    // ✅ 댓글 수정 (userId 직접 입력받음)
    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "작성한 댓글을 수정합니다.")
    public ResponseEntity<Comment> updateComment(
            @Parameter(description = "댓글 ID") @PathVariable Long commentId,
            @Parameter(description = "수정할 댓글 내용") @RequestParam String newContent,
            @Parameter(description = "작성자 ID") @RequestParam Long userId
    ) {
        User user = userService.getUserById(userId);
        Comment updatedComment = commentService.updateComment(commentId, newContent, user);
        return ResponseEntity.ok(updatedComment);
    }

    // ✅ 댓글 삭제 (userId 직접 입력받음)
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "작성한 댓글을 논리 삭제합니다.")
    public ResponseEntity<Void> deleteComment(
            @Parameter(description = "댓글 ID") @PathVariable Long commentId,
            @Parameter(description = "작성자 ID") @RequestParam Long userId
    ) {
        User user = userService.getUserById(userId);
        commentService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build();
    }

    // ✅ 특정 레시피의 댓글 목록 조회
    @GetMapping("/recipe/{recipeId}")
    @Operation(summary = "특정 레시피의 댓글 조회", description = "해당 레시피의 모든 댓글을 조회합니다.")
    public ResponseEntity<List<Comment>> getCommentsByRecipe(
            @Parameter(description = "레시피 ID") @PathVariable Long recipeId
    ) {
        List<Comment> comments = commentService.getCommentsByRecipe(recipeId);
        return ResponseEntity.ok(comments);
    }

    // ✅ 관리자 전체 댓글 조회 (userId 직접 입력받음)
    @GetMapping("/admin/all")
    @Operation(summary = "관리자 댓글 전체 조회", description = "관리자가 모든 댓글을 조회할 수 있습니다.")
    public ResponseEntity<List<Comment>> getAllCommentsForAdmin(
            @Parameter(description = "관리자 ID") @RequestParam Long userId
    ) {
        User user = userService.getUserById(userId);
        if (!user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }
        List<Comment> comments = commentService.getAllCommentsForAdmin();
        return ResponseEntity.ok(comments);
    }
}
