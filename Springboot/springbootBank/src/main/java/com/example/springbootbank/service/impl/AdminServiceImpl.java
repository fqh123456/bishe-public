package com.example.springbootbank.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springbootbank.common.Constants;
import com.example.springbootbank.controller.dto.AdminDto;
import com.example.springbootbank.entity.Admin;
import com.example.springbootbank.entity.Menu;
import com.example.springbootbank.exception.ServiceException;
import com.example.springbootbank.mapper.AdminMapper;
import com.example.springbootbank.mapper.RoleMapper;
import com.example.springbootbank.mapper.RoleMenuMapper;
import com.example.springbootbank.service.IAdminService;
import com.example.springbootbank.service.IMenuService;
import com.example.springbootbank.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-01-16
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public AdminDto login(AdminDto adminDto) {
//        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
//        //前一个参数是数据库表的表头，后一个数据是前端传来的参数
//        queryWrapper.eq("name",userDto.getUsername());
//        queryWrapper.eq("pass",userDto.getPassword());
        Admin one =getUserInfo(adminDto);
//        try{
//            one =getOne(queryWrapper);
//        }catch (Exception e){
//            throw new ServiceException(Constants.CODE_500,"系统错误");
//        }
        if(one!=null){
            BeanUtil.copyProperties(one,adminDto,true);

            //设置token
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            adminDto.setToken(token);

            String role =one.getRole();


            List<Menu> roleMenus = getRoleMenus(role);
            adminDto.setMenus(roleMenus);
            return adminDto;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    @Override
    public Admin register(AdminDto adminDto) {
        Admin one = this.getUserInfo(adminDto);
        if(one == null){
            one = new Admin();
            BeanUtil.copyProperties(adminDto,one,true);
            save(one);
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名已存在");
        }
        return one;
    }

    private Admin getUserInfo(AdminDto adminDto){
        QueryWrapper<Admin> queryWrapper=new QueryWrapper<>();
        //前一个参数是数据库表的表头，后一个数据是前端传来的参数
        queryWrapper.eq("username",adminDto.getUsername());
        queryWrapper.eq("password",adminDto.getPassword());
        Admin one;
        try{
            one = getOne(queryWrapper);
        }catch (Exception e){
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }



}
