package mail;

public class Receiver {
	void receive(Information information) {
		try {
			Lib.setSocket(information.pop3Server, information.pop3Port);
			login(information.username, information.password);
			int total = stat();
			Lib.println("You have " + total + " mail(s).");
			Lib.print("Input the mail number you want to receive: ");
			int number = Lib.nextInt();
			retr(number);
			quit();
		} catch (Exception e) {
			if (e.getMessage() == null)
				e = new Exception("Unknown.");
			Lib.println("Error: " + e.getMessage());
		}
	}

	void login(String username, String password) throws Exception {
		if (!Lib.receiveString()[0].equals("+OK"))
			Lib.error("Can not connect to server.");
		Lib.send("user " + username);
		if (!Lib.receiveString()[0].equals("+OK"))
			Lib.error("Invalid username.");
		Lib.send("pass " + password);
		if (!Lib.receiveString()[0].equals("+OK"))
			Lib.error("Wrong password.");
	}

	int stat() throws Exception {
		Lib.send("stat");
		String[] tmp = Lib.receiveString();
		if (!tmp[0].equals("+OK"))
			Lib.error("Failed to get mail count");
		return Integer.parseInt(tmp[1]);
	}

	void retr(int number) throws Exception {
		Lib.send("retr " + number);
		if (!Lib.receiveString()[0].equals("+OK"))
			Lib.error("Invalid mail number.");
		boolean body = false;
		while (true) {
			String now = Lib.receive();
			if (now.equals("."))
				break;
			if (now.isEmpty())
				body = true;
			if (!body) {
				String tmp = now.split(" ")[0].toLowerCase();
				if (tmp.equals("from:") || tmp.equals("to:")
						|| tmp.equals("subject:") || tmp.equals("date:"))
					Lib.println(now);
			}
			if (body)
				Lib.println(now);
		}
	}

	void quit() throws Exception {
		Lib.send("quit");
		if (!Lib.receiveString()[0].equals("+OK"))
			Lib.error("Quit unsuccessfully.");
	}
}
