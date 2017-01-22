package py.puchi.platzigram.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import py.puchi.platzigram.R;
import py.puchi.platzigram.login.LoginPresenter;
import py.puchi.platzigram.login.LoginPresenterImpl;
import py.puchi.platzigram.view.ContainerActivity;
import py.puchi.platzigram.view.CreateAccountActivity;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private TextInputEditText etemail;
    private TextInputEditText etpassword;
    private Button btnLogin;
    private ProgressBar progressBarLogin;
    private TextView tvCreateAccount;

    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setElementAndevents();
        loginPresenter = new LoginPresenterImpl(this);

    }

    private void setElementAndevents(){

        etemail = (TextInputEditText) findViewById(R.id.username);
        etpassword = (TextInputEditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        tvCreateAccount  = (TextView)findViewById(R.id.createHere);

        progressBarLogin = (ProgressBar) findViewById(R.id.progressbarLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignIn();
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCreateAccount();
            }
        });
    }




    @Override
    public void enableInputs() {

        setInputs(true);
    }

    @Override
    public void disableInputs() {
        setInputs(false);
    }

    @Override
    public void showProgressBar() {
        progressBarLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBarLogin.setVisibility(View.GONE);
    }

    @Override
    public void handleSignIn() {

        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();

        // Primero revisa las credenciales del usuario en Firebase
        if (email.equals("")) {
            Toast.makeText(LoginActivity.this, R.string.emailRequired, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.equals("")) {
            Toast.makeText(LoginActivity.this, R.string.passwordRequired, Toast.LENGTH_SHORT).show();
            return;
        }

        loginPresenter.validateLogin(email,password);

    }

    @Override
    public void navigateToMainScreen() {

        Intent intent = new Intent(this, ContainerActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToCreateAccount() {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void loginError(String error) {
        String msgError = String.format(getString(R.string.login_error_message_signin),error);
        Toast.makeText(LoginActivity.this, msgError, Toast.LENGTH_SHORT).show();
    }


    private void setInputs( boolean enable){
        etemail.setEnabled(enable);
        etpassword.setEnabled(enable);
        btnLogin.setEnabled(enable);
        tvCreateAccount.setEnabled(enable);
    }
}
