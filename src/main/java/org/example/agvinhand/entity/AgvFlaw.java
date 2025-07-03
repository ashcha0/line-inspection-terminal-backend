package org.example.agvinhand.entity;

import lombok.Data;
import java.util.Date;

@Data
public class AgvFlaw {
    private Long id;
    private Long taskId;
    private Integer round;
    private String flawType;
    private String flawName;
    private String flawDesc;
    private Double flawDistance;
    private String flawImage;
    private String flawImageUrl;
    private String flawRtsp;
    private Boolean shown;
    private Boolean confirmed;
    private Boolean uploaded;
    private Date createTime;
    private String remark;
    private Double flawLength;
    private Double flawArea;
    private String level;
    private Integer countNum;
    private Boolean deleteFlag;
}