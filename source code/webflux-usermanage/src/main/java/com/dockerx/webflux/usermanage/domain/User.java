package com.dockerx.webflux.usermanage.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * @author Author  知秋
 * @email fei6751803@163.com
 * @time Created by Auser on 2018/8/7 0:52.
 */
@Document(collection = "users")
@JsonIgnoreProperties(value = {"authorities", "name",
        "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "username"})
@ToString
@Builder
public class User implements UserDetails {

    @Id
    private String id;

    @NotNull
    @Length(min = 3, max = 30)
    private String username;

    @NotNull
    @Length(min = 5, max = 30)
    private String password;

    @NotNull
    @Length(min = 1, max = 30)
    private String fname;

    @NotNull
    @Length(min = 1, max = 30)
    private String lname;

    @Builder.Default
    private Role role = Role.CUSTOMER;

    @Builder.Default
    private boolean active = true;

    public User(String username, String password, String fname, String lname) {
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        role = Role.CUSTOMER;
    }

    public User(String username, String password, String fname, String lname, Role role) {
        this.username = username;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.role = role;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getRole() != null ? getRole().toString() : Role.CUSTOMER.toString());
    }


    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }


}
