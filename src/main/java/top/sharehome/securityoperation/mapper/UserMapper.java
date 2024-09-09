package top.sharehome.securityoperation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sharehome.securityoperation.model.entity.User;

/**
 * 用户Mapper类
 *
 * @author AntonyCheng
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}