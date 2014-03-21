package ast;

import java.io.PrintStream;

public class Var extends Node {
	public Type type;
	public Id name;
	public Init init;

	public Var() {
		type = new Type();
		name = new Id();
		init = new Init();
		kind = Node.Kind.Var;
	}

	public Var(Type _type, Id _name, Init _init) {
		type = _type;
		name = _name;
		init = _init;
		kind = Node.Kind.Var;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		type.print(out, len + 1);
		name.print(out, len + 1);
		init.print(out, len + 1);
	}
}
