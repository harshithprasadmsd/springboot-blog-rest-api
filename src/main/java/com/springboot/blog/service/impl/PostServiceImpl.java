package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper mapper;

    private CategoryRepository categoryRepository;

    public PostServiceImpl(PostRepository postRepository, ModelMapper mapper, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
         Category category  = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category","ID", postDto.getCategoryId()));
        Post post = mapToEntity(postDto);
        post.setCategory(category);

        Post newPost = postRepository.save(post);

//        convert entity to DTO
        PostDto postResponse = mapToDTO(newPost);

        return postResponse;
    }

    @Override
    public PostResponse getAllPosts(int pageNo ,int PageSize,String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, PageSize,sort);
        Page<Post> posts = postRepository.findAll(pageable);
//        get contains from page object
        List<Post> listOfPosts = posts.getContent();
        List<PostDto> content =  posts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {
        Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
//        get post by id from the database
        Category category  = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category","ID", postDto.getCategoryId()));

        Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        post.setCategory(category);
        post.setDescription(postDto.getDescription());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        Post post= postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));
        postRepository.delete(post);
    }

    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {
        Category category  = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category","ID", categoryId));

        List<Post> posts= postRepository.findByCategoryId(categoryId);
         return posts.stream().map((post -> mapToDTO(post))).collect(Collectors.toList());
    }


    //    convet Entity to DTO
    private PostDto mapToDTO(Post post){
        PostDto postDto = mapper.map(post,PostDto.class);
//        PostDto postDto = new PostDto();
//        postDto.setTitle(post.getTitle());
//        postDto.setId(post.getId());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }

//    convet DTO to Entity
    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto,Post.class);
//        Post post = new Post();
//        post.setId(postDto.getId());
//        post.setContent(postDto.getContent());
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
        return post;
    }
}
