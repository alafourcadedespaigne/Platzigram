package py.puchi.platzigram.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import py.puchi.platzigram.PlatziGramApplication;
import py.puchi.platzigram.R;
import py.puchi.platzigram.login.LoginRepository;
import py.puchi.platzigram.login.loginTaskListener;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Alejandro on 22/1/2017.
 */

public class LoginRepositoryImpl implements LoginRepository {

    FirebaseAuth firebaseAuth;
    loginTaskListener listener;
    PlatziGramApplication application;
    Context context;

    public LoginRepositoryImpl(loginTaskListener listener) {

        firebaseAuth = FirebaseAuth.getInstance();
        this.listener = listener;


    }


    @Override
    public void signIn(String email, String password) {

        application = (PlatziGramApplication)getApplicationContext();
        context = application.getContext();

        //Loguearme con Firebase
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        //Quedarnos con el usuarioLogueado
                        FirebaseUser user = task.getResult().getUser();

                        //Guardamos los datos del usuario en SharedPreferences
                        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences("USER", MODE_PRIVATE).edit();
                        sharedPreferences.putString("email", user.getEmail());
                        sharedPreferences.commit();

                        if (!task.isSuccessful()) {
                            listener.loginError(task.getException().toString());
                        } else {
                            listener.loginSuccess();

                        }


                    }
                });


    }
}
