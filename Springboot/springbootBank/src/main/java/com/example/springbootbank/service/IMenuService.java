package com.example.springbootbank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootbank.entity.Menu;

import java.util.List;

/**
 * <p>
 * 112 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-27
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
