package id.ac.amikom.avent.feature.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import id.ac.amikom.avent.main.BaseActivity;
import id.ac.amikom.avent.R;
import id.ac.amikom.avent.auth.AuthInteractor;
import id.ac.amikom.avent.feature.profile.ProfileActivity;

public class SignUpActivity extends BaseActivity implements AuthInteractor.OnAuthListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private EditText mEtEmail;
    private EditText mEtPassword;
    private EditText mEtRetypePassword;
    private Button mBtnSignUp;
    private ProgressBar mPbLoading;

    private AuthInteractor mAuthInteractor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuthInteractor = new AuthInteractor(this);
        setupView();
        setupViewListener();
    }

    @Override
    public void onAuthSuccess(FirebaseUser user) {
        Log.d(TAG, "createUser:success");
        getMainApp().setUser(user);
        openActivity();
    }

    @Override
    public void onAuthFailed(String errMsg, String errLog) {
        Log.w(TAG, "onAuthFailed: " + errLog);
        Toast.makeText(SignUpActivity.this, errMsg,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthLoadingStart() {
        mPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAuthLoadingStop() {
        mPbLoading.setVisibility(View.GONE);
    }

    private void openActivity() {
        Intent intent = new Intent(SignUpActivity.this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void createUser() {
        String email = mEtEmail.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String retypePassword = mEtRetypePassword.getText().toString().trim();
        mAuthInteractor.registerAuthentication(email, password, retypePassword);
    }

    private void setupView() {
        mEtEmail = findViewById(R.id.et_signup_email);
        mEtPassword = findViewById(R.id.et_signup_password);
        mEtRetypePassword = findViewById(R.id.et_signup_retype_password);
        mBtnSignUp = findViewById(R.id.btn_signup);
        mPbLoading = findViewById(R.id.pb_signup_loading);
    }

    private void setupViewListener() {
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                createUser();
            }
        });
    }
}
