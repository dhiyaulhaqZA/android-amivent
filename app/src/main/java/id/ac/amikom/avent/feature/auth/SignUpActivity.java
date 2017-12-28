package id.ac.amikom.avent.feature.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.ac.amikom.avent.main.BaseActivity;
import id.ac.amikom.avent.R;
import id.ac.amikom.avent.feature.profile.ProfileActivity;

public class SignUpActivity extends BaseActivity implements AuthInteractor.OnAuthListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    @BindView(R.id.et_signup_email) EditText mEtEmail;
    @BindView(R.id.et_signup_password) EditText mEtPassword;
    @BindView(R.id.et_signup_retype_password) EditText mEtRetypePassword;
    @BindView(R.id.btn_signup) Button mBtnSignUp;
    @BindView(R.id.pb_signup_loading) ProgressBar mPbLoading;

    private AuthInteractor mAuthInteractor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        mAuthInteractor = new AuthInteractor(this);
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

    @OnClick(R.id.btn_signup)
    public void onBtnSignUpClick() {
        hideKeyboard();
        createUser();
    }
}
