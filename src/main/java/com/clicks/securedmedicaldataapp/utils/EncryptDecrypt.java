package com.clicks.securedmedicaldataapp.utils;

import com.github.bloodshura.ignitium.assets.image.Image;
import com.github.bloodshura.ignitium.steganography.AsciiRgbCipher;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EncryptDecrypt {

    public static final String FILE_PATH = "src/main/resources/files/";

    public static SecretKey generateValidAESKey(String keyString) {
        byte[] keyBytes = keyString.getBytes();

        // AES supports key lengths of 128, 192, or 256 bits
        int validKeyLength = 16; // 128 bits

        if (keyBytes.length < validKeyLength) {
            // Pad the key with zeroes if it's too short
            byte[] paddedKey = new byte[validKeyLength];
            System.arraycopy(keyBytes, 0, paddedKey, 0, keyBytes.length);
            return new SecretKeySpec(paddedKey, "AES");
        } else if (keyBytes.length > validKeyLength) {
            // Truncate the key if it's too long
            byte[] truncatedKey = new byte[validKeyLength];
            System.arraycopy(keyBytes, 0, truncatedKey, 0, validKeyLength);
            return new SecretKeySpec(truncatedKey, "AES");
        } else {
            return new SecretKeySpec(keyBytes, "AES");
        }
    }

    public static String encrypt(String input, String keyString) throws Exception {
        SecretKey secretKey = generateValidAESKey(keyString);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedInput, String keyString) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey secretKey = generateValidAESKey(keyString);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedInput);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    public static String encodeImage(String imagePath) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(imagePath));
        return Base64.getEncoder().encodeToString(data);
    }

    public static String encryptImageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream); // You can specify the format ("png", "jpg", etc.) as needed
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptImageToBase642(String imagePath) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(imagePath));
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] base64ToBufferedImage2(String base64Image) {
        return Base64.getDecoder().decode(base64Image);
    }

    public static BufferedImage base64ToBufferedImage(String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image.trim());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(inputStream);

            if (image != null) {
                return image;
            } else {
                System.out.println("Failed to decode the image.");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveFile(BufferedImage image, String outputFilePath) {
        try {

            Path path = Path.of(outputFilePath);
            File outputFile = new File(path.toUri());

            Files.deleteIfExists(path);
            Files.createFile(path);

            ImageIO.write(image, "png", outputFile);

        } catch (IOException e) {
            // Handle any exceptions that may occur during the file saving process
            e.printStackTrace();
        }
    }

    public static void encodeTextInImage(String encKey, String secretText, String outputPath, BufferedImage bufferedImage, boolean isFile) throws Exception {
        AsciiRgbCipher asciiRgbCipher = new AsciiRgbCipher();
        Image image = new Image(generateRandomImage(bufferedImage));

        //create a UUID to represent each name
        encodeText(encKey, secretText, outputPath, asciiRgbCipher, image, isFile);
    }

    public static void encodeTextInImage(String encKey, String secretText, String outputPath, boolean isFile) throws Exception {
        AsciiRgbCipher asciiRgbCipher = new AsciiRgbCipher();
        Image image = new Image(generateRandomImage(600, 400));

        encodeText(encKey, secretText, outputPath, asciiRgbCipher, image, isFile);
    }

    private static void encodeText(String encKey, String secretText, String outputPath, AsciiRgbCipher asciiRgbCipher, Image image, boolean isImage) throws Exception {

        String typeString = isImage ? "Type=Image@@" : "Type=Text@@";

        //create a UUID to represent each name
        UUID fileName = UUID.randomUUID();
        String uuidString = typeString + fileName;

        //encrypt UUID with secKey
        String encryptedName = encrypt(uuidString, encKey);

        //store file with encrypted value as file name
        String filePath = FILE_PATH + fileName + ".txt";
        saveFile(secretText, filePath);

        //Embed the encryptedData
        asciiRgbCipher.encode(image, encryptedName);

        saveFile(image.getImage(), outputPath);
    }

    public static BufferedImage generateRandomImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        long seed = System.currentTimeMillis(); // Use current time as the seed
        Random random = new Random(seed);

        byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < pixels.length; i += 3) {
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);

            pixels[i] = (byte) r;
            pixels[i + 1] = (byte) g;
            pixels[i + 2] = (byte) b;
        }

        return image;
    }
    public static BufferedImage generateRandomImage(BufferedImage inputImage) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Random random = new Random();

        byte[] inputPixels = ((DataBufferByte) inputImage.getRaster().getDataBuffer()).getData();
        byte[] newPixels = ((DataBufferByte) newImage.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < inputPixels.length; i += 4) {
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            int a = inputPixels[i]; // Keep the original alpha value from the input image

            // Set the new color values in the output image
            newPixels[i] = (byte) a;
            newPixels[i + 1] = (byte) r;
            newPixels[i + 2] = (byte) g;
            newPixels[i + 3] = (byte) b;
        }

        return newImage;
    }



    public static void saveFile(String text, String filePath) {
        try(FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readTextFile(String filePath) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            StringBuilder content = new StringBuilder();

            for (String line : lines) {
                content.append(line).append("\n");
            }

            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
