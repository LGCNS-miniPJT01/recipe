package recipe.service;

import recipe.entity.Comment;
import recipe.entity.User;
import java.util.List;

public interface CommentService {
    Comment saveComment(Long recipeId, String content, User user);
    Comment updateComment(Long commentId, String newContent, User user);
    void deleteComment(Long commentId, User user);
    List<Comment> getCommentsByRecipe(Long recipeId);
    List<Comment> getAllCommentsForAdmin(); // 관리자용 전체 댓글 조회
}
