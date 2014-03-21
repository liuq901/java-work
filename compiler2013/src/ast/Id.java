package ast;

import java.io.PrintStream;

public class Id extends Node {
	public String str;

	public Id() {
		str = "";
		kind = Node.Kind.Id;
	}

	public Id(String _str) {
		str = _str;
		kind = Node.Kind.Id;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		space(out, len + 1);
		out.println(str);
	}
}
