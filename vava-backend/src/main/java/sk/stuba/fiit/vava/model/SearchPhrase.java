package sk.stuba.fiit.vava.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request body:
 * Search image model by text phrase
 */
@NoArgsConstructor
@AllArgsConstructor
public class SearchPhrase {

    @Getter
    @Setter
    private LoginCredentials loginCredentials;

    @Getter
    @Setter
    private String phrase;
}
