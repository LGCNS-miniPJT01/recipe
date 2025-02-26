package recipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipe.dto.CommentDto;
import recipe.entity.Comment;
import recipe.entity.User;
import recipe.service.CommentService;
import recipe.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "댓글 API", description = "댓글 작성, 수정, 삭제, 조회 API")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    // 댓글 작성 (userId 직접 입력받음)
    @PostMapping("/{id}")
    @Operation(summary = "댓글 작성", description = "특정 레시피에 댓글을 작성합니다.")
    public ResponseEntity<Comment> addComment(
            @Parameter(description = "레시피 ID") @PathVariable Long id,
            @Parameter(description = "댓글 내용") @RequestParam String content,
            @Parameter(description = "작성자 ID") @RequestParam Long userId
    ) {
        User user = userService.getUserById(userId);
        Comment comment = commentService.saveComment(id, content, user);
        return ResponseEntity.ok(comment);
    }

    // 댓글 수정 (userId 직접 입력받음)
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

    // 댓글 삭제 (userId 직접 입력받음)
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

    // 특정 레시피의 댓글 목록 조회 (DTO 변환 추가)
    @GetMapping("/recipe/{id}")
    @Operation(summary = "특정 레시피의 댓글 조회", description = "해당 레시피의 모든 댓글을 조회합니다.")
    public ResponseEntity<List<CommentDto>> getCommentsByRecipe(
            @Parameter(description = "레시피 ID") @PathVariable Long id
    ) {
        List<Comment> comments = commentService.getCommentsByRecipe(id);
        List<CommentDto> commentDTOs = comments.stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentDTOs);
    }

    // 관리자 전체 댓글 조회 (DTO 변환 추가)
    @GetMapping("/admin/all")
    @Operation(summary = "관리자 댓글 전체 조회", description = "관리자가 모든 댓글을 조회할 수 있습니다.")
    public ResponseEntity<List<CommentDto>> getAllCommentsForAdmin(
            @Parameter(description = "관리자 ID") @RequestParam Long userId
    ) {
        User user = userService.getUserById(userId);
        if (!user.isAdmin()) {
            return ResponseEntity.status(403).build();
        }
        List<Comment> comments = commentService.getAllCommentsForAdmin();
        List<CommentDto> commentDTOs = comments.stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentDTOs);
    }
}
