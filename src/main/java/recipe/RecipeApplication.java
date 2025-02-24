package recipe;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RecipeApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeApplication.class, args);
	}
	
	// ModelMapper 빈 등록
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
