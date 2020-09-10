package life.majiang.community.community.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")//获取出错路径
public class CustomizeErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request,
                                  Model model) {
        HttpStatus status = getStatus(request);
        if(status.is4xxClientError()){//请求出错
            model.addAttribute("message","你请求出错了，要不然换个方式？");
        }
        if(status.is5xxServerError()){//服务器出错
            model.addAttribute("message","服务器也没吹空调，热晕啦，要不然你等它凉快一下？");
        }
        return (new ModelAndView("error"));//返回到出错页面即error.html
    }

    private HttpStatus getStatus(HttpServletRequest request) {//返回出错信息
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;//服务出错
        }
        try {
            return HttpStatus.valueOf(statusCode);//注入出错代码，获取出错信息
        }
        catch (Exception ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
