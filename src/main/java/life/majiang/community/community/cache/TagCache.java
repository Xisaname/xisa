package life.majiang.community.community.cache;

import life.majiang.community.community.dto.TagDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagCache {
    public static List<TagDTO> get(){
        List<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js","php","python","java","c#","cpp","c","ruby","spark","css","html","html5","node.js","golang","objective-c","typescript","shell","swift","sass","bash","less","asp.net","lua","scala","coffeescript","actionscript","rust","erlang","perl"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("laravel","spring","express","django","flask","yii","ruby-on-rails","tornado","koa","struts"));
        tagDTOS.add(framework);

        TagDTO sever =new TagDTO();
        sever.setCategoryName("服务器");
        sever.setTags(Arrays.asList("linux","nginx ","docker ","apache ","ubuntu ","centos ","缓存 tomcat  ","负载均衡 ","unix ","hadoop ","windows-server"));
        tagDTOS.add(sever);

        TagDTO db =new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql","redis ","mongodb","sql","oracle ","nosql ","memcached ","sqlserver","postgresql ","sqlite"));
        tagDTOS.add(db);

        TagDTO tool =new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git","github","visual-studio-code","vim","sublime-text","xcode","intellij-idea","eclipse","maven","ide","svn","visual-studio","atom","emacs","textmate","hg"));
        tagDTOS.add(tool);

        return tagDTOS;
    }
}
