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
package org.etosha.tools.gui;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.security.auth.login.LoginException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.etosha.cmd.EtoshaContextLogger;
import static org.etosha.cmd.EtoshaContextLogger.clt;
import org.etosha.core.sc.connector.SemanticContextBridge;
import org.etosha.core.sc.connector.external.Wiki;
import org.semanpix.parser.SemanpixImageParser;

/**
 *
 * @author kamir
 */
public class WikiTemplateRenderer {
 
    public static EtoshaContextLogger clt = null;

    // here are the WIKI page templates ...
    static String[] template = {
       
           "TEMPLATE_ImagePage"
    
    };

    public static String renderTemplate(String templatePageName, Properties props) throws IOException {

        VelocityContext context = new VelocityContext();

        for (Object k : props.keySet()) {
            String s = (String) k;
            System.out.println(">>> " + s + " : " + props.getProperty(s));
            context.put(s, props.getProperty(s));
        }

        Wiki w = clt.scb.getWiki();        
        String template = w.getPageText( templatePageName );

        System.out.println( template );        
        
        StringWriter writer = new StringWriter();

        Velocity.init();
        Velocity.evaluate(context,
                writer,
                "LOG", // used for logging
                template);

        System.out.println(writer.getBuffer());

        return writer.getBuffer().toString();

    }

    public static void main(String args[]) throws Exception {

        WikiTemplateRenderer renderer = new WikiTemplateRenderer();
        
        renderer.init();
        
        String[] documents = {
            "http://semanpix.de/oldtimer/wiki/images/5/5e/Peter001.jpg"
        }; 
        
        SemanpixImageParser sp = new SemanpixImageParser();

        URL url = new URL(documents[0]);
        Properties props = sp.getMetaDataAsProperties(url);

        renderTemplate(template[0], props);

    }
    
    public static void init() throws Exception {
        
        clt = new EtoshaContextLogger();

        clt.setConf(new Configuration());

        File cfgFile = EtoshaContextLogger.getCFGFile();

        if (cfgFile.exists()) {
            /**
             * according to:
             *
             * http://stackoverflow.com/questions/11478036/hadoop-configuration-
             * property-returns-null
             *
             * we add the resource as a URI
             */
            clt.getConf().addResource(cfgFile.getAbsoluteFile().toURI().toURL());
        }

        clt.initConnector();
         
    }
 

}
