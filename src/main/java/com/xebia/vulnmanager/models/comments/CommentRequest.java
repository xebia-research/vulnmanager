package com.xebia.vulnmanager.models.comments;

import java.io.Serializable;

public class CommentRequest implements Serializable {
    private String userName;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
