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
public class Step {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String step_title;
    private String step_time;
    private int step_num;
    private Boolean inputVisible;
    private Boolean timeInputVisible;
    private Boolean step_explain;
    private String inputValue;
    private int domainId;
    private int nodeId;
}
