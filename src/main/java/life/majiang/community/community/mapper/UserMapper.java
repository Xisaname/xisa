package life.majiang.community.community.mapper;

import life.majiang.community.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,BIO,avatar_url) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{bio},#{avatarUrl})")
    public void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);//当形参不是对象时，需要指定参数，即用@Param("token")

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);

    @Select("select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set name = #{name},token = #{token},gmt_modified = #{gmtModified},avatar_url = #{avatarUrl} where id = #{id}")
    void update(User user);
}
