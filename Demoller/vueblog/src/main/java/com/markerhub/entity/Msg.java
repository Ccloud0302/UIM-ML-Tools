package com.markerhub.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Msg {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String name;
    private String type;
    private String triggerAction;
    private String causeContext;
    private String causeAction;
    private int domainId;
    private int sserviceId;


}
