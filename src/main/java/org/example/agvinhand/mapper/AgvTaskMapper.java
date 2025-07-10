package org.example.agvinhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.agvinhand.entity.AgvTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AgvTaskMapper extends BaseMapper<AgvTask> {

    /**
     * 根据cloudTaskId查询任务
     */
    @Select("SELECT * FROM agv_task WHERE cloud_task_id = #{cloudTaskId} AND delete_flag = 0")
    AgvTask selectByCloudTaskId(@Param("cloudTaskId") Long cloudTaskId);
}