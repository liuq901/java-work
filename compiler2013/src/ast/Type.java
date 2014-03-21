package ast;

import java.io.PrintStream;

public class Type extends Node {
	public static enum TypeName {
		Str, Void, Char, Int, Struct, Union, Array, Function, Define, Pointer, Alias
	};

	public TypeName name;

	public Type() {
		kind = Node.Kind.Type;
	}

	public Type(TypeName _name) {
		kind = Node.Kind.Type;
		name = _name;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		space(out, len + 1);
		out.println(name);
	}

	public boolean same(Type type) {
		if (isInt(this))
			return (isInt(type) || isPointer(type));
		return (false);
	}
}
