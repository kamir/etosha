/**
 *  A Snippet Loader loads snippets from a wiki.
 * 
 *  In general it is the "Velocity Snippet", which
 *  is used to render documents.
 *
 **/
package wikiapiclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SnippetLoader {

    WikiORIGINAL wiki = null;
    static SnippetLoader loader = null;

    private SnippetLoader() {}

    public static SnippetLoader getTheLoader( WikiORIGINAL wikiServer ) {
        
        if ( loader == null ) 
            loader = new SnippetLoader();

        loader.init(wikiServer);
        return loader;
    }

    
    public void init(WikiORIGINAL wikiServer) {
        wiki = wikiServer;
    }

    /**
     * Velocity-Snippets are stored in the WikiSpace. So they are editable 
     * by all users.
     * 
     * @param name
     * @param f
     * @return 
     */
    public String loadVelocityTemplate(final String name, File f, boolean debug) {
        String s = "";
        try {

            System.out.println(">>> Velocity-Snippet: " + name);

                // String statuss = wiki.getPageInfo(name).toString();
            s = wiki.getPageText(name);

            s = s.replace("<nowiki>", " ");
            s = s.replace("</nowiki>", " ");

            if ( debug )    
                System.out.println(s);

            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(s);

            bw.flush();

            System.out.println(">>> Done." );

        } 
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return s;
    }

}
