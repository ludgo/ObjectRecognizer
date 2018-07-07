package sk.stuba.fiit.vava.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Credentials necessary to be contained in every authenticated API call by verified user
 * (Google recommends POST for every request by verified Firebase user)
 */
@NoArgsConstructor
@AllArgsConstructor
public class LoginCredentials {

    @Getter
    @Setter
    private String provider;

    @Getter
    @Setter
    private String token;
}
