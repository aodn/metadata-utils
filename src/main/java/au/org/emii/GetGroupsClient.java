// package org.geonetwork.xmlservices.client;
package au.org.emii;



import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jdom.Document;
import org.jdom.Element;
// import org.jdom.XMLOutputter;

import org.apache.commons.httpclient.protocol.Protocol;


import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory; 


import org.jdom.output.XMLOutputter; 

public class GetGroupsClient {

  public static void main(String args[]) {
    // Create request xml**
    Element request = new Element("request");
    // Create PostMethod specifying service url**
    // String serviceUrl = "http://localhost:8080/geonetwork/srv/en/xml.group.list";


    String serviceUrl = "https://catalogue-123.aodn.org.au/geonetwork/srv/en/xml.group.list";


      Protocol.registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory (), 443)); 
     // Protocol.registerProtocol("myhttps", new Protocol("https", new MySSLSocketFactory(), 9443)); 



    PostMethod post = new PostMethod(serviceUrl);

    try {
//      String postData = "blah" ;//Xml.getString(new Document(request));
      String postData = new XMLOutputter().outputString( new Document( request ) );



		// Set post data, mime-type and encoding**
      post.setRequestEntity(new StringRequestEntity(postData, "application/xml", "UTF8"));

      // Send request**
      HttpClient httpclient = new HttpClient();
      int result = httpclient.executeMethod(post);

      // Display status code**
      System.out.println("Response status code: " + result);

      // Display response**
      System.out.println("Response body: ");
      System.out.println(post.getResponseBodyAsString());

    } catch (Exception ex) {
      ex.printStackTrace();

    } finally {
      // Release current connection to the connection pool
      // once you are done**
      post.releaseConnection();
    }
  }
}
