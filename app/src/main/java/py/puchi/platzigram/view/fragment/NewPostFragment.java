package py.puchi.platzigram.view.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import py.puchi.platzigram.PlatziGramApplication;
import py.puchi.platzigram.R;
import py.puchi.platzigram.model.Post;
import py.puchi.platzigram.utils.Constans;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPostFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "New post fragment";
    String mCurrentPhotoPath;

    ImageView ivPicture;

    // Firebase
    String mCurrentAbsolutePhotePath;
    PlatziGramApplication app;
    StorageReference storageReference;
    DatabaseReference postReference;

    public NewPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_post, container, false);
        showToolbar(getResources().getString(R.string.new_post_title), false, view);

        Button btnTakePicture = (Button) view.findViewById(R.id.btnTakePicture);
        ivPicture = (ImageView) view.findViewById(R.id.ivPicture);

        //Firebase
        app = (PlatziGramApplication) getActivity().getApplicationContext();
        storageReference = app.getStorageReference();
        postReference = app.getPostReference();

        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Picasso.with(getActivity()).load(mCurrentPhotoPath).into(ivPicture);
            addPictureToGallery();



            //Una vez que seteamos en el imageView vamos a guardar la imagen en Firebase

            uploadFile();
        }
    }

    private void uploadFile() {

        File newFile = new File(mCurrentAbsolutePhotePath);

        Uri contentUri = Uri.fromFile(newFile);

        StorageReference imagesReference = storageReference.child(Constans.FIREBASE_STORAGE_IMAGES + contentUri.getLastPathSegment());

        //Esto es nu Task propio de Firebase
        UploadTask uploadTask = imagesReference.putFile(contentUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error subiendo la imagen", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String imageUrl = taskSnapshot.getDownloadUrl().toString();
                createNewPost(imageUrl);
            }
        });
    }

    private void createNewPost(String url) {

        SharedPreferences preferencias = getActivity().getSharedPreferences("USER",getActivity().MODE_PRIVATE);
        String email = preferencias.getString("email", "");

        //Como en firebase no se pueden guardar los email con punto lo encodamos
        String encodeEmail = email.replace(".",",");



        Post post = new Post(encodeEmail, url, (double)new Date().getTime());

        postReference.push().setValue(post);

        getFragmentManager().popBackStackImmediate();

    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d(TAG, getString(R.string.new_post_error_create_file));
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "py.puchi.platzigram",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();

        // Esto lo necesitamos para guardar la imagen en Firebase
        mCurrentAbsolutePhotePath = image.getAbsolutePath();
        return image;
    }

    private void addPictureToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public void showToolbar(String title, boolean upButton, View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);


    }

}
