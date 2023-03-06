package vip.huancaixi.hcxchat.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.huancaixi.hcxchat.module.user.entity.User;
import vip.huancaixi.hcxchat.module.user.mapper.UserMapper;
import vip.huancaixi.hcxchat.module.user.service.IUserService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hcx
 * @since 2023-02-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
