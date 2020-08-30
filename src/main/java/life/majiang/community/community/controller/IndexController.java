package life.majiang.community.community.controller;

import life.majiang.community.community.dto.QuestionDTO;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.Question;
import life.majiang.community.community.model.User;
import life.majiang.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;//自动装配

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")//主页面，在第一层，当有重定向到"/"时,会重新执行该controller
    public String index(HttpServletRequest request,
                        Model model){
        Cookie[] cookies = request.getCookies();//获得全部的cookie
        if(cookies!=null&&cookies.length!=0)
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
        List<QuestionDTO>questionDTOList=questionService.list();
        model.addAttribute("questions",questionDTOList);
        return "index";//回到页面
    }
}
