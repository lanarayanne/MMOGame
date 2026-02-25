package com.ifpe.mmogame.dto;

public class NewLikeDTO {
    private int postId;
    private int characterId;

    public NewLikeDTO() {
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

}
