package mail;

public class Main {
	public static void main(String args[]) {
		new Main().run();
	}

	void run() {
		Information information = new Information();
		Sender sender = new Sender();
		Receiver receiver = new Receiver();
		Lib.println();
		Lib.println("Welcome to use the simple e-mail client.");
		Lib.println();
		init(information);
		Lib.println();
		help();
		while (true) {
			Lib.println();
			Lib.print(">>> ");
			String command = Lib.next();
			if (command.equals("help"))
				help();
			else if (command.equals("init"))
				init(information);
			else if (command.equals("send"))
				sender.send(information);
			else if (command.equals("recv"))
				receiver.receive(information);
			else if (command.equals("exit"))
				break;
			else
				Lib.println("Unknown command.");
		}
	}

	void init(Information information) {
		Lib.println("Initialize the account information.");
		Lib.print("Please input your e-mail address: ");
		information.username = Lib.next();
		Lib.print("Please input your password: ");
		information.password = Lib.nextPassword();
		Lib.print("Please input the SMTP server: ");
		information.smtpServer = Lib.next();
		Lib.print("Please input the SMTP port number (default 25): ");
		information.smtpPort = Lib.nextInt();
		Lib.print("Please input the POP3 server: ");
		information.pop3Server = Lib.next();
		Lib.print("Please input the POP3 port number (default 110): ");
		information.pop3Port = Lib.nextInt();
	}

	void help() {
		Lib.println("You can use the following commands: help, init, send, recv, exit.");
		Lib.println("help: Show the help messages.");
		Lib.println("init: Initialize the account information.");
		Lib.println("send: Send an e-mail.");
		Lib.println("recv: Receive your e-mail.");
		Lib.println("exit: Quit the program.");
		Lib.println("If you got anything wrong, please check the Internet or initialize again.");
	}
}
