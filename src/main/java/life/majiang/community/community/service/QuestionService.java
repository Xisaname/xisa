package life.majiang.community.community.service;

import life.majiang.community.community.dto.PaginationDTO;
import life.majiang.community.community.dto.QuestionDTO;
import life.majiang.community.community.mapper.QuestionMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.Question;
import life.majiang.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    //将问题放到了页面层
    public PaginationDTO list(Integer page, Integer size) {
        Integer totalCount = questionMapper.count();//获取总的问题数，以便进行页面划分
        Integer totalPage;
        //计算需要的总页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if(page<1)
            page=1;
        if(page>totalPage)
            page=totalPage;
        Integer offset = size * (page - 1);
        //获取所有问题
        List<Question> questions = questionMapper.list(offset, size);
        //将问题与用户组合起来形成一个对象
        List<QuestionDTO> questionDTOS = new ArrayList<>();

        PaginationDTO paginationDTO = new PaginationDTO();

        for (Question question : questions) {//该循环即是将用户与其所提出的问题一一对应
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);//将两者相同的部分复制到DTO
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOS);//将所有问题放到页面层次
        paginationDTO.setPagination(totalCount, page, size,totalPage);//设置页面
        return paginationDTO;
    }
}
