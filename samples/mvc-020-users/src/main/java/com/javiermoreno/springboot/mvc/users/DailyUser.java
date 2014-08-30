/*
 * Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */

package com.javiermoreno.springboot.mvc.users;


import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Hex;

/**
 *
 * @author ciberado
 */
@Entity
@Table(name="dailyusers",
       indexes = {@Index(columnList = "email")}) 
public class DailyUser implements UserDetails, Serializable{

    private static final long serialVersionUID = 1L;
    
    public enum RoleType {
        CONFIRMATION_PENDING,
        REGULAR,
        ADMIN
    };
    
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @Column(columnDefinition = "varchar(128)", length = 128, nullable = true, unique = true)
    private String email;
    
    @Column(columnDefinition = "char(32)", length = 32, nullable = false, unique = false)
    private String passwordHash;
    
    @Temporal(TemporalType.DATE)
    private Date birthday;
    
    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = {@JoinColumn(name="user_id")})
    private List<RoleType> roles = new ArrayList<>();

    public DailyUser() {
    }

    public DailyUser(String email, Date birthday) {
        this.email = email;
        this.birthday = birthday;
    }
    
    public void setPlainTextPassword(String plainTextPassword)  {
        try {
            byte[] bytesOfMessage = plainTextPassword.getBytes("UTF-8");
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            this.passwordHash = new String(Hex.encode(thedigest));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
                
    }    
    
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roleNames = new ArrayList<>();
        for (RoleType current : roles) roleNames.add(current.name());
        return AuthorityUtils.createAuthorityList((String[]) roleNames.toArray());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(List<RoleType> roles) {
        this.roles = roles;
    }

    public List<RoleType> getRoles() {
        return roles;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.email);
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
        final DailyUser other = (DailyUser) obj;
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CustomUserDetails{" + "id=" + id + ", username=" + ", passwordHash=" + passwordHash + ", roles=" + roles + ", email=" + email + '}';
    }

    
    
}
