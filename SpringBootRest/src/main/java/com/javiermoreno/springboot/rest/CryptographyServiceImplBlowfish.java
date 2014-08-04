/* Copyright 2014, Javier Moreno.
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 * And have fun ;-)
 */
package com.javiermoreno.springboot.rest;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import org.springframework.security.crypto.codec.Base64;

/**
 *
 * @author ciberado
 */
public class CryptographyServiceImplBlowfish implements CryptographyService {

    private final Cipher cipherEncrypt;
    private final Cipher cipherDecrypt;
    private final Key key;
   
    public CryptographyServiceImplBlowfish() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        keyGenerator.init(128);
        key = keyGenerator.generateKey();
        cipherEncrypt = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipherEncrypt.init(Cipher.ENCRYPT_MODE, key);
        cipherDecrypt = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
        cipherDecrypt.init(Cipher.DECRYPT_MODE, key);
    }

    @Override
    public String encryptToBase64(String message) {
        try {
            byte[] ciphertext = cipherEncrypt.doFinal(message.getBytes("UTF8"));
            String result = new String(Base64.encode(ciphertext), "UTF8");
            return result;
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String decryptFromBasea64(String messageBase64) {
        try {
            byte[] data = Base64.decode(messageBase64.getBytes("UTF8"));
            byte[] decryptedText = cipherDecrypt.doFinal(data);
            String result = new String(decryptedText, "UTF8");
            return result;
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }    
}
