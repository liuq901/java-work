package ast;

import java.io.PrintStream;

public class Function extends Type {
	public Type type;
	public Arg arg;

	public Function() {
		type = new Type();
		arg = new Arg();
		name = Type.TypeName.Function;
		kind = Node.Kind.Type;
	}

	public Function(Type _type, Arg _arg) {
		type = _type;
		arg = _arg;
		name = Type.TypeName.Function;
		kind = Node.Kind.Type;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		type.print(out, len + 1);
		arg.print(out, len + 1);
	}
}
