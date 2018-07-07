package sk.stuba.fiit.vava.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Database entity representing image url of an image belonging to user
 */
@Entity
@Table(name = ImageUrl.TABLE_NAME, uniqueConstraints = @UniqueConstraint(
        columnNames = {ImageUrl.COLUMN_URL, ImageUrl.COLUMN_FIREBASE_USER_ID}))
@NoArgsConstructor
public class ImageUrl {

    static final String TABLE_NAME = "image_url";
    // PK
    static final String COLUMN_ID = "id";
    // Image url
    static final String COLUMN_URL = "url";
    // Is uploaded for image recognition?
    static final String COLUMN_IS_UPLOADED = "is_uploaded";
    // FK
    static final String COLUMN_FIREBASE_USER_ID = "firebase_user_id";

    @Id
    @Column(name = COLUMN_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = COLUMN_URL, nullable = false)
    @Getter
    @Setter
    private String url;

    @Column(name = COLUMN_IS_UPLOADED, nullable = false)
    @Getter
    @Setter
    private boolean isUploaded;

    @ManyToOne
    @JoinColumn(name = COLUMN_FIREBASE_USER_ID, referencedColumnName = FirebaseUser.COLUMN_ID, nullable = false)
    @Getter
    @Setter
    private FirebaseUser firebaseUser;

    public ImageUrl(String url, FirebaseUser firebaseUser) {
        this.url = url;
        this.isUploaded = false;
        this.firebaseUser = firebaseUser;
    }
}
