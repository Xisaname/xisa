package life.majiang.community.community.service;

import life.majiang.community.community.dto.CommentDTO;
import life.majiang.community.community.enums.CommentTypeEnum;
import life.majiang.community.community.enums.NotificationStatusEnum;
import life.majiang.community.community.enums.NotificationTypeEnum;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import life.majiang.community.community.mapper.*;
import life.majiang.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment,User commentator) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //判断是否是评论类型
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()/* 这个获得的就是2，只不过为了封装性将其保存在了枚举类型中*/) {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            commentMapper.insert(comment);
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            //创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_COMMENT,question.getId());
        } else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            createNotify(comment,question.getCreator(), commentator.getName(), question.getTitle() ,NotificationTypeEnum.REPLY_QUESTION,question.getId());
        }
    }

    private void createNotify(Comment comment, Long reciver, String notifierName, String outerTitle ,NotificationTypeEnum notificationTypeEnum, Long outerId) {
        if(reciver==comment.getCommentator()){
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnum.getType());
        notification.setOuterId(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(reciver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();//抽取事例，用于查询
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());//设置查询限制，相当于where
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);//获得所有回复
        if (comments.size() == 0) {
            return new ArrayList<>();//返回空
        }
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());//当前是comment，返回的是commentator

        //将user获取到，并且建立user和回复的对应关系

        List<Long>userIds=new ArrayList();
        userIds.addAll(commentators);//将所有回复的id单独放到一起
        UserExample userExample = new UserExample();//建立事例
        userExample.createCriteria()
                .andIdIn(userIds);//设置查询限制
        List<User> users = userMapper.selectByExample(userExample);//查询到与回复相关的所有用户

        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));//将回复和user之间建立对应关系，Java8语法

        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {//将所有整合到的回复以及用户属性放到一起，即commentDTOS
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
