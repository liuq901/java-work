package translator;

import java.io.*;

public class CLoad extends Code {
	public int dest;
	public String add;

	public CLoad(int _dest, String _add) {
		dest = _dest;
		add = _add;
		kind = Code.Kind.Load;
	}

	@Override
	public void print(PrintStream out) {
		out.println(blank("Load") + blank(reg(dest)) + add);
	}
}
