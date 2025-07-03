package org.example.agvinhand.entity;

import lombok.Data;

@Data
public class AgvConfig {
    private Long id;
    private String host;
    private Integer drivePort;
    private Integer analysisPort;
    private String cloudUrl;
    private String cam1;
    private String cam2;
    private String cam3;
    private String cam4;
    private String username1;
    private String username2;
    private String username3;
    private String username4;
    private String password1;
    private String password2;
    private String password3;
    private String password4;
    private Boolean deleteFlag;
}