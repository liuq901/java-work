package ast;

import java.io.PrintStream;

public class Alias extends Type {
	public Id alias;

	public Alias() {
		alias = new Id();
		name = Type.TypeName.Alias;
		kind = Node.Kind.Type;
	}

	public Alias(Id _alias) {
		alias = _alias;
		name = Type.TypeName.Alias;
		kind = Node.Kind.Type;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		alias.print(out, len + 1);
	}
}
