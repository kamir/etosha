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
package org.etosha.sandbox.nlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

/**
 *
 * @author kamir
 */
public class POSDemo {

    public static void main(String[] args) {
        InputStream modelIn = null;

        try {
            
            // Where can I find the models?
            // 
            // http://opennlp.sourceforge.net/models-1.5/
            // 
            modelIn = new FileInputStream("en-pos-maxent.bin");
            POSModel model = new POSModel(modelIn);
            System.out.println( "Model created ...");
            
            POSTaggerME tagger = new POSTaggerME(model);
            System.out.println( "Tagger created ...");
            
            String sentence = javax.swing.JOptionPane.showInputDialog("?");
            
            String[] words = sentence.split(" ");
            
            String tags[] = tagger.tag( words );
            System.out.println("Analyse the Query: " + sentence );
            int i = 0;
            for( String t : tags ) {
                System.out.println( words[i] + " : "+ t );
                i++;
            }    

        } catch (IOException e) {
            // Model loading failed, handle the error
            e.printStackTrace();
        } finally {
            if (modelIn != null) {
                try {
                    
                    System.out.println( "Model closed ...");
                    modelIn.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
