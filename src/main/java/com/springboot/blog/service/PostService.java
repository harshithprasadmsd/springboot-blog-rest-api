package com.springboot.blog.service;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto);
    public PostResponse getAllPosts(int pageNo , int PageSize, String sortBy, String sortDir);

    PostDto getPostById(long id);

    PostDto updatePost(PostDto postDto , Long id);

    void deletePost(Long id);

    List<PostDto> getPostsByCategory(Long categoryId);
}
