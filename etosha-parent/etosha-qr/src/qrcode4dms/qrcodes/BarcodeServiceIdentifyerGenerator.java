package qrcode4dms.qrcodes;

/*
 * Copyright 2010 Jeremias Maerki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id: SampleBarcodeEnhanced.java,v 1.1 2010/10/05 08:56:04 jmaerki Exp $ */
 

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.datamatrix.DataMatrixBean;
import org.krysalis.barcode4j.impl.datamatrix.SymbolShapeHint;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoder;
import org.krysalis.barcode4j.output.bitmap.BitmapEncoderRegistry;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * This class demonstrates how to create a barcode bitmap that is enhanced by custom elements.
 *
 * @version $Id: SampleBarcodeEnhanced.java,v 1.1 2010/10/05 08:56:04 jmaerki Exp $
 */
public class BarcodeServiceIdentifyerGenerator {

    private void generate(String[] text, File outputFile) throws IOException {
        
        String msg = "WebSceye-Service-Locator";
        
        //String[] paramArr = new String[] {"URL=192.168.0.1", "APP=websceye", "USER=mirko.kaempf@bitocean.de"};
        
        String[] paramArr = text;

        //Create the barcode bean
        DataMatrixBean bean = new DataMatrixBean();

        final int dpi = 200;

        //Configure the barcode generator
        bean.setModuleWidth(UnitConv.in2mm(8.0f / dpi)); //makes a dot/module exactly eight pixels
        bean.doQuietZone(true);
        // bean.setShape(SymbolShapeHint.FORCE_RECTANGLE);
        bean.setShape(SymbolShapeHint.FORCE_SQUARE);
        

        boolean antiAlias = false;
        int orientation = 0;
        //Set up the canvas provider to create a monochrome bitmap
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(
                dpi, BufferedImage.TYPE_BYTE_BINARY, antiAlias, orientation);

        //Generate the barcode
        bean.generateBarcode(canvas, msg);

        //Signal end of generation
        canvas.finish();

        //Get generated bitmap
        BufferedImage symbol = canvas.getBufferedImage();

        int fontSize = 11; //pixels
        int lineHeight = (int)(fontSize * 1.2);
        Font font = new Font("Times", Font.PLAIN, fontSize);
        int width = symbol.getWidth();
        int height = symbol.getHeight();
        FontRenderContext frc = new FontRenderContext(new AffineTransform(), antiAlias, true);
        for (int i = 0; i < paramArr.length; i++) {
            String line = paramArr[i];
            Rectangle2D bounds = font.getStringBounds(line, frc);
            width = (int)Math.ceil(Math.max(width, bounds.getWidth()));
            height += lineHeight;
        }

        //Add padding
        int padding = 2;
        width += 2 * padding;
        height += 3 * padding;

        BufferedImage bitmap = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = (Graphics2D)bitmap.getGraphics();
        g2d.setBackground(Color.white);
        g2d.setColor(Color.black);
        g2d.clearRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        g2d.setFont(font);

        //Place the barcode symbol
        AffineTransform symbolPlacement = new AffineTransform();
        symbolPlacement.translate(padding, padding);
        g2d.drawRenderedImage(symbol, symbolPlacement);

        //Add text lines (or anything else you might want to add)
        int y = padding + symbol.getHeight() + padding;
        for (int i = 0; i < paramArr.length; i++) {
            String line = paramArr[i];
            y += lineHeight;
            g2d.drawString(line, padding, y);
        }
        g2d.dispose();

        //Encode bitmap as file
        String mime = "image/png";
        OutputStream out = new FileOutputStream(outputFile);
        try {
            final BitmapEncoder encoder = BitmapEncoderRegistry.getInstance(mime);
            encoder.encode(bitmap, out, mime, dpi);
        } finally {
            out.close();
        }
    }
    
        /**
     * Command-line program.
     * @param args the command-line arguments
     */
    public static boolean create( String[] text, File file ) {
        boolean bv = false;
        try {
            BarcodeServiceIdentifyerGenerator app = new BarcodeServiceIdentifyerGenerator();
            app.generate( text , file );
            if ( file.canRead() ) bv = true;
        } 
        catch (Exception e) {
            e.printStackTrace();
            bv = false;
        }
        return bv;
    }

    /**
     * Command-line program.
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        generateQR4FlyerRefinder();
        generateQR4Test();
    }
    
    public static void generateQR4FlyerRefinder() { 
       try {
            
            BarcodeServiceIdentifyerGenerator app = new BarcodeServiceIdentifyerGenerator();
            String[] text = new String[3];
            
            File f = new File("C:/TEST/facebook.png");
            text[0] = "http://www.facebook.com/Refinder";
            text[1] = "";
            text[2] = "";
            
            if ( app.create(text, f) ) {
                System.out.println(">>> QR: [" + text[0] + "] erzeugt in: " + f.getAbsolutePath() );
            }
            else { 
                System.out.println("[ERROR] >>> QR: [" + text[0] + "] wurde nicht erzeugt in: " + f.getAbsolutePath() );
            }
                        
            f = new File("C:/TEST/twitter.png");
            text[0] = "http://twitter.com/Refinder";
            text[1] = "";
            text[2] = "";
            
            if ( app.create(text, f) ) {
                System.out.println(">>> QR: [" + text[0] + "] erzeugt in: " + f.getAbsolutePath() );
            }
            else { 
                System.out.println("[ERROR] >>> QR: [" + text[0] + "] wurde nicht erzeugt in: " + f.getAbsolutePath() );
            }
            
                                    
            f = new File("C:/TEST/register.png");
            text[0] = "https://www.getrefinder.com/accounts/register";
            text[1] = "";
            text[2] = "";
            
            if ( app.create(text, f) ) {
                System.out.println(">>> QR: [" + text[0] + "] erzeugt in: " + f.getAbsolutePath() );
            }
            else { 
                System.out.println("[ERROR] >>> QR: [" + text[0] + "] wurde nicht erzeugt in: " + f.getAbsolutePath() );
            }
            
            f = new File("C:/TEST/landing_page.png");
            text[0] = "https://www.getrefinder.com/dpg-2012";
            text[1] = "";
            text[2] = "";
            
            if ( app.create(text, f) ) {
                System.out.println(">>> QR: [" + text[0] + "] erzeugt in: " + f.getAbsolutePath() );
            }
            else { 
                System.out.println("[ERROR] >>> QR: [" + text[0] + "] wurde nicht erzeugt in: " + f.getAbsolutePath() );
            }
            
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    
    }
    
    public static void generateQR4Test() { 
       try {
            
            BarcodeServiceIdentifyerGenerator app = new BarcodeServiceIdentifyerGenerator();
            File outputFile = new File("out.png");
            String[] text = new String[] {"Hallo Doreen,", "Ich liebe DICH!", "\nDein Mirko. " };
            app.generate(text, outputFile);
            
            File f = new File("C:/TEST/websceyeprofil.png");
            text[0] = "http://192.168.3.149:8080/websceyex/profil?resoultion=200dpi&color=bw&user=kamir1604";
            text[1] = "";
            text[2] = "";
            
            if ( app.create(text, f) ) {
                System.out.println(">>> QR: [" + text[0] + "] erzeugt in: " + f.getAbsolutePath() );
            }
            else { 
                System.out.println("[ERROR] >>> QR: [" + text[0] + "] wurde nicht erzeugt in: " + f.getAbsolutePath() );
            }
            
            
            
            
            
            f = new File("C:/TEST/doc1locator.png");
            text[0] = "http://192.168.3.149:8080/webdocs/showfile?id=1234567";
            text[1] = "";
            text[2] = "";
            
            if ( app.create(text, f) ) {
                System.out.println(">>> QR: [" + text[0] + "] erzeugt in: " + f.getAbsolutePath() );
            }
            else { 
                System.out.println("[ERROR] >>> QR: [" + text[0] + "] wurde nicht erzeugt in: " + f.getAbsolutePath() );
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    
    }


}
