package py.puchi.platzigram.posts;

import py.puchi.platzigram.posts.PostsInteractor;
import py.puchi.platzigram.posts.PostsRepository;
import py.puchi.platzigram.posts.PostsRepositoryImpl;
import py.puchi.platzigram.posts.PostsTaskListener;

/**
 * Created by Alejandro on 22/1/2017.
 */

public class PostsInteractorImpl implements PostsInteractor {

    PostsRepository postsRepository;

    public PostsInteractorImpl(PostsTaskListener listener) {

        this.postsRepository = new PostsRepositoryImpl(listener);
    }

    @Override
    public void getPosts() {

        postsRepository.getPosts();
    }
}
