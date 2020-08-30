package life.majiang.community.community.controller;

import life.majiang.community.community.dto.AccessTokenDTO;
import life.majiang.community.community.dto.GithubUser;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.User;
import life.majiang.community.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller//控制器，用来控制页面的显示，将前后端数据进行连接
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;//将对象自动装配，省去了初始化(GET,SET)的过程
    @Value("${github.client.id}")
    private String clientId;//赋值
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")//返回到callback这一层
    public String callback(@RequestParam(name="code") String code,//获取到code并赋值给code变量
                            @RequestParam(name="state") String state,
                           HttpServletResponse response//设置响应，用来获取用户信息
                           ){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();//服务器和浏览器之间的信息传递对象
        accessTokenDTO.setCode(code);//给予信息
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);//用code来获取accesstoken
        GithubUser githubUser = githubProvider.getUser(accessToken);//用accesstoken来获取用户信息
        if(githubUser!=null){
            //登陆成功，写cookie和session
            User user = new User();//初始化数据库模型
            String token = UUID.randomUUID().toString();//为浏览器手动设置token
            user.setToken(token);//给予信息
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());//用的是格林尼治时间
            user.setGmtModified(user.getGmtCreate());
            user.setBio(githubUser.getBio());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userMapper.insert(user);//插入到数据库中
            response.addCookie(new Cookie("token",token));//添加cookie，用于登录持久化
            return "redirect:/";//重定向，在显示前一页面的同时，"/"即是主页面index。这样的做法隐藏了code相关信息。
        }else{
            //登陆失败,重新登陆
            return "redirect:/";
        }
    }
}
