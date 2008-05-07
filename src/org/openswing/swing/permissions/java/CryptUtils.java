package org.openswing.swing.permissions.java;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.*;
import java.util.*;


/**
 * <p>Title: OpenSwing Framework</p>
 * <p>Description: Utility class used to crypt and decrypt of bytes.
 * This class can be used by LoginDialog class to encode/decode the password field.
 * This class can be used by any other layer: there are two initializers, based on "getInstance" method;
 * - getInstance() initializes the internal cipher by providing a default internal password
 * - getInstance(String passwd) initializes the internal cipher by providing an application defined password</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class CryptUtils {

  /** unique instance of the class */
  private static CryptUtils cryptUtils = null;

  /** cipher used to encode bytes, based on PBEWithMD5AndDES algorithm */
  private Cipher encodeCipher = null;

  /** cipher used to decode bytes, based on PBEWithMD5AndDES algorithm */
  private Cipher decodeCipher = null;


  private CryptUtils(String passwd) {
    try {
      char[] chars = new char[passwd.length()];
      for(int i=0;i<chars.length;i++)
        chars[i] = passwd.charAt(i);
      PBEKeySpec pbeKeySpec = new PBEKeySpec(chars);
      SecretKeyFactory keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

      // Create PBE encode Cipher
      encodeCipher = Cipher.getInstance("PBEWithMD5AndDES");
      // Salt
         byte[] salt = {
             (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
             (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99
         };

      // Iteration count
      int count = 20;

      // Create PBE parameter set
      PBEParameterSpec encodePbeParamSpec = new PBEParameterSpec(salt, count);

      // Initialize PBE encode Cipher with key and parameters
      encodeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, encodePbeParamSpec);

      // Create PBE decode Cipher
      decodeCipher = Cipher.getInstance("PBEWithMD5AndDES");

      // Create PBE parameter set
      PBEParameterSpec decodePbeParamSpec = new PBEParameterSpec(salt, count);

      // Initialize PBE decode Cipher with key and parameters
      decodeCipher.init(Cipher.DECRYPT_MODE, pbeKey, decodePbeParamSpec);

    }
    catch (NoSuchPaddingException ex) {
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * @return unique instance of this class
   */
  public static CryptUtils getInstance() {
    if (cryptUtils==null)
      cryptUtils = new CryptUtils("openswing");
    return cryptUtils;
  }


  /**
   * @param passwd password used to initialize the encryption/decryption cipher
   * @return unique instance of this class
   */
  public static CryptUtils getInstance(String passwd) {
    if (cryptUtils==null)
      cryptUtils = new CryptUtils(passwd);
    return cryptUtils;
  }


  /**
   * @param clearBytes bytes to encode
   * @return encoded bytes
   */
  public final byte[] encodeBytes(byte[] clearBytes) throws Throwable {
    return encodeCipher.doFinal(clearBytes);
  }


  /**
   * @param encodedBytes bytes to decode
   * @return decoded bytes
   */
  public final byte[] decodeBytes(byte[] encodedBytes) throws Throwable {
    return decodeCipher.doFinal(encodedBytes);
  }


  /**
   * @param clearText text to encode
   * @return encoded text as bytes
   */
  public final byte[] encodeText(String clearText) throws Throwable {
    return encodeCipher.doFinal(clearText.getBytes());
  }


  /**
   * @param encodedBytes bytes to decode as text
   * @return decoded text
   */
  public final String decodeText(byte[] encodedBytes) throws Throwable {
    return new String(decodeCipher.doFinal(encodedBytes));
  }



}
