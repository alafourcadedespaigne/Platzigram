package py.puchi.platzigram.posts;

import java.util.List;

import py.puchi.platzigram.model.Post;

/**
 * Created by Alejandro on 22/1/2017.
 */

public class PostsPresenterImpl implements PostsPresenter, PostsTaskListener{

    PostsView postsView;
    PostsInteractor postsInteractor;

    public PostsPresenterImpl(PostsView postsView) {
        this.postsView = postsView;
        this.postsInteractor = new PostsInteractorImpl(this);
    }

    @Override
    public void loadPosts() {
        postsInteractor.getPosts();
    }

    @Override
    public void onPostLoaded(List<Post> posts) {
        postsView.showPosts(posts);
    }
}
