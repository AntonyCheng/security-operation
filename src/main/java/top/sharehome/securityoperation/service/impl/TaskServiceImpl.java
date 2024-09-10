package top.sharehome.securityoperation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sharehome.securityoperation.mapper.TaskMapper;
import top.sharehome.securityoperation.model.entity.Task;
import top.sharehome.securityoperation.service.TaskService;

/**
 * 任务服务实现类
 *
 * @author AntonyCheng
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

}




