package ast;

import java.io.PrintStream;

public class Arg extends DeclList {
	public boolean infinity;

	public Arg() {
		infinity = false;
		kind = Node.Kind.Arg;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		if (infinity) {
			space(out, len + 1);
			out.println("...");
		}
	}
}
