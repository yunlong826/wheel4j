package com.wheel.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jack_yun
 * @version 1.0
 * @description:
 * @date 2022/7/30 12:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRoleDto {
    private String account;
    private String roleName;
}
