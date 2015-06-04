package riley.web.rest;

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
/**
 * This class sends emails.
 * @author Largely this was based on a tutorial from Oracle.
 * @author Ian Riley
 * @author Christian Markmiller
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
	public Mailer(int port, String host, String from, String user,
				String password)
	{
		this.port = port;
		this.host = host;
		this.from = from;
		this.username = user;
		this.password = password;
	}
	
	/**
	 * 
	 * @param to destination user@example.com
	 * @param subject the subject of your message
	 * @param body the body of your message
	 */
	public void sendEmail(String to, String subject, String body) {
		Properties props = new Properties();
		
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);

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
					private PasswordAuthentication pa =
							new PasswordAuthentication(username,password);
					@Override
					public PasswordAuthentication getPasswordAuthentication(){
						return pa;
					}
				};
			}
			Session session = Session.getInstance(props,authen);
			session.setDebug(true);
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
