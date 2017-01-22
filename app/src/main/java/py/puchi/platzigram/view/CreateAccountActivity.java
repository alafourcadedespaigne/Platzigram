package py.puchi.platzigram.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import py.puchi.platzigram.login.ui.LoginActivity;
import py.puchi.platzigram.R;

public class CreateAccountActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private TextInputEditText etName;
    private TextInputEditText etUser;

    // Declarar una variable de tipo Auth de Firebase
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(py.puchi.platzigram.R.layout.activity_create_account);
        showToolbar(getResources().getString(py.puchi.platzigram.R.string.toolbar_tittle_createaccount), true);

        firebaseAuth = FirebaseAuth.getInstance();

        etEmail = (TextInputEditText) findViewById(R.id.email);
        etPassword = (TextInputEditText) findViewById(R.id.password_createaccount);
        etConfirmPassword = (TextInputEditText) findViewById(R.id.confirmPassword);
        etName = (TextInputEditText) findViewById(R.id.name);
        etUser = (TextInputEditText) findViewById(R.id.user);

        Button btnCreateAccount = (Button) findViewById(R.id.joinUs);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString().trim();
                String user = etUser.getText().toString().trim();
                String username = etName.getText().toString().trim();

                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (email.equals("")) {
                    Toast.makeText(CreateAccountActivity.this, R.string.emailValidation, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.equals("")){
                    Toast.makeText(CreateAccountActivity.this, R.string.requiredPassword, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)){
                    Toast.makeText(CreateAccountActivity.this, R.string.equalsPassword, Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    Toast.makeText(CreateAccountActivity.this, R.string.AuthFailure, Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                                    finish();
                                }
                            }
                        });


            }
        });

    }

    public void showToolbar(String tittle, boolean upButton) {
        Toolbar toolbar = (Toolbar) findViewById(py.puchi.platzigram.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }
}
