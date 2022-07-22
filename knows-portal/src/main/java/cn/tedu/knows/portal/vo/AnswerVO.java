package cn.tedu.knows.portal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class AnswerVO implements Serializable {
// NotBlank 用在字符串
// NotNull 用在对象上
// NotEmpty 用在集合上
    @NotNull(message = "问题id不能为空")
    private Integer questionId;
    @NotBlank(message = "问题内容不能为空")
    private String content;

}
