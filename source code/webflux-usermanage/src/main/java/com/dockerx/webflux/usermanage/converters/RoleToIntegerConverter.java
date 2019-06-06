package com.dockerx.webflux.usermanage.converters;

import com.dockerx.webflux.usermanage.domain.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * @author Author  知秋
 * @email fei6751803@163.com
 * @time Created by Auser on 2018/8/7 1:01.
 */
@Component
public class RoleToIntegerConverter implements Converter<Role, Integer> {
    @Override
    public Integer convert(Role role) {
        return role.getValue();
    }
}
