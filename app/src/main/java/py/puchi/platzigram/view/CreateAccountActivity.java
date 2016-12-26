package py.puchi.platzigram.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(py.puchi.platzigram.R.layout.activity_create_account);
        showToolbar(getResources().getString(py.puchi.platzigram.R.string.toolbar_tittle_createaccount), true);

    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(py.puchi.platzigram.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);

    }
}
