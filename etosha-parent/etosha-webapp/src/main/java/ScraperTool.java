
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

/**
 *
 * @author kamir
 */
class ScraperTool {

    public static int timeoutMillis = 10000;

    static String[] getDataLists(URL url) throws IOException {

        Document doc = Jsoup.parse(url, timeoutMillis);
        Elements listU = doc.select("ul");
        Elements listO = doc.select("ol");

        String[] l = new String[listU.size() + listO.size()];
        int i = 0;

        for (Element e : listU) {
            l[i] = e.text();
            i++;
        }
        for (Element e : listO) {
            l[i] = e.text();
            i++;
        }

        return l;
    }

    static String getDataFromTable(URL url, String selector) throws IOException {

        Document doc = Jsoup.parse(url, timeoutMillis);
        
        Elements tab = doc.getElementsByIndexEquals( Integer.parseInt(selector) );

        Elements rows = tab.first().select("tr");

        Iterator<Element> it = rows.iterator();

        int a = 0;
        int e = 0;

        // cols is the SCHEMA STRING ...
        // println( "# of cols: " + cols.size );
        int z = 12;

        Vector<String> vec = new Vector<String>();
        StringBuffer sb = new StringBuffer();

        while (it.hasNext()) {

            Element row = it.next();

            int size = row.select("td").size();

            // println( size )
            if (size == z) {

                int zr = processTableRow(row);
                String line = transformTableRow(row);

                System.out.println("row[" + a + "] " + zr + " => " + line);

                vec.add(line);
                sb.append( line + "\n" );

                a += 1;
            } else {
                e += 1;
            }
        }

        System.out.println("# of short rows  =" + e);

        System.out.println("# of rows in vec =" + vec.size());

        return sb.toString();

    }

    static String[] getDataTables(URL url) throws IOException {

        Document doc = Jsoup.parse(url, timeoutMillis);
        Elements links = doc.select("table");

        String[] l = new String[links.size()];
        int i = 0;

        for (Element e : links) {
            l[i] = "id=" + e.id() + " nodeName=" + e.nodeName();
            i++;
        }
        return l;

    }

    static String[] getLinks(URL url) throws IOException {
        Document doc = Jsoup.parse(url, timeoutMillis);
        Elements links = doc.select("a[href]");

        String[] l = new String[links.size()];
        int i = 0;

        for (Element e : links) {
            l[i] = e.text() + " => " + e.attr("abs:href");
            i++;
        }

        return l;
    }

    private static int processTableRow(Element r) {
        return r.select("td").size();
    }

    private static String transformTableRow(Element r) {

        int a = 0; // we concat all the columns by a comma ...	
        Elements fields = r.select("td");
        Iterator<Element> it = fields.iterator();

        String fieldString = it.next().text();
  
        while (it.hasNext()) {

            String field = it.next().text();

            System.out.println("field[" + a + "] => " + field); //  vec = vec :+ line
            a = a + 1;
            fieldString = fieldString + "," + field;
        }
        //"month,lang.1,lang.2,lang.3,lang.4,lang.5,regions,participation.1,participation.2,participation.3,usage,content"
        return fieldString;
    }

    static String getHTMLFromTable(URL url, String selector) throws IOException {

        Document doc = Jsoup.parse(url, timeoutMillis);

        Elements tab = doc.getElementsByTag("table");
        
        return tab.get( Integer.parseInt(selector) ).html() + "\n\n" + tab.get( Integer.parseInt(selector) ).outerHtml() ;

        // Elements tab = doc.select("table" + selector);
        
    }
}
