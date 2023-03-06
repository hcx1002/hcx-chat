package vip.huancaixi.hcxchat.module.user.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserData  implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String username;
    private String avatar;
    private String other;
    private Integer vip;
}
