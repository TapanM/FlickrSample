package com.tapan.flickrsample.objects;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class FeedObject {

    private List<Items> items;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public class Items {

        private String title;
        private String link;
        private String date_taken;
        private String description;
        private Media media;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    public class Media {

        private String m;
    }
}
