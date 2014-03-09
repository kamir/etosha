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
package semweb;

import java.io.File;
import org.ontoware.rdf2go.Reasoning;
import org.ontoware.rdfreactor.generator.CodeGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.ontoware.rdf2go.model.*;
import org.ontoware.rdf2go.*;
import org.ontoware.rdf2go.exception.ModelException;
import org.ontoware.rdf2go.model.node.URI;
import org.ontoware.rdf2go.model.node.Variable;

/**
 *
 * @author training
 */
public class SMWApiGenerator {

    public static void main(String[] args) throws ModelException, MalformedURLException, IOException {


        String URL1 = "http://opentox.org/data/documents/development/RDF%20files/ExampleOWL/at_download/file";
        String URL2 = "http://semanpix.de/oldtimer/wiki/index.php?title=Special:ExportRDF/Category:DataSet";
        String URL3 = "http://semanpix.de/oldtimer/wiki/index.php?title=Special:ExportRDF/Category:Project";
        String URL4 = "http://semanpix.de/oldtimer/wiki/index.php?title=Special:ExportRDF/Category:ClusterUser";
        String URL5 = "http://semanpix.de/oldtimer/wiki/index.php?title=Special:ExportRDF/Category:CP";
        String URL6 = "http://semanpix.de/oldtimer/wiki/index.php?title=Special:ExportRDF/Category:PP";
        String URL7 = "http://semanpix.de/oldtimer/wiki/index.php?title=Special:ExportRDF/Category:Cluster_Docu";

        
        
        args = new String[7];
        args[0] = URL1;
        args[1] = URL2;
        args[2] = URL3;
        args[3] = URL4;
        args[4] = URL5;
        args[5] = URL6;
        args[6] = URL7;
        
        Model mod  = RDF2Go.getModelFactory().createModel();
        mod.open();

        for(int i = 0; i < args.length; i++ ) {
       
             Model model = RDF2Go.getModelFactory().createModel();
             model.open();
             InputStream input = new URL(args[i]).openStream();

             model.readFrom(input);

             mod.addModel(model);
        }
    
        // dumping model to the screen
        mod.dump();

        File outDir = new File("./src/main/java");
        
        CodeGenerator.generate(mod, outDir, "smw.api.etosha.oldtimer.wiki", Reasoning.rdfsAndOwl, true, "mod");
        
        javax.swing.JOptionPane.showMessageDialog(null, "DONE");
        
    }
}
