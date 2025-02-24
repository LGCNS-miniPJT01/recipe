package recipe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindEmailRequestDto {
    private String username;
    private String phone;
}
