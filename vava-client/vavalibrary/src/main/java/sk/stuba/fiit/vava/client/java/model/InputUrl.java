package sk.stuba.fiit.vava.client.java.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
