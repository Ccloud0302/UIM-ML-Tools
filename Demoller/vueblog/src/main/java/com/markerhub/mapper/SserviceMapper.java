package com.markerhub.mapper;

import com.markerhub.entity.Sservice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 */

public interface SserviceMapper extends BaseMapper<Sservice> {
    void addSservice(Sservice sservice);
    void deleteSservice(Integer id);

}
