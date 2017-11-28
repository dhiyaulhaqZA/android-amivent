package id.ac.amikom.amivent.feature;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import id.ac.amikom.amivent.BaseActivity;
import id.ac.amikom.amivent.MainApp;
import id.ac.amikom.amivent.R;
import id.ac.amikom.amivent.auth.AuthInteractor;

public class SignUpActivity extends BaseActivity implements AuthInteractor.OnAuthListener {

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private EditText mEtName;
    private EditText mEtEmail;
    private EditText mEtPassword;
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
    public void onSuccess(FirebaseUser user) {
        Log.d(TAG, "createUser:success");
        getMainApp().setUser(user);
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailed(String errMsg, String errLog) {
        Log.w(TAG, "onFailed: " + errLog);
        Toast.makeText(SignUpActivity.this, errMsg,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoadingStart() {
        mPbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingStop() {
        mPbLoading.setVisibility(View.GONE);
    }

    private void createUser() {
        String name = mEtName.getText().toString().trim();
        String email = mEtEmail.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();

        mAuthInteractor.registerAuthentication(name, email, password);
    }

    private void setupView() {
        mEtName = findViewById(R.id.et_signup_name);
        mEtEmail = findViewById(R.id.et_signup_email);
        mEtPassword = findViewById(R.id.et_signup_password);
        mBtnSignUp = findViewById(R.id.btn_signup);
        mPbLoading = findViewById(R.id.pb_signup_loading);
    }

    private void setupViewListener() {
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }
}
