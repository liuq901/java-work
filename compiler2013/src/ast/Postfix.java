package ast;

import java.io.PrintStream;

import symbol.*;

public class Postfix extends Expr {
	public static enum OpType {
		Add, Sub, Index, Call, Get
	}

	public OpType op;
	public Expr expr;

	public Postfix() {
		expr = new Expr();
		kind = Node.Kind.Postfix;
	}

	public Postfix(OpType _op) {
		expr = new Expr();
		op = _op;
		kind = Node.Kind.Postfix;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		expr.print(out, len + 1);
		space(out, len + 1);
		out.println(op);
	}

	@Override
	public void update(Table table) throws Exception {
		expr.update(table);
		if ((!isInt(expr.type) && !isPointer(expr.type)) || !expr.lvalue)
			error("Can't ++");
		type = expr.type;
		lvalue = false;
	}
}
