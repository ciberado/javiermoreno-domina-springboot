/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.rest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author ciberado
 */
@Entity
@Table(name="users", 
       indexes = {@Index(columnList = "username"), @Index(columnList = "email")}) 
@NamedQueries({
    @NamedQuery(name = "findByUsername", query = "select u from CustomUserDetails as u where u.username = :username"),
    @NamedQuery(name = "findByEmail", query = "select u from CustomUserDetails as u where u.email = :email")
})
public class CustomUserDetails implements UserDetails, Serializable{

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    @Column(columnDefinition = "varchar", length = 64, nullable = false, unique = true)
    private String username;
    @Column(columnDefinition = "char", length = 32, nullable = false, unique = false)
    private String md5password;
    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = {@JoinColumn(name="user_id")})
    private List<String> roles = new ArrayList<>();
    @Column(columnDefinition = "varchar", length = 128, nullable = true, unique = false)
    private String email;

    public CustomUserDetails() {
    }

    public CustomUserDetails(int id, String username, String md5password, String email) {
        this.id = id;
        this.username = username;
        this.md5password = md5password;
        this.email = email;
    }
    
    
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList((String[]) roles.toArray());
    }

    @Override
    public String getPassword() {
        return md5password;
    }

    @Override
    public String getUsername() {
        return md5password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomUserDetails other = (CustomUserDetails) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" + "id=" + id + ", username=" + username + ", md5password=" + md5password + ", roles=" + roles + ", email=" + email + '}';
    }

    
    
}
