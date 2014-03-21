package ast;

import java.io.PrintStream;

import symbol.*;

public class Index extends Postfix {
	public Expr sub;

	public Index() {
		sub = new Expr();
		op = Postfix.OpType.Index;
		kind = Node.Kind.Postfix;
	}

	public Index(Expr _sub) {
		sub = _sub;
		op = Postfix.OpType.Index;
		kind = Node.Kind.Postfix;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		sub.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		expr.update(table);
		if (!isArray(expr.type))
			error("Index error");
		if (expr.type.name == Type.TypeName.Array) {
			type = ((Array) expr.type).type;
			lvalue = isLvalue(type);
		} else {
			type = ((Pointer) expr.type).type;
			lvalue = isLvalue(type);
		}
	}
}
