package org.myproject.test.qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
 

import javax.imageio.ImageIO;

import org.eclipse.swt.graphics.RGB;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
 
/**
 * @author Crunchify.com
 */
//N123 - 5ª - 1ºS 2015/2016

 
public class GenerateQRCode {
 
     // Tutorial: https://github.com/Slackify/zxing
 
	private String codeText = "N123 - 5ª - 1ºS 2015/2016";
    private String fileName = "D:/N123_5F_1S_1516.png";

    
	public GenerateQRCode() {
		super();
	}


	public void generateQRCode(String codeText, String fileName, RGB qrColor) {
        int size = 125;
        String fileType = "png";
        
        if (codeText == null || fileName == null) {
        	return;
        }

        File qrCodeFile = new File(fileName);
        
        try {
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            
            BitMatrix byteMatrix = qrCodeWriter.encode(codeText,BarcodeFormat.QR_CODE, size, size, hintMap);
            
            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);
            
            image.createGraphics();
 
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
//            graphics.setColor(Color.BLACK);
            
            Color rgbColor = new Color(qrColor.red, qrColor.green, qrColor.blue);
            graphics.setColor(rgbColor);
           
            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            
            ImageIO.write(image, fileType, qrCodeFile);
            
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n\nYou have successfully created QR Code.");
		
	}
	
}