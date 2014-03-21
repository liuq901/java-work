package translator;

import java.io.*;

public class CMove extends Code {
	public int dest;
	public String from;

	public CMove(int _dest, String _from) {
		dest = _dest;
		from = _from;
		kind = Code.Kind.Move;
	}

	@Override
	public void print(PrintStream out) {
		out.println(blank("Move") + blank(reg(dest)) + from);
	}
}
