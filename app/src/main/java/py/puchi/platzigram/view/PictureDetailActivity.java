package py.puchi.platzigram.view;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;

public class PictureDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(py.puchi.platzigram.R.layout.activity_picture_detail);
        showToolbar("", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setEnterTransition(new Fade());
        }

    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(py.puchi.platzigram.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(py.puchi.platzigram.R.id.collapsingToolbar);

    }

}
