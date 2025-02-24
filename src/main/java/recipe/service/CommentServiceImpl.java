package recipe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import recipe.entity.Comment;
import recipe.entity.Recipe;
import recipe.entity.User;
import recipe.repository.CommentRepository;
import recipe.repository.RecipeRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Override
    @Transactional
    public Comment saveComment(Long recipeId, String content, User user) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe not found"));

        Comment comment = new Comment();
        comment.setRecipe(recipe);
        comment.setUser(user);
        comment.setContent(content);
        commentRepository.save(comment);
        return comment;
    }

    @Override
    @Transactional
    public Comment updateComment(Long commentId, String newContent, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!user.isAdmin() && !comment.getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        comment.setContent(newContent);
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));

        if (!user.isAdmin() && !comment.getUser().getUserId().equals(user.getUserId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }

        comment.setDeletedYn(true);
        commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsByRecipe(Long recipeId) {
        return commentRepository.findByRecipe_RecipeIdAndDeletedYnFalse(recipeId);
    }

    @Override
    public List<Comment> getAllCommentsForAdmin() {
        return commentRepository.findAll();
    }
}
