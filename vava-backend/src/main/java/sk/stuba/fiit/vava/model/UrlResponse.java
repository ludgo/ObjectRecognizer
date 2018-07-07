package sk.stuba.fiit.vava.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Response body:
 * Respond with a set of unique urls
 */
@NoArgsConstructor
@AllArgsConstructor
public class UrlResponse {

    @Getter
    @Setter
    private Set<String> urls;
}
