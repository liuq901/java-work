package ast;

import java.io.PrintStream;

import symbol.*;

public class Sizeof extends Expr {
	public Type sizeType;

	public Sizeof() {
		sizeType = new Type();
		kind = Node.Kind.Sizeof;
	}

	public Sizeof(Type _type) {
		sizeType = _type;
		kind = Node.Kind.Sizeof;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		sizeType.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		type = new Type(Type.TypeName.Int);
		lvalue = false;
	}
}
