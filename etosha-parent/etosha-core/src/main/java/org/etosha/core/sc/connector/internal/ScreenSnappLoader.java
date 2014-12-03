/*
 * Copyright 2014 The Apache Software Foundation.
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
package org.etosha.core.sc.connector.internal;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchProviderException;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.security.auth.login.LoginException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;
import org.etosha.cmd.EtoshaContextLogger;

/**
 *
 * @author training
 */
public class ScreenSnappLoader {

    public static EtoshaContextLogger clt = null;
    public static Configuration cfg = null;

    public static void run(String[] args) throws UnknownHostException, LoginException, IOException {
        
        try {
            ScreenSnappLoader.importScreenShot();
        } 
        catch (AWTException ex) {
            Logger.getLogger(ScreenSnappLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void importImageToNewPage(File image) throws LoginException, IOException, AWTException {

        File f = image;
  
        System.out.println( "image_import@"+f.getAbsolutePath() );
        
        // we select a context
        String uc = javax.swing.JOptionPane.showInputDialog("??? USERCONTEXT ???", "(training)");
        
        // here we upload the data 
        clt.scb.logImageToPage( clt.scb.createTheNewUCPage( uc  ), f, f.getName() );
        
        return;
    }        
            

    public static void importScreenShot() throws LoginException, IOException, AWTException {

        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();

        Rectangle allScreenBounds = new Rectangle();
        for (GraphicsDevice screen : screens) {
            Rectangle screenBounds = screen.getDefaultConfiguration().getBounds();

            allScreenBounds.width += screenBounds.width;
            allScreenBounds.height = Math.max(allScreenBounds.height, screenBounds.height);
        }

        
        Robot robot = new Robot();
        BufferedImage screenShot = robot.createScreenCapture(allScreenBounds);

        File f = File.createTempFile("etosha_", "_screen.png");
        
        ImageIO.write(screenShot, "png", f );
        
        System.out.println( "snap@"+f.getAbsolutePath() );
        
        // we select a context
        String uc = javax.swing.JOptionPane.showInputDialog("??? USERCONTEXT ???", "(training)");
        
        clt.scb.logImageToPage( clt.scb.createTheNewUCPage( uc  ), f, f.getName() );
        
        return;
    }
}
