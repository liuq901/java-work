package ast;

import java.io.PrintStream;

public class Array extends Type {
	public Type type;
	public Expr expr;

	public Array() {
		type = new Type();
		expr = new Expr();
		name = Type.TypeName.Array;
		kind = Node.Kind.Type;
	}

	public Array(Type _type, Expr _expr) {
		type = _type;
		expr = _expr;
		name = Type.TypeName.Array;
		kind = Node.Kind.Type;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		type.print(out, len + 1);
		expr.print(out, len + 1);
	}

	@Override
	public boolean same(Type temp) {
		if (temp.name == Type.TypeName.Pointer)
			return (type.equals(((Pointer) temp).type));
		if (temp.name == Type.TypeName.Array)
			return (type.equals(((Array) temp).type));
		return (false);
	}
}
