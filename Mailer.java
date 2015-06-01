package riley.web.rest;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//This class is required by profMailServlet.
//The only additional part of the Mailer that is hardcoded right now is 
//the protocol used, an additional Mailer constructor opperator to add would be
//a choice for which protocol is used.
/**
 * 
 * @author Largely this was based on a tutorial from Oracle.
 *
 */
public class Mailer {
	
	private int port;
	private String host;
	private String from;
	private boolean auth = true;
	private String username;
	private String password;
	private Protocol protocol = Protocol.TLS;
	
	/**
	 * 
	 * @param inPort Port being used for smtp server.
	 * @param inHost smtp host server.
	 * @param inFrom username@example.com
	 * @param inUser username@example.com
	 * @param inPassword your secret password for the smtp server used.
	 */
	public Mailer(int inPort, String inHost, String inFrom, String inUser, String inPassword)
	{
		port = inPort;
		host = inHost;
		from = inFrom;
		username = inUser;
		password = inPassword;
	}
	
	/**
	 * 
	 * @param to destination user@example.com
	 * @param subject the subject of your message
	 * @param body the body of your message
	 */
	public void sendEmail(String to, String subject, String body)
	{
		//PrintWriter writer = new PrintWriter(System.out);
		Properties props = new Properties();
		
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		//props.list(writer);
		//props.put("mail.smtp.ssl.enable", true);

		switch(protocol)
		{
		case SMTPS:
			props.put("mail.smtp.ssl.enable", true);
			break;
		case TLS:
			props.put("mail.smtp.starttls.enable", true);
			break;
		}
			Authenticator authen = null;
			if(auth)
			{
				props.put("mail.smtp.auth", true);
				authen = new Authenticator()
				{
					private PasswordAuthentication pa = new PasswordAuthentication(username,password);
					@Override
					public PasswordAuthentication getPasswordAuthentication(){
						return pa;
					}
				};
			}
			Session session = Session.getInstance(props,authen);
			session.setDebug(true);
			//System.out.println(session);
			MimeMessage message = new MimeMessage(session);
			try
			{
				message.setFrom(new InternetAddress(from));
				InternetAddress[] address = {new InternetAddress(to)};
				message.setRecipients(Message.RecipientType.TO, address);
				message.setSubject(subject);
				message.setSentDate(new Date());
				message.setText(body);
				Transport.send(message);
			}catch(MessagingException ex){
				ex.printStackTrace();
			}
		}
	}
