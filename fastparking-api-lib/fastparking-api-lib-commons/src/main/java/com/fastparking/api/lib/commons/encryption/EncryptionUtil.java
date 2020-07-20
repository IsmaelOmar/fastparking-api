package com.fastparking.api.lib.commons.encryption;

import com.fastparking.api.lib.commons.exceptions.FastParkingApplicationException;
import org.slf4j.Logger;
import org.springframework.util.ResourceUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import com.fastparking.api.lib.commons.util.FastParkingUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import static com.fastparking.api.lib.commons.constants.DataConstants.TECHNICAL_ERROR;
import static org.slf4j.LoggerFactory.getLogger;

public class EncryptionUtil {

    private static SecretKeySpec secretKeySpec;
    private static byte[] key;


    private static final Logger LOGGER = getLogger(EncryptionUtil.class);

    public static void setKey(String myKey) throws FastParkingApplicationException {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKeySpec = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
           LOGGER.error("Algorithm chosen for encryption does not exist : " + e);
           throw new FastParkingApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), TECHNICAL_ERROR);
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.error("Algorithm chosen for encryption is not supported  : " + e);
            throw new FastParkingApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), TECHNICAL_ERROR);
        }
    }

    private String readEncryptionKeyFromFile() throws FastParkingApplicationException {
        return this.getContentOfResourceFile("key.key");
    }

    public String getContentOfResourceFile(String resourceFile) {
        String val = "";
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceFile);

            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

            // reads each line
            String l;
            while((l = r.readLine()) != null) {
                val = val + l;
            }
            inputStream.close();
        } catch (IOException e) {
            LOGGER.error("Unable to read encryption key from file: " + e.getMessage());
        }

        return val;
    }

    public String encrypt(String strToEncrypt) throws FastParkingApplicationException {
        try
        {
            EncryptionUtil encryptionUtil = new EncryptionUtil();
            String key = encryptionUtil.readEncryptionKeyFromFile();
            setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
           LOGGER.error("Error while encrypting: " + e.toString());
           throw new FastParkingApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), TECHNICAL_ERROR);
        }
    }

    public String decrypt(String strToDecrypt) throws FastParkingApplicationException {
        try
        {
            EncryptionUtil encryptionUtil = new EncryptionUtil();
            String key = encryptionUtil.readEncryptionKeyFromFile();
            setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            LOGGER.error("Error while decrypting: " + e.toString());
            throw new FastParkingApplicationException(e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), TECHNICAL_ERROR);
        }

    }
}
