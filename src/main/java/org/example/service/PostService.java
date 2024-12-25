package org.example.service;

import org.example.model.Post;
import org.example.model.CommunityActivityScore;
import org.example.repository.PostRepository;
import org.example.repository.CommunityActivityScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommunityActivityScoreRepository scoreRepository;

    @Autowired
    public PostService(PostRepository postRepository,
                       CommunityActivityScoreRepository scoreRepository) {
        this.postRepository = postRepository;
        this.scoreRepository = scoreRepository;
    }

    public Post createPost(Post post, String registrationCode) {
        post.setRegistrationCode(registrationCode);
        post.setCreatedAt(new Date());
        Post savedPost = postRepository.save(post);

        CommunityActivityScore score = scoreRepository.findByRegistrationCode(registrationCode)
                .orElseGet(() -> {
                    CommunityActivityScore newScore = new CommunityActivityScore();
                    newScore.setRegistrationCode(registrationCode);
                    return newScore;
                });

        score.incrementPostCount();
        scoreRepository.save(score);

        return savedPost;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }



    public Post updatePost(Long id, Post updatedPost) {
        if (postRepository.existsById(id)) {
            updatedPost.setId(id);
            return postRepository.save(updatedPost);
        }
        return null;
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}