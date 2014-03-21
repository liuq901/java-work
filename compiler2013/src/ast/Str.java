package ast;

import java.io.PrintStream;

import symbol.Table;

public class Str extends Expr {
	public String str;

	public Str() {
		str = "";
		kind = Node.Kind.Str;
	}

	public Str(String _str) {
		str = _str;
		kind = Node.Kind.Str;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		space(out, len + 1);
		out.println(str);
	}

	public void update(Table table) throws Exception {
		type = new Type(Type.TypeName.Str);
		lvalue = false;
	}
}
