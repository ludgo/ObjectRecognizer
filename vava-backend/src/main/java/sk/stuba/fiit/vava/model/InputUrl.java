package sk.stuba.fiit.vava.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Request body:
 * Insert urls to image model
 */
@NoArgsConstructor
@AllArgsConstructor
public class InputUrl {

    @Getter
    @Setter
    private LoginCredentials loginCredentials;

    @Getter
    @Setter
    private Set<String> urls;
}
