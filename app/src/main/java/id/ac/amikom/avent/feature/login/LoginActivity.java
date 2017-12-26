package id.ac.amikom.avent.feature.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.amikom.avent.main.BaseActivity;
import id.ac.amikom.avent.main.MainActivity;
import id.ac.amikom.avent.R;
import id.ac.amikom.avent.auth.AuthInteractor;
import id.ac.amikom.avent.feature.register.SignUpActivity;

public class LoginActivity extends BaseActivity
        implements  AuthInteractor.OnAuthListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.et_login_email) EditText mEtEmail;
    @BindView(R.id.et_login_password) EditText mEtPassword;
    @BindView(R.id.tv_login_forgot) TextView mTvForgotPassword;
    @BindView(R.id.tv_login_signup) TextView mTvSignUp;
    @BindView(R.id.btn_login) Button mBtnLogin;
    private ProgressBar mPbLoading;

    private FirebaseUser mUser;
    private AuthInteractor mAuthInteractor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mAuthInteractor = new AuthInteractor(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mAuthInteractor.getCurrentUser();
        checkIfUserLogedIn();
    }

    private void checkIfUserLogedIn() {
        if (mUser != null) {
            getMainApp().setUser(mUser);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void loginWithEmailAndPassword() {
        String email = mEtEmail.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();

        mAuthInteractor.loginAuthentication(email, password);
    }

    @Override
    public void onAuthSuccess(FirebaseUser user) {
        Log.d(TAG, "signInWithEmail:success");
        mUser = mAuthInteractor.getCurrentUser();
        getMainApp().setUser(mUser);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onAuthFailed(String errMsg, String errLog) {
        Log.w(TAG, "onAuthFailed: " + errLog);
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthLoadingStart() {
        mPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAuthLoadingStop() {
        mPbLoading.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_login)
    public void onBtnLoginClick() {
        hideKeyboard();
        loginWithEmailAndPassword();
    }

    @OnClick(R.id.tv_login_signup)
    public void onTvSignUp() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    @OnClick(R.id.tv_login_forgot)
    public void onForgotClick() {

    }
}
