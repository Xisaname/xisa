package life.majiang.community.community.controller;

import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;//自动装配

    @GetMapping("/")//主页面，在第一层，当有重定向到"/"时,会重新执行该controller
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();//获得全部的cookie
        for (Cookie cookie: cookies
             ) {
            if(cookie.getName().equals("token")){//找到叫token的cookie
                String token=cookie.getValue();//获取value值
                User user=userMapper.findByToken(token);//通过数据库查找，找到相关的用户信息
                if(user!=null){
                    request.getSession().setAttribute("user",user);//存储到session中持久化
                }
                break;
            }
        }
        return "index";//回到页面
    }
}
