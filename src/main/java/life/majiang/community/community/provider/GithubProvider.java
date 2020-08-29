package life.majiang.community.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.community.dto.AccessTokenDTO;
import life.majiang.community.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component//把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");//初始化
        OkHttpClient client = new OkHttpClient();//用来获取http请求
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));//请求主体
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")//请求地址
                .post(body)//将主体放入
                .build();//将主体赋值
        try (Response response = client.newCall(request).execute()) {//构建响应对象
            String string = response.body().string();//将主题内容字符串化
            String token = string.split("&")[0].split("=")[1];//将字符串分割并取得token
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public GithubUser getUser(String access_Token){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+access_Token)//将token写入并取得user信息
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);//将字符串对象化
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }

}
