package recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import recipe.entity.Comment;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private String content;
    private Date createdAt;
    private Boolean deletedYn;
    private Long recipeId;
    private Long userId;

    public static CommentDto fromEntity(Comment comment) {
        return new CommentDto(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getDeletedYn(),
                comment.getRecipe().getRecipeId(),
                comment.getUser().getUserId()
        );
    }
}
