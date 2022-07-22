package cn.tedu.knows.portal.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class QuestionVO {
    @NotBlank(message = "标题不能为空")
    @Pattern(regexp = "^.{3,50}$",message = "标题需要3到50个字符")
    private String title;
    @NotEmpty(message = "至少选择一个标签")  //专门用于判断集合和数组非空的注解
    private String[] tagNames={};
    @NotEmpty(message = "至少选择一名讲师")
    private String[] teacherNicknames = {};
    @NotBlank(message = "问题内容不能为空")
    private String content;
}
