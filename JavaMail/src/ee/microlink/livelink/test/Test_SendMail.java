package ee.microlink.livelink.test;

import ee.microlink.livelink.mail.Sender;

public class Test_SendMail {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
		Sender sender = new ee.microlink.livelink.mail.Sender();
		
		String 	host = "mail.digitalmind.lv";
		String	to = "keit.kivisild@digitlamind.ee";
		String	from = "keit.kivisild@digitlamind.ee";
		String	subject = "subje";
		String 	body = "Test Reminder";
		String	files = "";
		int port = 25;
		
		sender.send(host, port, to, from, subject, body, files);
		
	}

}
