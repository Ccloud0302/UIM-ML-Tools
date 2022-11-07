package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class StateAction {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private int domainId;
    private int nodeId;
    private String mode;
    private String stateName;
    private String des;
    private int num;
}
