package com.tensquare.article.service;

import com.tensquare.article.dao.CommentRepository;
import com.tensquare.article.po.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.IdWorker;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private IdWorker idWorker;

    public void add(Comment comment) {
        comment.setId(idWorker.nextId() + "");
        commentRepository.save(comment);
    }

    public List<Comment> findByArticleid(String articleid) {
        return commentRepository.findByArticleid(articleid);
    }
}
