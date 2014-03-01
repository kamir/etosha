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
public class RDF2GoDemo {

    public static void main(String[] args) throws ModelException, MalformedURLException, IOException {


        String URL1 = "http://semanpix.de/oldtimer/wiki/index.php?title=Special:Ask/-5B-5BCategory:Fach-5D-5D/-3F%3DFach-23/-3FHas-20Abbreviation%3DAbk%C3%BCrzung/-3FHasSpecialAbbreviation%3DSpezialabk%C3%BCrzung-20(y-2Fn)/-3FIs-20taught-20at%3DHochschulart/format%3Drdf/mainlabel%3DFach/offset%3D0";
        String URL2 = "http://semanpix.de/oldtimer/wiki/index.php?title=Special:ExportRDF/Mirko";
        args = new String[1];
        args[0] = URL2;
        InputStream input = new URL(URL2).openStream();

        // getting model
        Model model = RDF2Go.getModelFactory().createModel();
        model.open();

        model.readFrom(input);

        // creating URIs
        URI max = model.createURI("http://xam.de/foaf.rdf.xml#i");
        URI currentProject = model.createURI("http://xmlns.com/foaf/0.1/#term_currentProject");
        URI name = model.createURI("http://xmlns.com/foaf/0.1/#term_name");
        URI semweb4j = model.createURI("http://semweb4j.org");

        // adding a statement to the model
        model.addStatement(max, currentProject, semweb4j);
        model.addStatement(max, name, "Max Völkel");

        // dumping model to the screen
        model.dump();

        File outDir = new File("gen-src");
        
        CodeGenerator.generate(model, outDir, "etosha.oldtimer.wiki", Reasoning.rdfs, true, "mod");
        
        javax.swing.JOptionPane.showMessageDialog(null, "DONE");
    }
}
