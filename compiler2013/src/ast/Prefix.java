package ast;

import java.io.PrintStream;

import symbol.*;

public class Prefix extends Expr {
	public static enum OpType {
		Add, Sub, Transform, Sizeof, Address, Pointer, Positive, Negative, LogNot, BitNot
	}

	public OpType op;
	public Expr expr;

	public Prefix() {
		expr = new Expr();
		kind = Node.Kind.Prefix;
	}

	public Prefix(OpType _op, Expr _expr) {
		op = _op;
		expr = _expr;
		kind = Node.Kind.Prefix;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		space(out, len + 1);
		out.println(op);
		expr.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		expr.update(table);
		if (op == OpType.Add || op == OpType.Sub || op == OpType.LogNot) {
			if (!isInt(expr.type) && !isPointer(expr.type))
				error("Prefix error");
			type = expr.type;
			lvalue = false;
		} else if (op == OpType.Positive || op == OpType.Negative
				|| op == OpType.BitNot) {
			if (!isInt(expr.type))
				error("Prefix error");
			type = expr.type;
			lvalue = false;
		} else if (op == OpType.Pointer) {
			if (!isPointer(expr.type))
				error("Prefix error");
			type = ((Pointer) expr.type).type;
			lvalue = isLvalue(type);
		} else if (op == OpType.Address) {
			if (expr.kind != Node.Kind.Variable)
				error("Prefix error");
			type = new Pointer(expr.type);
			lvalue = false;
		} else if (op == OpType.Sizeof) {
			type = new Type(Type.TypeName.Int);
			lvalue = false;
		} else
			error("Prefix error");
	}
}
