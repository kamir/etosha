/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.etosha.googleclients.oauth2.gui;

import org.etosha.contextualizer.tools.MailIndexer;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.Message;
import java.io.BufferedWriter;
import org.etosha.contextualizer.ContextualizerFactory;
import org.etosha.contextualizer.IContextualizer;
import org.etosha.contextualizer.contexts.EmailCommunicationContext;
import org.etosha.contextualizer.tools.fusekicluster.TripleStoreRunner;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import org.apache.jena.rdf.model.Model;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.etosha.googleclients.oauth2.tool.GmailCollector;
import org.xml.sax.ContentHandler;

/**
 *
 * This JFrame needs an autorized Google-Service to load mails.
 *
 * @author kamir
 */
public class GmailLabelSelectorFrame extends javax.swing.JFrame {

    public static GmailCollector mailcollector = null;

    public static GmailLabelSelectorFrame frame = null;

    public static IContextualizer contexter2;

    public static void openLOGEDOUT(GmailCollector gmix) {

        open(gmix);

    }

    public void login() throws IOException {

        String code = javax.swing.JOptionPane.showInputDialog("TOKEN:");

        // Generate Credential using retrieved code.
        GoogleTokenResponse response = mailcollector.flow.newTokenRequest(code)
                .setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI).execute();

        mailcollector.credential = new GoogleCredential()
                .setFromTokenResponse(response);

        // Create a new authorized Gmail API client
        mailcollector.service = new Gmail.Builder(
                mailcollector.httpTransport,
                mailcollector.jsonFactory,
                mailcollector.credential)
                .setApplicationName(mailcollector.APP_NAME).build();

    }

    //                     < ID   ,Name  >
    public static Hashtable<String, String> labels = new Hashtable<String, String>();

    /**
     *
     * Show all available labels and folders in the GMail account.
     *
     */
    private void initLabels() {

        try {
            List<Label> list = mailcollector.listLabels();

            DefaultListModel dm = new DefaultListModel();

            for (Label l : list) {

                String ls = l.getId() + " " + l.getName();

                dm.addElement(ls);
                labels.put(l.getId(), (String) l.getName());

            }

            this.jList1.setModel(dm);
            this.jLabel2.setText(labels.size() + "");

        } catch (IOException ex) {
            Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Creates new form GMailLabelSelectorFrame
     */
    public GmailLabelSelectorFrame() {

        initComponents();

        initContextualizer();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtaEXCLUDE = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(jList1);

        jButton2.setText("DUMP model and expose");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText("Close TripleStore");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Import all mails");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton3.setText("List all labels");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setText("Login");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel1.setText("# of labels: ");

        jLabel2.setText("...");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton6)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Fuseki Cluster Control"));

        jLabel3.setText("memstore local:");

        jLabel4.setText("partition id:");

        jLabel5.setText("distributed memstore:");

        jButton7.setText("set");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel7.setText("1");

        jTextField1.setText("email-graph-partition.ttl");

        jTextField2.setText("n.a.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(jLabel7))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(337, 337, 337)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(408, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jButton7)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("EXCLUDE LABELS"));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jtaEXCLUDE.setColumns(20);
        jtaEXCLUDE.setRows(5);
        jtaEXCLUDE.setText("DRAFT\nUNREAD\nCATEGORY_UPDATES\nCATEGORY_PROMOTIONS\nCATEGORY_SOCIAL\nCATEGORY_PERSONAL\nCATEGORY_FORUMS\nTRASH\nCHAT\nSPAM\nINBOX\nIMPORTANT\nASF Giraph\nDeleted Messages\n[Imap]/Drafts\n");
        jScrollPane2.setViewportView(jtaEXCLUDE);

        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Label List Controler"));

        jCheckBox1.setText("remove imported label from list");

        jButton1.setText("Import selected mails");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBox1)
                .addGap(396, 396, 396)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jCheckBox1)
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jButton1)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(300, 300, 300)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(284, 284, 284)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addGap(51, 51, 51)
                        .addComponent(jButton4)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        frame.initLabels();
    }//GEN-LAST:event_jButton3ActionPerformed

    // the list of currently imported label-ids
    ArrayList<String> al1 = null;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        String s = (String) this.jList1.getSelectedValue();


        if (this.jCheckBox1.isSelected()) {
            try {

                DefaultListModel model = (DefaultListModel) jList1.getModel();
                int selectedIndex = jList1.getSelectedIndex();
                if (selectedIndex != -1) {
                    model.remove(selectedIndex);
                }

            } 
            catch (Exception ex) {
            };
        }
        
        this.jList1.clearSelection();


        int split = s.indexOf(" ");

        String id = s.substring(0, split);
        String label = s.substring(split);

        System.out.println(">>> Import mails with label-id: " + id);
        System.out.println(">>> Label Name                : " + label);

        al1 = new ArrayList<String>();
        al1.add(id);

        importList(al1);
    }

    public static Vector<String> imported = new Vector<String>();

    private void importList(ArrayList<String> al) {

        Model mo = null;

        int z = 0;

        try {

            // all selected labels are mixed here
            List<Message> lm = mailcollector.listMessagesWithLabels((List<String>) al);

            // ALL labels to import 
            StringBuffer sb1 = new StringBuffer();
            for (String id : al) {
                String s = labels.get(id);
                sb1.append(s + "\n\t");
            }

            MailIndexer faqi = new MailIndexer();
            faqi.init();

            System.out.println(">>> Start import of " + lm.size() + " mails with label-ids: \n" + sb1.toString() + ".");

            for (Message m : lm) {

                String messageid = m.getId();

                if (imported.contains(messageid)) {

                    System.err.println("### " + messageid + " ### Exists already. ");
                } else {

                    List<String> messageLabelIds = m.getLabelIds();
                    String thread = m.getThreadId();

                    // LIST OF labels
                    ArrayList<String> b = new ArrayList<String>();

                    StringBuffer sb = new StringBuffer();
                    for (String id : messageLabelIds) {
                        String s = labels.get(id);
                        b.add(s);
                        sb.append(s + "\n\t");
                    }

                    MimeMessage mm = mailcollector.getMimeMessage(m.getId());

                    System.out.println("[ID     ] " + messageid);
                    System.out.println("[SUBJECT] " + mm.getSubject());
                    System.out.println("[Date  R] " + mm.getReceivedDate());
                    System.out.println("[Date  S] " + mm.getSentDate());
                    System.out.println("[Labels ] \n\t" + sb);

                    /**
                     * Dataset has to be created in two layers.
                     */
                //
                    // Layer 1
                    // 
                    // The RDF graph of mail, topics, labels and collaborators
                    //
                    mo = EmailCommunicationContext.getGoogleEmailAsModel(m, mm, b, thread, contexter2);

                //
                    // Layer 2
                    // 
                    // The documant collection 
                    //
                    // Each mail is indexed in Solr, using its GMail id.
                    //
                    SolrInputDocument doc = faqi.getSolrInputDocument(m.getId(), "GMail-FAQ-Indexer-v1");

                // LABELS b is a multi-field !!!
//                doc.addField("question", mm.getSubject());
//                doc.addField("author", mm.getFrom());
                    String content = "NULL";

                    if (mm.getContent() instanceof String) {
                        content = (String) mm.getContent();
                    } else if (mm.getContent() instanceof MimeMultipart) {
                        MimeMultipart mmp = (MimeMultipart) mm.getContent();

                        content = getText(mmp.getParent());
                    }

                    InputStream stream = new ByteArrayInputStream(
                            content.getBytes(StandardCharsets.UTF_8));

                    // NOW PARSE the content ...
                    ContentHandler contenthandler = new BodyContentHandler();
                    Metadata metadata = new Metadata();
                    Parser parser = new AutoDetectParser();
                    parser.parse(stream, contenthandler, metadata, new ParseContext());

                    content = contenthandler.toString();

                    if (content == null) {
                        content = "NULL";
                    }

                    File f = new File("/ETOSHA.WS/tmp/mail-collection-01/" + messageid + ".txt");

                    File folder = f.getParentFile();
                    if( ! folder.exists() ) folder.mkdirs();
                    
                    FileWriter fw = new FileWriter(f);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(content);
                    bw.flush();
                    bw.close();

                // System.out.println(">BODY<   " + content);
//                doc.addField("answer", content );
//                
//                doc.addField("context" , label );
//                doc.addField("type" , "RAW.MAIL" );
//                
//                faqi.updateCollection("FAQMails02Collection", doc);
                    z++;
                    imported.add(messageid);

                }
            }

        } catch (IOException ex) {
            Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        //
        // finally the model goes into the current "MASTER MODEL" ...
        //
        if (mo != null) {
            contexter2.addGraph(mo);
        }

        System.out.println(">>> " + z + " mails loaded.");

    }//GEN-LAST:event_jButton1ActionPerformed

    private boolean textIsHtml = false;

    /**
     * Return the primary text content of the message.
     */
    private String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);

                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }

        return null;
    }


    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {

            contexter2.close();

            TripleStoreRunner.FUSEKI_DATA = "/GITHUB/ETOSHA.WS/etosha/etosha-parent/etosha-contextualizer/MAIL-CONTEXT-test.ttl";
            TripleStoreRunner.runTS();

            TripleStoreRunner.openUI();

        } catch (IOException ex) {
            Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        TripleStoreRunner.stopTS();


    }//GEN-LAST:event_jButton4ActionPerformed

    public static Vector<String> excludeLabelsV = null;
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        System.out.println(">>> Import all mails, except the label in exclude list. ");

        excludeLabelsV = new Vector<String>();

        String excludes = initExcludeLabels();

        System.out.println(">>> Exclude labels: \n" + excludes);

        Set<String> set = this.labels.keySet();

        al1 = new ArrayList<String>();

        for (String s : set) {
            if (!excludeLabelsV.contains(labels.get(s))) {
                al1.add(s);
                System.out.println("[+] " + s);
            } else {
                System.out.println("[-] " + s);

            }
        }

        // importList( al1 );
        importListToPartitions(al1);


    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        try {

            this.login();
            frame.initLabels();

        } catch (IOException ex) {
            Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        TripleStoreRunner.FUSEKI_DATA = this.jTextField1.getText();

    }//GEN-LAST:event_jButton7ActionPerformed

    /**
     * /
     *
     **
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame = new GmailLabelSelectorFrame();
                frame.setVisible(true);
            }
        });

    }

    public static void open(GmailCollector g) {

        mailcollector = g;

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GmailLabelSelectorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame = new GmailLabelSelectorFrame();
                frame.setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextArea jtaEXCLUDE;
    // End of variables declaration//GEN-END:variables

    private void initContextualizer() {
        contexter2 = ContextualizerFactory.getJenaInMemoryContextualizer("./MAIL-CONTEXT-test.ttl");
    }

    private String initExcludeLabels() {

        excludeLabelsV = new Vector<String>();

        String[] LINES = this.jtaEXCLUDE.getText().split("\n");

        for (String line : LINES) {
            this.excludeLabelsV.add(line);
        }

        return this.jtaEXCLUDE.getText();

    }

    /**
     *
     * @param al1
     */
    private void importListToPartitions(ArrayList<String> al1) {

        // each partition loads all mails, no matter if they exist already
        // in a different one
        boolean flush = true;
        String distGraphFolder = "/ETOSHA.WS/FUSEKI/DISTR/";
        String name = this.jTextField1.getText();

        File f = new File(distGraphFolder);
        f.mkdirs();

        System.out.println("#partitions to load : " + al1.size());

        for (String label : al1) {

            createGraphPartition(label, distGraphFolder, name, flush);

        }

    }

    private void createGraphPartition(String labelID, String distGraphFolder, String name, boolean flush) {

        contexter2.getNewPartition(distGraphFolder, "DGP_" + labelID, name);

        ArrayList al = new ArrayList<String>();
        al.add(labelID);
        importList(al);

        // forgets all data which is already stored and defines a new model
        contexter2.persistPartition();

        imported = new Vector<String>();

    }

}