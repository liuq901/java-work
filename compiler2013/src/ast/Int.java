package ast;

import java.io.PrintStream;

import symbol.Table;

public class Int extends Expr {
	public int value;

	public Int() {
		value = 0;
		kind = Node.Kind.Int;
	}

	public Int(int _value) {
		value = _value;
		kind = Node.Kind.Int;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		space(out, len + 1);
		out.println(value);
	}

	public void update(Table table) throws Exception {
		type = new Type(Type.TypeName.Int);
		lvalue = false;
	}
}
