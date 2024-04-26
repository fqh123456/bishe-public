package com.example.springbootbank.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springbootbank.controller.dto.AdminDto;
import com.example.springbootbank.entity.Admin;

public interface IAdminService extends IService<Admin> {

    AdminDto login(AdminDto adminDto);

    Admin register(AdminDto adminDto);
}
