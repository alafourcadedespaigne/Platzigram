package py.puchi.platzigram.posts;

import java.util.List;

import py.puchi.platzigram.model.Post;

/**
 * Created by Alejandro on 22/1/2017.
 */

public interface PostsView {

    void showPosts(List<Post> posts);

    void showAddNewPostView();
}
