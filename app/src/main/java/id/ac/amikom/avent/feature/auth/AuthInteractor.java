package id.ac.amikom.avent.feature.auth;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by dhiyaulhaqza on 11/22/17.
 */

public class AuthInteractor {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private OnAuthListener mAuthListener;

    public AuthInteractor(OnAuthListener authListener) {
        mAuthListener = authListener;
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
    }

    public void loginAuthentication(String email, String password) {
        if (isEmailPasswordInvalid(email, password)) return;
        mAuthListener.onAuthLoadingStart();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuthListener.onAuthLoadingStop();
                        if (task.isSuccessful()) {
                            mUser = getCurrentUser();
                            mAuthListener.onAuthSuccess(mUser);
                        } else {
                            notifyTaskFailure(task);
                        }
                    }
                });
    }

    public void registerAuthentication(String email, String password, String retypePassword) {
        boolean isPasswordMatch = retypePassword.equals(password);
        if (!isPasswordMatch) mAuthListener.onAuthFailed("Password doesn't match", "");
        if (isEmailPasswordInvalid(email, password) || !isPasswordMatch) return;
        mAuthListener.onAuthLoadingStart();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = getCurrentUser();
                            mAuthListener.onAuthSuccess(mUser);
                        } else {
                            notifyTaskFailure(task);
                        }
                    }
                });
    }

    public FirebaseUser getCurrentUser()    {
        return mAuth.getCurrentUser();
    }

    private void notifyTaskFailure(Task<AuthResult> task) {
        String errLog = "";
        if (task.getException() != null) {
            errLog = task.getException().getMessage();
        }
        mAuthListener.onAuthFailed("Authentication failed.", errLog);
    }

    private boolean isEmailPasswordInvalid(String email, String password) {
        boolean isEmpty = email.equals("") || password.equals("");
        boolean isEmailMatches = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        boolean isAmikomEmail = (email.endsWith("@students.amikom.ac.id") || email.endsWith("@amikom.ac.id"));
        boolean isEmailInvalid = !isEmpty && !(isEmailMatches && isAmikomEmail);
        boolean isNotPasswordSizeGreaterThanFive = !isEmpty && !(password.length() > 5);

        if (isEmpty) mAuthListener.onAuthFailed("Email or password can not be empty", "");
        if (isEmailInvalid) mAuthListener.onAuthFailed("Must use AMIKOM email", "");
        if (isNotPasswordSizeGreaterThanFive) mAuthListener.onAuthFailed("Password must be greater than 5", "");

        return isEmpty || isEmailInvalid || isNotPasswordSizeGreaterThanFive;
    }

    public interface OnAuthListener {
        void onAuthSuccess(FirebaseUser user);

        void onAuthFailed(String errMsg, String errLog);

        void onAuthLoadingStart();

        void onAuthLoadingStop();
    }
}

