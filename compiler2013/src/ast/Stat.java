package ast;

import java.io.PrintStream;

public class Stat extends Node {
	public boolean inLoop;
	public Type retType;

	public Stat() {
		kind = Node.Kind.Stat;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
	}

	public boolean isLvalue(Type type) {
		return (isInt(type) || isPointer(type) || isStruct(type));
	}
}
