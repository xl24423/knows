package cn.tedu.knows.portal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class CommentVO {
    @NotNull(message = "评论id不能为空")
    private Integer answerId;
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
