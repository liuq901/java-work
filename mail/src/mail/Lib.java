package mail;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import sun.misc.BASE64Encoder;

public class Lib {
	static Console console = System.console();
	static Socket socket = null;
	static BufferedReader in = null;
	static PrintWriter out = null;
	static BASE64Encoder encoder = new BASE64Encoder();

	static String next() {
		return console.readLine();
	}

	static int nextInt() {
		try {
			return Integer.parseInt(next());
		} catch (Exception e) {
			return -1;
		}
	}

	static String nextPassword() {
		return new String(console.readPassword());
	}

	static void print(String s) {
		console.writer().print(s);
		console.flush();
	}

	static void println(String s) {
		console.writer().println(s);
		console.flush();
	}

	static void println() {
		console.writer().println();
		console.flush();
	}

	static void error(String s) throws Exception {
		throw new Exception(s);
	}

	static void setSocket(String server, int port) throws Exception {
		try {
			socket = new Socket(server, port);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (Exception e) {
			error("Establish socket failed.");
		}
	}

	static String encode(String s) {
		return encoder.encode(s.getBytes());
	}

	static void send(String s) {
		out.println(s);
		out.flush();
	}

	static String receive() {
		try {
			return in.readLine();
		} catch (Exception e) {
			return "";
		}
	}

	static String receiveString() {
		return receive().split(" ")[0];
	}

	static int receiveInt() {
		try {
			return Integer.parseInt(receiveString());
		} catch (Exception e) {
			return -1;
		}
	}
}
