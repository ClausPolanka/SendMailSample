/*
 * Copyright 2009 Manuel Carrasco Moñino. (manuel_carrasco at users.sourceforge.net) 
 * http://code.google.com/p/gwtupload
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package gwtupload.sendmailsample.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

/**
 * This class sends by email, all the fields and files received by GWTUpload servlet.
 * 
 * @author Manolo Carrasco Moñino
 *
 */
public class SendMailSampleServlet extends UploadAction {

  private static final String SMTP_SERVER = "localhost";

  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see gwtupload.server.UploadAction#executeAction(javax.servlet.http.HttpServletRequest, java.util.List)
   */
  @Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
    try {
      String from=null, to=null, subject="", body="";
      // create a new multipart content
      MimeMultipart multiPart = new MimeMultipart();
      for (FileItem item : sessionFiles) {
        if (item.isFormField()) {
          if ("from".equals(item.getFieldName()))
            from = item.getString();
          if ("to".equals(item.getFieldName()))
            to = item.getString();
          if ("subject".equals(item.getFieldName()))
            subject = item.getString();
          if ("body".equals(item.getFieldName()))
            body = item.getString();
        } else {
          // add the file part to multipart content
          MimeBodyPart part = new MimeBodyPart();
          part.setFileName(item.getName());
          part.setDataHandler(new DataHandler(new ByteArrayDataSource(item.get(), item.getContentType())));
          multiPart.addBodyPart(part);
        }
      }
      
      // add the text part to multipart content
      MimeBodyPart txtPart= new MimeBodyPart();
      txtPart.setContent(body, "text/plain");
      multiPart.addBodyPart(txtPart);
      
      // configure smtp server
      Properties props = System.getProperties();
      props.put("mail.smtp.host", SMTP_SERVER);
      // create a new mail session and the mime message
      MimeMessage mime = new MimeMessage(Session.getInstance(props));
      mime.setText(body);
      mime.setContent(multiPart);
      mime.setSubject(subject);
      mime.setFrom(new InternetAddress(from));
      for(String rcpt: to.split("[\\s;,]+"))  
        mime.addRecipient(Message.RecipientType.TO, new InternetAddress(rcpt));
      // send the message
      Transport.send(mime);
    } catch (MessagingException e) {
      throw new UploadActionException(e.getMessage());
    }
    return "Your mail has been sent successfuly.";
  }

}
