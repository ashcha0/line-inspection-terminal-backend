package org.example.agvinhand.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.agvinhand.entity.AgvFlaw;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface AgvFlawMapper extends BaseMapper<AgvFlaw> {

    /**
     * 根据任务ID查询缺陷列表
     */
    @Select("SELECT * FROM agv_flaw WHERE task_id = #{taskId} AND delete_flag = 0")
    List<AgvFlaw> selectByTaskId(@Param("taskId") Long taskId);
}