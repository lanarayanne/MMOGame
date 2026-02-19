package com.ifpe.mmogame.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ifpe.mmogame.entities.CommentPost;

public interface CommentRepositoy extends JpaRepository<CommentPost, Integer> {
    public List<CommentPost> findByPostId(int postId);

    public List<CommentPost> findByCharacterId(int CharacterId);

}
