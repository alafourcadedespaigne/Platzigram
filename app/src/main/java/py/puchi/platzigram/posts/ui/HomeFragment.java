package py.puchi.platzigram.posts.ui;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import py.puchi.platzigram.R;
import py.puchi.platzigram.adapter.PictureAdapterRecyclerView;
import py.puchi.platzigram.adapter.PostAdapterRecyclerView;
import py.puchi.platzigram.model.Post;
import py.puchi.platzigram.posts.PostsPresenter;
import py.puchi.platzigram.posts.PostsPresenterImpl;
import py.puchi.platzigram.posts.PostsView;
import py.puchi.platzigram.view.fragment.NewPostFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements PostsView {

    RecyclerView picturesRecycler;
    LinearLayoutManager linearLayoutManager;
    PictureAdapterRecyclerView pictureAdapterRecyclerView;
    PostAdapterRecyclerView postAdapterRecyclerView;
    ArrayList<Post> posts;


    PostsPresenter postsPresenter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(py.puchi.platzigram.R.layout.fragment_home, null);
        showToolbar(getResources().getString(py.puchi.platzigram.R.string.tab_home), false, view);

        //Para poblar el arreglo de la informacion de Firebase

        posts = new ArrayList<>();

        postsPresenter = new PostsPresenterImpl(this);


        picturesRecycler = (RecyclerView) view.findViewById(py.puchi.platzigram.R.id.pictureRecycler);

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        picturesRecycler.setLayoutManager(linearLayoutManager);

        postAdapterRecyclerView =
                new PostAdapterRecyclerView(posts, py.puchi.platzigram.R.layout.cardview_picture, getActivity());
        picturesRecycler.setAdapter(postAdapterRecyclerView);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewPostView();
            }
        });

        postsPresenter.loadPosts();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        postsPresenter.loadPosts();
    }




    public void showToolbar(String tittle, boolean upButton, View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(py.puchi.platzigram.R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);


    }

    @Override
    public void showPosts(List<Post> postsList) {

        posts.clear();
        posts.addAll(postsList);
        postAdapterRecyclerView.notifyDataSetChanged();

    }

    @Override
    public void showAddNewPostView() {

        NewPostFragment newPostFragment = new NewPostFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newPostFragment)
                .addToBackStack(null)
                .commit();
    }
}
