package ast;

import java.io.PrintStream;

public class StructUnion extends Type {
	public Id alias;
	public DeclList body;

	public StructUnion() {
		alias = new Id();
		body = new DeclList();
		kind = Node.Kind.Type;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		alias.print(out, len + 1);
		body.print(out, len + 1);
	}
}
