package sk.stuba.fiit.vava.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static sk.stuba.fiit.vava.component.ClarifaiProperties.PREFIX_CLARIFAI;

/**
 * Spring-boot-style Clarifai properties representation for custom properties defined in default .properties file
 */
@Component
@ConfigurationProperties(PREFIX_CLARIFAI)
public class ClarifaiProperties {

    static final String PREFIX_CLARIFAI = "clarifai";

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    private String secret;
}
