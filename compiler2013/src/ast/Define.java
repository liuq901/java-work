package ast;

import java.io.PrintStream;

public class Define extends Type {
	public Type type;

	public Define() {
		type = new Type();
		name = Type.TypeName.Define;
		kind = Node.Kind.Type;
	}

	public Define(Type _type) {
		type = _type;
		name = Type.TypeName.Define;
		kind = Node.Kind.Type;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		type.print(out, len + 1);
	}
}
