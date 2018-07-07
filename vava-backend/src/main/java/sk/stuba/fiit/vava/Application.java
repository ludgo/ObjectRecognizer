package sk.stuba.fiit.vava;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sk.stuba.fiit.vava.util.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Spring application server launcher class
 */
@SpringBootApplication
public class Application {

    /**
     * Initialize Firebase Admin SDK to get access to user authentication
     * @throws FileNotFoundException if Firebase Admin SDK config file not found
     */
    private static void initFirebaseAdmin() throws FileNotFoundException {
        FileInputStream serviceAccount =
                new FileInputStream(Constants.FIREBASE_AUTH_PRIVATE_KEY_FILE_NAME);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                .setDatabaseUrl(Constants.FIREBASE_DATABASE_URL)
                .build();

        FirebaseApp.initializeApp(options);
    }

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
        try {
            initFirebaseAdmin();
        }
        catch (FileNotFoundException e) {
            Logger.getLogger(Application.class.getName()).fatal(
                    "Firebase Admin SDK configuration file not found", e);
        }
    }
}
