package sk.stuba.fiit.vava.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sk.stuba.fiit.vava.android.R;
import sk.stuba.fiit.vava.android.util.TokenManager;
import sk.stuba.fiit.vava.client.java.rest.RestCallBuilder;

/**
 * Application sign in activity as an entry point to main functionality
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == ResultCodes.OK) {
                // Successfully signed in Firebase
                persistToken(FirebaseAuth.getInstance().getCurrentUser());
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, getString(R.string.error_cancelled), Toast.LENGTH_LONG).show();
                } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, getString(R.string.error_no_network), Toast.LENGTH_LONG).show();
                } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, getString(R.string.error_unknown), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Show Firebase Auth UI login screen
     */
    private void promptLogin() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false) // TODO remove: this line forces login every time application is newly opened
                        .setProviders(Collections.singletonList(
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(),
                REQUEST_CODE_SIGN_IN);
    }

    /**
     * Save token within client and continue with server login
     *
     * @param firebaseUser Current Firebase user
     */
    private void persistToken(FirebaseUser firebaseUser) {
        firebaseUser.getToken(true) // TODO remove: this boolean ensures token is refreshed every time
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            // Firebase token accessible
                            String token = task.getResult().getToken();
                            if (token != null) {
                                TokenManager.setFirebaseToken(token);
                                serverLogin();
                            }
                        }
                    }
                });
    }

    /**
     * Perform server login for current user
     */
    private void serverLogin() {
        // Call server login endpoint
        Call<Object> call = (new RestCallBuilder(TokenManager.getFirebaseToken()))
                .login();
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    // Server login successful
                    // Continue with main app functionality
                    toMainActivity();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.e(LoginActivity.class.getName(), "Server login failed", t);
                Toast.makeText(getBaseContext(), getString(R.string.error_login_failed),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * User event: layout button click
     * <p>
     * Initialize login within application
     * @param view View with set onClick event
     */
    public void initLogin(View view) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            // User not signed in Firebase
            promptLogin();
        } else {
            // User signed in Firebase, but not at the server yet
            persistToken(firebaseUser);
        }
    }

    /**
     * To {@link MainActivity}
     */
    private void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
