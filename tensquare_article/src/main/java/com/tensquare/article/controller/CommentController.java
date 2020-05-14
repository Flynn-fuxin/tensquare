package com.tensquare.article.controller;

import com.tensquare.article.po.Comment;
import com.tensquare.article.service.CommentService;
import constants.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import dto.ResultDTO;

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    public ResultDTO save(@RequestBody Comment comment) {
        commentService.add(comment);
        return new ResultDTO(true, StatusCode.OK, "提交成功");
    }

    @RequestMapping(value = "/article/{articleid}", method = RequestMethod.GET)
    public ResultDTO findByArticleid(@PathVariable String articleid) {
        return new ResultDTO(true, StatusCode.OK, "查询成功",commentService.findByArticleid(articleid));
    }
}