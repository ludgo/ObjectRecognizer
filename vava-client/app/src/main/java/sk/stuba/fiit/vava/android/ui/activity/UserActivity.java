package sk.stuba.fiit.vava.android.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Template for activities for authenticated users only
 */
public abstract class UserActivity extends AppCompatActivity {

    /**
     * Check whether user signed in. Otherwise force login
     * @return Current Firebase user
     */
    protected FirebaseUser getSignedUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            toLoginActivity();
        }
        return firebaseUser;
    }

    /**
     * Get user ID
     * @return User ID
     */
    protected String getUid() {
        return getSignedUser().getUid();
    }

    /**
     * To {@link LoginActivity}
     */
    private void toLoginActivity() {
        // No signed user. Redirect to login page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        // ..and end current activity
        finish();
    }
}
