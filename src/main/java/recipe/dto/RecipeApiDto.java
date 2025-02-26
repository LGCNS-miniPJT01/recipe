package recipe.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeApiDto {

    @JsonProperty("COOKRCP01")
    private RecipeDetail cookRcp01;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecipeDetail {
    	
    	@JsonProperty("total_count")
        private String totalCount;
    	
    	@JsonProperty("row")
        private List<RecipeItem> row;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecipeItem {
    	
    	@JsonProperty("RCP_SEQ")
        private String recipeId;
    	
        @JsonProperty("RCP_NM")
        private String title;

        @JsonProperty("RCP_WAY2")
        private String cookingMethod;

        @JsonProperty("RCP_PAT2")
        private String category;

        @JsonProperty("INFO_WGT")
        private String weight;

        @JsonProperty("INFO_ENG")
        private String energy; // int

        @JsonProperty("INFO_CAR")
        private String carbohydrate; //float

        @JsonProperty("INFO_PRO")
        private String protein; // float

        @JsonProperty("INFO_FAT")
        private String fat; //float

        @JsonProperty("INFO_NA")
        private String sodium; // float

        @JsonProperty("HASH_TAG")
        private String hashTag;

        @JsonProperty("ATT_FILE_NO_MAIN")
        private String imageSmall;

        @JsonProperty("ATT_FILE_NO_MK")
        private String imageLarge;

        @JsonProperty("RCP_PARTS_DTLS")
        private String ingredients;

        @JsonProperty("RCP_NA_TIP")
        private String tip;
     // 📌 조리 과정 저장하는 맵
        private Map<String, String> manualSteps = new HashMap<>();
        private Map<String, String> manualImages = new HashMap<>();

        // 📌 JSON의 "MANUALXX" 데이터를 자동으로 `manualSteps`에 넣기
        @JsonAnySetter
        public void setManualSteps(String key, String value) {
            if (key.startsWith("MANUAL")) {  // "MANUAL01" ~ "MANUAL20"
                if (key.contains("_IMG")) {
                    manualImages.put(key, value);
                } else {
                    manualSteps.put(key, value);
                }
            }
        }

//        @JsonProperty("MANUAL01")
//        private String manual01;
//
//        @JsonProperty("MANUAL02")
//        private String manual02;
//
//        @JsonProperty("MANUAL03")
//        private String manual03;
//
//        @JsonProperty("MANUAL04")
//        private String manual04;
//
//        @JsonProperty("MANUAL05")
//        private String manual05;
//
//        @JsonProperty("MANUAL06")
//        private String manual06;
//
//        @JsonProperty("MANUAL07")
//        private String manual07;
//
//        @JsonProperty("MANUAL08")
//        private String manual08;
//
//        @JsonProperty("MANUAL09")
//        private String manual09;
//
//        @JsonProperty("MANUAL10")
//        private String manual10;
//        
//        @JsonProperty("MANUAL11")
//        private String manual11;
//
//        @JsonProperty("MANUAL12")
//        private String manual12;
//
//        @JsonProperty("MANUAL13")
//        private String manual13;
//
//        @JsonProperty("MANUAL14")
//        private String manual14;
//
//        @JsonProperty("MANUAL15")
//        private String manual15;
//
//        @JsonProperty("MANUAL16")
//        private String manual16;
//
//        @JsonProperty("MANUAL17")
//        private String manual17;
//
//        @JsonProperty("MANUAL18")
//        private String manual18;
//
//        @JsonProperty("MANUAL19")
//        private String manual19;
//
//        @JsonProperty("MANUAL20")
//        private String manual20;
//
//
//        @JsonProperty("MANUAL_IMG01")
//        private String manualImg01;
//
//        @JsonProperty("MANUAL_IMG02")
//        private String manualImg02;
//
//        @JsonProperty("MANUAL_IMG03")
//        private String manualImg03;
//        
//        @JsonProperty("MANUAL_IMG04")
//        private String manualImg04;
//
//        @JsonProperty("MANUAL_IMG05")
//        private String manualImg05;
//
//        @JsonProperty("MANUAL_IMG06")
//        private String manualImg06;
//        
//        @JsonProperty("MANUAL_IMG07")
//        private String manualImg07;
//
//        @JsonProperty("MANUAL_IMG08")
//        private String manualImg08;
//
//        @JsonProperty("MANUAL_IMG09")
//        private String manualImg09;
//        
//        @JsonProperty("MANUAL_IMG10")
//        private String manualImg10;
//        
//        @JsonProperty("MANUAL_IMG11")
//        private String manualImg11;
//
//        @JsonProperty("MANUAL_IMG12")
//        private String manualImg12;
//
//        @JsonProperty("MANUAL_IMG13")
//        private String manualImg13;
//        
//        @JsonProperty("MANUAL_IMG14")
//        private String manualImg14;
//
//        @JsonProperty("MANUAL_IMG15")
//        private String manualImg15;
//
//        @JsonProperty("MANUAL_IMG16")
//        private String manualImg16;
//        
//        @JsonProperty("MANUAL_IMG17")
//        private String manualImg17;
//
//        @JsonProperty("MANUAL_IMG18")
//        private String manualImg18;
//
//        @JsonProperty("MANUAL_IMG19")
//        private String manualImg19;
//        
//        @JsonProperty("MANUAL_IMG20")
//        private String manualImg20;
        
    }
}

