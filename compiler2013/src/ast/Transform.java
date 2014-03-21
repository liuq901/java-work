package ast;

import java.io.PrintStream;

import symbol.*;

public class Transform extends Prefix {
	public Type transType;

	public Transform() {
		op = Prefix.OpType.Transform;
		transType = new Type();
		kind = Node.Kind.Prefix;
	}

	public Transform(Expr _expr, Type _type) {
		super(Prefix.OpType.Transform, _expr);
		transType = _type;
		kind = Node.Kind.Prefix;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		transType.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		expr.update(table);
		type = transType;
		lvalue = expr.lvalue;
	}
}
