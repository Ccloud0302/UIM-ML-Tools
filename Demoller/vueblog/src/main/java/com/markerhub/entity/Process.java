package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Process {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String name;
    private String type;
    private boolean descVisible;
    private int stepNum;
    private String des;
    private int domainId;
    private int nodeId;
}
