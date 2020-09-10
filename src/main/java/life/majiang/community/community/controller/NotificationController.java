package life.majiang.community.community.controller;

import life.majiang.community.community.dto.NotificationDTO;
import life.majiang.community.community.enums.NotificationTypeEnum;
import life.majiang.community.community.model.User;
import life.majiang.community.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    //消息通知逻辑体
    public String profile(@PathVariable(name = "id") Long id,
                          Model model,
                          HttpServletRequest request) {
        User user=(User) request.getSession().getAttribute("user");
        if(user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.read(id,user);//根据id和user来获得所有通知
        if(NotificationTypeEnum.REPLY_COMMENT.getType()==notificationDTO.getType()||
                NotificationTypeEnum.REPLY_QUESTION.getType()==notificationDTO.getType()) {//当用户点击某条问题时，重定向到该问题
            return "redirect:/question/" + notificationDTO.getOuterId();
        }
        else {
            return "redirect:/";
        }
    }
}
