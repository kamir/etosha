package org.etosha.googleclients.oauth2.tool;


import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Thread;
import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.Message;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.etosha.googleclients.oauth2.gui.GMailLabelSelectorFrame;



public class GmailIndexer {

    // Check https://developers.google.com/gmail/api/auth/scopes for all available scopes
    private String SCOPE = "https://www.googleapis.com/auth/gmail.readonly";
    private String APP_NAME = "Gmail API Quickstart";
    // Email address of the user, or "me" can be used to represent the currently authorized user.
    private String USER = "mirko.kaempf@cloudera.com";
    // Path to the client_secret.json file downloaded from the Developer Console
    private String CLIENT_SECRET_PATH = "client_secret_761864824458-tg536nkrif0rgitmo8ej2pmfbjsmh3os.apps.googleusercontent.com.json";

    private GoogleClientSecrets clientSecrets;

    private Gmail service = null;

    private GoogleCredential credential = null;

    public GmailIndexer() throws IOException, URISyntaxException {

        boolean testQueryImport = false;
        boolean testThreadImport = false;
        boolean testLabelList = false;

        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        clientSecrets = GoogleClientSecrets.load(jsonFactory, new FileReader(CLIENT_SECRET_PATH));

        // Allow user to authorize via url.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Arrays.asList(SCOPE))
                .setAccessType("online")
                .setApprovalPrompt("auto").build();

        String url = flow.newAuthorizationUrl().setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI)
                .build();
        System.out.println("Please open the following URL in your browser then type"
                + " the authorization code:\n" + url);

        java.awt.Desktop.getDesktop().browse(new URI(url));

        // Read code entered by user.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();

        // Generate Credential using retrieved code.
        GoogleTokenResponse response = flow.newTokenRequest(code)
                .setRedirectUri(GoogleOAuthConstants.OOB_REDIRECT_URI).execute();

        credential = new GoogleCredential()
                .setFromTokenResponse(response);

        // Create a new authorized Gmail API client
        service = new Gmail.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName(APP_NAME).build();

        if (testThreadImport) {

            // Retrieve a page of Threads; max of 100 by default.
            ListThreadsResponse threadsResponse = service.users().threads().list(USER).execute();
            List<Thread> threads = threadsResponse.getThreads();

            // Print ID of each Thread.
            for (Thread thread : threads) {
                System.out.println("Thread ID: " + thread.getId());
            }

        }

        if (testLabelList) {

            listLabels(service, USER);

            String demo2 = "Label_55";

            List<String> labels = new ArrayList<String>();
            labels.add(demo2);

            System.out.println("Now we load content ... ");

            listMessagesWithLabels(service, USER, labels);

        }

        if (testQueryImport) {
            /**
             *
             * Test the Query import ...
             *
             */

            String query = "subject:([tt.faq])";
            List<Message> messages = listMessagesMatchingQuery(service, USER, query);
            // Print ID of each Thread.
            for (Message m : messages) {

                Message m2 = getMessage(service, USER, m.getId());

            }

        }

    }

    public static void main(String[] args) throws IOException, URISyntaxException {

        GmailIndexer gmix = new GmailIndexer();

        GMailLabelSelectorFrame.open(gmix);

    }

    /**
     * List the Labels in the user's mailbox.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me" can be used to
     * indicate the authenticated user.
     * @throws IOException
     */
    public static List<Label> listLabels(Gmail service, String userId) throws IOException {
        ListLabelsResponse response = service.users().labels().list(userId).execute();
        List<Label> labels = response.getLabels();
        for (Label label : labels) {
            System.out.println(label.toPrettyString());
        }
        return labels;
    }

    /**
     * List the Labels in the user's mailbox.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me" can be used to
     * indicate the authenticated user.
     * @throws IOException
     */
    public List<Label> listLabels() throws IOException {
        ListLabelsResponse response = service.users().labels().list(USER).execute();
        List<Label> labels = response.getLabels();
        for (Label label : labels) {
            System.out.println(label.toPrettyString());
        }
        return labels;
    }

    /**
     * List all Messages of the user's mailbox matching the query.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me" can be used to
     * indicate the authenticated user.
     * @param query String used to filter the Messages listed.
     * @throws IOException
     */
    public List<Message> listMessagesMatchingQuery(String query) throws IOException {
        ListMessagesResponse response = service.users().messages().list(USER).setQ(query).execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(USER).setQ(query)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        for (Message message : messages) {
            System.out.println(message.toPrettyString());
        }

        return messages;
    }
    /**
     * List all Messages of the user's mailbox matching the query.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me" can be used to
     * indicate the authenticated user.
     * @param query String used to filter the Messages listed.
     * @throws IOException
     */
    public static List<Message> listMessagesMatchingQuery(Gmail service, String userId,
            String query) throws IOException {
        ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(userId).setQ(query)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        for (Message message : messages) {
            System.out.println(message.toPrettyString());
        }

        return messages;
    }

    /**
     * List all Messages of the user's mailbox with labelIds applied.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me" can be used to
     * indicate the authenticated user.
     * @param labelIds Only return Messages with these labelIds applied.
     * @throws IOException
     */
    public static List<Message> listMessagesWithLabels(Gmail service, String userId,
            List<String> labelIds) throws IOException {

        ListMessagesResponse response = service.users().messages().list(userId)
                .setLabelIds(labelIds).execute();

        List<Message> messages = new ArrayList<Message>();

        System.out.println(messages.size());

        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(userId).setLabelIds(labelIds)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        for (Message message : messages) {

            System.out.println(message.toPrettyString());

            Message m = getMessage(service, userId, message.getId());

        }

        return messages;
    }
    
    
        /**
     * List all Messages of the user's mailbox with labelIds applied.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me" can be used to
     * indicate the authenticated user.
     * @param labelIds Only return Messages with these labelIds applied.
     * @throws IOException
     */
    public List<Message> listMessagesWithLabels(List<String> labelIds) throws IOException {

        ListMessagesResponse response = service.users().messages().list(USER)
                .setLabelIds(labelIds).execute();

        List<Message> messages = new ArrayList<Message>();

        System.out.println(messages.size());

        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(USER).setLabelIds(labelIds)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        List<Message> messagesFull = new ArrayList<Message>();

        for (Message message : messages) {

            System.out.println(message.toPrettyString());

            Message m = getMessage(service, USER, message.getId());

            System.out.println(message.toPrettyString());

            messagesFull.add(m);
            
        }

        return messagesFull;
    }


    /**
     * Get Message with given ID.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me" can be used to
     * indicate the authenticated user.
     * @param messageId ID of Message to retrieve.
     * @return Message Retrieved Message.
     * @throws IOException
     */
    public static Message getMessage(Gmail service, String userId, String messageId)
            throws IOException {
        
        Message message = service.users().messages().get(userId, messageId).setFormat("raw").execute();

        System.out.println("Message id      : " + message.getId());
        System.out.println("Message thread  : " + message.getThreadId());
        System.out.println("Message snippet : " + message.getSnippet());
        System.out.println("Message raw     : " + message.getRaw());
        
        // System.out.println("Message payload : " + message.getPayload().toString() );  // works not with raw formats
        
        return message;
        
    }
    
    
    /**
   * Get a Message and use it to create a MimeMessage.
   *
   * @param service Authorized Gmail API instance.
   * @param userId User's email address. The special value "me"
   * can be used to indicate the authenticated user.
   * @param messageId ID of Message to retrieve.
   * @return MimeMessage MimeMessage populated from retrieved Message.
   * @throws IOException
   * @throws MessagingException
   */
  public MimeMessage getMimeMessage(String messageId)
      throws IOException, MessagingException {
      
    Message message = service.users().messages().get(USER, messageId).setFormat("raw").execute();

    byte[] emailBytes = Base64.decodeBase64(message.getRaw());

    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));

    return email;
  }


}
