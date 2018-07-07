package sk.stuba.fiit.vava.client.java.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
