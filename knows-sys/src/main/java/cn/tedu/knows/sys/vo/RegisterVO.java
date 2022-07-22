package cn.tedu.knows.sys.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
public class RegisterVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotBlank(message = "邀请码不能为空")
    private String inviteCode;  //邀请码
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9][0-9]{9}$",message = "请输入正确的手机号")
    private String phone; //电话
    @NotBlank(message = "昵称不能为空")
    @Pattern(regexp = "^.{2,20}$",message = "请输入2到20个字符的昵称")
    private String nickname; //昵称
    @NotBlank(message = "密码不能为空")
    @Pattern(regexp = "^\\w{6,20}$",message = "密码必须由6到20个数字字母下划线组成")  // \\w 表示字符数字下划线
    private String password; //密码
    @NotBlank(message = "请确认密码")
    private String confirm; //确认密码


}
