package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
@Schema(
        description = "PostDto model Information"
)
public class PostDto {
    private Long id;
//    title should not be empty or null
//    should have atleast 2 character
    @Schema(
            description = "Blog Post Title"
    )
    @NotEmpty
    @Size(min=2,message = "post title should have atleast 2 characters")
    private String title;

    @Schema(
            description = "Blog Post Description"
    )
    @NotEmpty
    @Size(min=10,message = "post decription should have atleast 10 characters")
    private String description;

//    content should not be empty
    @Schema(
        description = "Blog Post Content"
    )
    @NotEmpty
    private String content;
    @Schema(
            description = "Blog Post Comments"
    )
    private Set<CommentDto> comments;

    private Long categoryId;
}
