package com.clicks.securedmedicaldataapp.service;

import com.clicks.securedmedicaldataapp.utils.EncryptDecrypt;
import com.github.bloodshura.ignitium.assets.image.Image;
import com.github.bloodshura.ignitium.steganography.AsciiRgbCipher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EncryptionService {


    public boolean encodeText(String text, String secKey, String outputPath) {
        try {
            String encryptedText = EncryptDecrypt.encrypt(text, secKey);
            EncryptDecrypt.encodeTextInImage(secKey, encryptedText, outputPath, false);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean encodeImage(String source, String output, String key) throws Exception {

        //read image

        BufferedImage bufferedImage = readImage(source);

        //convert to base 64
        String base64Image = EncryptDecrypt.encryptImageToBase64(bufferedImage);

        if(base64Image != null) {
            //create stegno-image
            EncryptDecrypt.encodeTextInImage(key, base64Image, output, bufferedImage, true);
            return true;
        }
        return false;
    }

    public BufferedImage readImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                return ImageIO.read(imageFile);
            } else {
                System.err.println("Image file does not exist: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void decrypt(String sourcePath, String outPutPath, String secreteKeyText) throws Exception {
        AsciiRgbCipher asciiRgbCipher = new AsciiRgbCipher();
        BufferedImage bufferedImage = readImage(sourcePath);
        Image image = new Image(bufferedImage);
        String decodedString = asciiRgbCipher.decode(image);
        String decryptedFile = EncryptDecrypt.decrypt(decodedString, secreteKeyText);

        String[] split = decryptedFile.split("@@");
        String type = split[0].replace("Type=", "");
        String file = split[1];

        String filePath = EncryptDecrypt.FILE_PATH + file + ".txt";

        if (type.equalsIgnoreCase("Image")) {
            BufferedImage reconstructedImage = reconstructImage(filePath);
            assert reconstructedImage != null;
            EncryptDecrypt.saveFile(reconstructedImage, outPutPath + ".png");
        }
        else {
            String textFile = EncryptDecrypt.readTextFile(filePath);

            assert textFile != null;
            String decryptedText = EncryptDecrypt.decrypt(textFile.trim(), secreteKeyText);
            EncryptDecrypt.saveFile(decryptedText, outPutPath + ".txt");
        }

    }

    private BufferedImage reconstructImage(String filePath) {
        String textFile = EncryptDecrypt.readTextFile(filePath);
        if(textFile != null) {
            return EncryptDecrypt.base64ToBufferedImage(textFile);
        }
        return null;
    }
}
