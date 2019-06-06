package com.dockerx.webflux.usermanage.converters;


import com.dockerx.webflux.usermanage.domain.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Author  知秋
 * @email fei6751803@163.com
 * @time Created by Auser on 2018/8/7 0:57.
 */
@Component
public class IntegerToRoleConverter implements Converter<Integer,Role> {
    @Override
    public Role convert(Integer integer) {
        for (Role role : Role.values()) {
            if (role.getValue() == integer) {
                return role;
            }
        }
        return null;
    }
}
