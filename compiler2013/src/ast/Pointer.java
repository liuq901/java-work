package ast;

import java.io.PrintStream;

public class Pointer extends Type {
	public Type type;

	public Pointer() {
		type = new Type();
		name = Type.TypeName.Pointer;
		kind = Node.Kind.Type;
	}

	public Pointer(Type _type) {
		type = _type;
		name = Type.TypeName.Pointer;
		kind = Node.Kind.Type;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		type.print(out, len + 1);
	}

	@Override
	public boolean same(Type now) {
		if (now.name == Type.TypeName.Str)
			return (true);
		return (isPointer(now) || isInt(now) || isArray(now));
	}
}
