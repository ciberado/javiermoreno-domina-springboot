/* Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.rest;

import java.util.Objects;

/**
 *
 * @author ciberado
 */
public class Token {
    private static final String TOKEN_PART_SEPARATOR = "#";
    
    // TODO: Investigar @Configurable para realizar el weaving de esta dependencia.
    private CryptographyService cryptoService;
    private String username;
    private String ip;
    private long expirationTime;

    public Token(CryptographyService cryptoService, String username, String ip, long ttlInSeconds) {
        this.cryptoService = cryptoService;
        this.expirationTime = System.currentTimeMillis() + ttlInSeconds * 1000;
        this.username = username;
        this.ip = ip;
    }

    public Token(CryptographyService cryptoService, String encryptedBase64Token) {
        String token = cryptoService.decryptFromBasea64(encryptedBase64Token);
        String[] parts = token.split(TOKEN_PART_SEPARATOR);
        this.username = parts[0];
        this.ip = parts[1];
        this.expirationTime = Long.parseLong(parts[2]);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expirationTime;
    }
    
    public String getEncryptedToken() {
        String serialization = username + TOKEN_PART_SEPARATOR + ip + TOKEN_PART_SEPARATOR + expirationTime;
        String encrypted = cryptoService.encryptToBase64(serialization);
        return encrypted;
    }

    public String getUsername() {
        return username;
    }

    public String getIp() {
        return ip;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.username);
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
        final Token other = (Token) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Token{" + "username=" + username + ", ip=" + ip + ", expirationTime=" + expirationTime + '}';
    }
 
    
    
}
