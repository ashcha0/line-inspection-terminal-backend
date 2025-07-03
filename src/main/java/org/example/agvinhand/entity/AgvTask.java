package org.example.agvinhand.entity;

import lombok.Data;
import java.util.Date;

@Data
public class AgvTask {
    private Long id;
    private String taskCode;
    private String taskName;
    private String startPos;
    private String taskTrip;
    private String creator;
    private String executor;
    private Date execTime;
    private Date endTime;
    private Date createTime;
    private String taskStatus;
    private Integer round;
    private Boolean uploaded;
    private String remark;
    private Long cloudTaskId;
    private Boolean deleteFlag;
}