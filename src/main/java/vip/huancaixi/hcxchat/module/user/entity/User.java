package vip.huancaixi.hcxchat.module.user.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author hcx
 * @since 2023-02-25
 */
@Getter
@Setter
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 用户名
     */
    private String username;
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 权限集合
     */
    private String permission;

    /**
     * 扩展信息
     */
    private String other;

    private Integer vip;

}
