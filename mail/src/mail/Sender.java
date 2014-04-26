package mail;

public class Sender {
	void send(Information information) {
		try {
			Lib.setSocket(information.smtpServer, information.smtpPort);
			helo(information.smtpServer);
			authLogin(information.username, information.password);
			mailFrom(information.username);
		} catch (Exception e) {
			Lib.println("Error: " + e.getMessage());
			Lib.println();
		}
	}

	void helo(String smtpServer) throws Exception {
		if (Lib.receiveInt() != 220)
			Lib.error("Can not connect to server.");
		Lib.send("helo " + smtpServer);
		if (Lib.receiveInt() != 250)
			Lib.error("Can not connect to SMTP server.");
	}

	void authLogin(String username, String password) throws Exception {
		Lib.send("auth login");
		if (Lib.receiveInt() != 334)
			Lib.error("Authentication failed.");
		Lib.send(Lib.encode(username));
		if (Lib.receiveInt() != 334)
			Lib.error("Invalid username.");
	}
	
	void mailFrom(String username) throws Exception
	{
		Lib.send("mail from:<"+username+">");
		if (Lib.receiveInt() != 250)
			Lib.error("Invalid source address.");
}
