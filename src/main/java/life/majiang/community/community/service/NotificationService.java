package life.majiang.community.community.service;

import life.majiang.community.community.dto.NotificationDTO;
import life.majiang.community.community.dto.PaginationDTO;
import life.majiang.community.community.enums.NotificationStatusEnum;
import life.majiang.community.community.enums.NotificationTypeEnum;
import life.majiang.community.community.exception.CustomizeErrorCode;
import life.majiang.community.community.exception.CustomizeException;
import life.majiang.community.community.mapper.NotificationMapper;
import life.majiang.community.community.mapper.UserMapper;
import life.majiang.community.community.model.Notification;
import life.majiang.community.community.model.NotificationExample;
import life.majiang.community.community.model.User;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO list(Long userId, Integer page, Integer size) {
        PaginationDTO<NotificationDTO> paginationDTO=new PaginationDTO<>();
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);//获取总的问题数，以便进行页面划分
        Integer totalPage;
        //计算需要的总页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1)
            page = 1;
        if (page > totalPage)
            page = totalPage;
        paginationDTO.setPagination(totalCount,page,size,totalPage);
        Integer offset = size * (page - 1);
        //获取所有问题
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        RowBounds rowBounds = new RowBounds(offset, size);//设置表的格式，这里设置的是每7行数据为一页，数据库中同样如此
        List<Notification> notifications = notificationMapper.selectByExampleWithRowbounds(example, rowBounds);
        if(notifications.size() == 0){
            return paginationDTO;
        }

        //将问题与用户组合起来形成一个对象
        List<NotificationDTO> notificationDTOS = new ArrayList<>();
        for (Notification notification : notifications) {
            NotificationDTO notificationDTO=new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));

            notificationDTOS.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOS);//将所有问题放到页面层次
        return paginationDTO;
    }

    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        if(notification.getReceiver().longValue() != user.getId().longValue()){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);
        NotificationDTO notificationDTO=new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));//标记为已读
        return notificationDTO;
    }
}
