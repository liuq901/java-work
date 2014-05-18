package mail;

public class Sender {
	void send(Information information) {
		try {
			Lib.setSocket(information.smtpServer, information.smtpPort);
			helo(information.smtpServer);
			authLogin(information.username, information.password);
			mailFrom(information.username);
			Lib.print("Please input the destination address: ");
			String destination = Lib.next();
			rcptTo(destination);
			data(information.username, destination);
			quit();
		} catch (Exception e) {
			if (e.getMessage() == null)
				e = new Exception("Unknown.");
			Lib.println("Error: " + e.getMessage());
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
		Lib.send(Lib.encode(password));
		if (Lib.receiveInt() != 235)
			Lib.error("Wrong password.");
	}

	void mailFrom(String username) throws Exception {
		Lib.send("mail from:<" + username + ">");
		if (Lib.receiveInt() != 250)
			Lib.error("Invalid source address.");
	}

	void rcptTo(String destination) throws Exception {
		Lib.send("rcpt to:<" + destination + ">");
		if (Lib.receiveInt() != 250)
			Lib.error("Invalid destination address.");
	}

	void data(String username, String destination) throws Exception {
		Lib.send("data");
		if (Lib.receiveInt() != 354)
			Lib.error("Can not send the content.");
		Lib.print("Please input the subject: ");
		String subject = Lib.next();
		Lib.send("From: " + username);
		Lib.send("To: " + destination);
		Lib.send("Subject: " + subject);
		Lib.send("");
		Lib.println("Please input your e-mail content, end with a line only contains a single dot.");
		while (true) {
			Lib.print("> ");
			String content = Lib.next();
			Lib.send(content);
			if (content.equals("."))
				break;
		}
		if (Lib.receiveInt() != 250)
			Lib.error("Failed to send the content.");
	}

	void quit() throws Exception {
		Lib.send("quit");
		if (Lib.receiveInt() != 221)
			Lib.error("Quit unsuccessfully.");
	}
}
