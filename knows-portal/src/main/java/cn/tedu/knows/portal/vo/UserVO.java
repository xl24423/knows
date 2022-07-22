package cn.tedu.knows.portal.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserVO {
    private Integer id;
    private String username;
    private String nickname;
    // 问题数量
    private int questions;
    // 收藏数量
    private int collections;

}
