package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StepTag {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String name;
    private String type;
    private String list;
    private int stepNum;
    private int domainId;
    private int nodeId;
}
