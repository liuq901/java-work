package ast;

import java.io.PrintStream;

import symbol.*;

public class Char extends Expr {
	public char ch;

	public Char() {
		ch = '\0';
		kind = Node.Kind.Char;
	}

	public Char(char _ch) {
		ch = _ch;
		kind = Node.Kind.Char;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		space(out, len + 1);
		out.println(ch);
	}

	public void update(Table table) throws Exception {
		type = new Type(Type.TypeName.Char);
		lvalue = false;
	}
}
