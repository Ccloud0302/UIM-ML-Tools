package com.markerhub.service;

import com.markerhub.entity.Sservice;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 */
public interface SserviceService extends IService<Sservice> {
    void addSservice(Sservice sservice);
    void deleteSservice(Integer id);
}
