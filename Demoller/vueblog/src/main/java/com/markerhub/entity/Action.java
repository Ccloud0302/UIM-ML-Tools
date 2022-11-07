package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Action {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String name;
    private String type;
    @TableField(exist = false)
    private List<Param> params;
    @TableField(exist = false)
    private List<String> res;
    private String des;
    private int domainId;
    private int nodeId;
    private String cause;
    private String source;

}
