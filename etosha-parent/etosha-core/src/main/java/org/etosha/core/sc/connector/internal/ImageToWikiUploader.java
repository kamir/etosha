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
public class ImageToWikiUploader {

    public static EtoshaContextLogger clt = null;
    public static Configuration cfg = null;
    
    public static String _importImageToNewPage(File image, Properties props ) throws LoginException, IOException, AWTException {

        File f = image;
  
        System.out.println( "image_import@"+f.getAbsolutePath() );
        
        // we select a context
        String uc = javax.swing.JOptionPane.showInputDialog("??? USERCONTEXT ???", "(training)");
        
        // here we upload the data 
        clt.scb.logImageToPage( clt.scb.createTheNewUCPage( uc  ), f, f.getName() );
        
        return "Done.";
    }        
            


}
