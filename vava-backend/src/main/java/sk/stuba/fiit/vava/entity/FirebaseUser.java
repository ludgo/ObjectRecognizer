package sk.stuba.fiit.vava.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Database entity representing Firebase user
 */
@Entity
@Table(name = FirebaseUser.TABLE_NAME)
@NoArgsConstructor
public class FirebaseUser {

    static final String TABLE_NAME = "firebase_user";
    // PK
    static final String COLUMN_ID = "id";
    // Firebase user ID
    static final String COLUMN_UID = "uid";

    @Id
    @Column(name = COLUMN_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = COLUMN_UID, unique = true, nullable = false)
    @Getter
    @Setter
    private String uid;

    public FirebaseUser(String uid) {
        this.uid = uid;
    }
}
