package org.example.agvinhand.entity;

import lombok.Data;
import java.util.Date;

@Data
public class AgvStatus {
    private Long id;
    private String sysTime;
    private Boolean isRunning;
    private Double currentPosition;
    private Date createTime;
}