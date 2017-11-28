package id.ac.amikom.amivent.feature;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import id.ac.amikom.amivent.BaseActivity;
import id.ac.amikom.amivent.R;
import id.ac.amikom.amivent.auth.AuthInteractor;

public class LoginActivity extends BaseActivity
        implements View.OnClickListener, AuthInteractor.OnAuthListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText mEtEmail;
    private EditText mEtPassword;
    private TextView mTvForgotPassword;
    private TextView mTvSignUp;
    private Button mBtnLogin;
    private ProgressBar mPbLoading;

    private FirebaseUser mUser;
    private AuthInteractor mAuthInteractor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuthInteractor = new AuthInteractor(this);
        setupView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mAuthInteractor.getCurrentUser();
        checkIfUserLogedIn();
    }

    private void checkIfUserLogedIn() {
        if (mUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_forgot:
                break;
            case R.id.tv_login_signup:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
            case R.id.btn_login:
                loginWithEmailAndPassword();
                break;
        }
    }

    private void loginWithEmailAndPassword() {
        String email = mEtEmail.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();

        mAuthInteractor.loginAuthentication(email, password);
    }

    @Override
    public void onSuccess(FirebaseUser user) {
        Log.d(TAG, "signInWithEmail:success");
        mUser = mAuthInteractor.getCurrentUser();
        getMainApp().setUser(mUser);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onFailed(String errMsg, String errLog) {
        Log.w(TAG, "onFailed: " + errLog);
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingStart() {
        mPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingStop() {
        mPbLoading.setVisibility(View.GONE);
    }

    private void setupView() {
        mEtEmail = findViewById(R.id.et_login_email);
        mEtPassword = findViewById(R.id.et_login_password);
        mBtnLogin = findViewById(R.id.btn_login);
        mTvSignUp = findViewById(R.id.tv_login_signup);
        mTvForgotPassword = findViewById(R.id.tv_login_forgot);
        mPbLoading = findViewById(R.id.pb_signin_loading);

        mBtnLogin.setOnClickListener(this);
        mTvSignUp.setOnClickListener(this);
        mTvForgotPassword.setOnClickListener(this);
    }
}
