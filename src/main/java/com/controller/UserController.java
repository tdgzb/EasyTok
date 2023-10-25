package com.controller;
import com.auth.TokenManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;;
import com.domain.User;
import com.result.CommonResult;
import com.service.UserService;
import com.model.vo.UserLoginVo;
import com.utlis.Sha1Cipher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

/**
 * @Description:
 * @DATE: 2023/10/25  15:38
 * @Author: 2257736387@qq.com
 * @Version: 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    TokenManager tokenManager;
    @Resource
    private UserService userService;

    @PostMapping("/register")
    public CommonResult register(@RequestParam("username") String username, @RequestParam("password") String passwrod){
        User user=new User();
        user.setUsername(username);
        user.setPassword(Sha1Cipher.encryptBySha1(passwrod));
        userService.save(user);
        return CommonResult.operateSuccess();
    }

    @RequestMapping("/login")
    public CommonResult<UserLoginVo> login(@RequestParam("username") String username, @RequestParam("password") String passwrod) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername,username).eq(User::getPassword,Sha1Cipher.encryptBySha1(passwrod));
        User user = userService.getOne(wrapper);
        if (null==user){
            return CommonResult.operateFailWithMessage("用户名或密码错误");
        }
        String token = tokenManager.generateToken(user);
        return CommonResult.operateSuccess(new UserLoginVo(token));
    }
}
