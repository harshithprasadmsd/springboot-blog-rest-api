package com.springboot.blog.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class PostDto {
    private Long id;
//    title should not be empty or null
//    should have atleast 2 character

    @NotEmpty
    @Size(min=2,message = "post title should have atleast 2 characters")
    private String title;

    @NotEmpty
    @Size(min=10,message = "post decription should have atleast 10 characters")
    private String description;

//    content should not be empty
    @NotEmpty
    private String content;
    private Set<CommentDto> comments;
}
