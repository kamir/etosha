/*
 * Copyright 2015 The Apache Software Foundation.
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
package org.etosha.contextualizer.tools.fusekicluster;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

/**
 *
 *
 * @author kamir
 */
public class EmbeddedZookeeperRunner {

    public static void runZookeeperEmbedded(String parameterTextForParsing ) throws IOException {
        
        // LIKE FROM A PROPERTIES FILE ...
        
        // ... we write to a dumy properties file and load to simulate the
        // parsing of normal properties.
        System.out.println(parameterTextForParsing);
        
        
        
        Properties startupProperties = new Properties();
        InputStream in = IOUtils.toInputStream(parameterTextForParsing, "UTF-8");
        startupProperties.load( in );

        
        
        QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
        try {
            quorumConfiguration.parseProperties(startupProperties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ZooKeeperServerMain zooKeeperServer = new ZooKeeperServerMain();
        final ServerConfig configuration = new ServerConfig();
        configuration.readFrom(quorumConfiguration);

        new Thread() {
            public void run() {
                try {
                    zooKeeperServer.runFromConfig(configuration);
                } catch (IOException e) {
                    logError("### Embedded ZooKeeper Failed ###", e);
                }
            }

            private void logError(String s, IOException e) {
                System.err.println();
                e.printStackTrace();
            }
            
        }.start();

    }

}
