package ast;

import java.io.PrintStream;

import symbol.*;

public class Op extends Expr {
	public static enum OpType {
		Add, Sub, Mul, Div, Mod, LogAnd, LogOr, BitAnd, BitOr, BitXor, LShift, RShift, Less, Greater, LEqual, GEqual, Equal, NotEqual, Assign, AddAss, SubAss, MulAss, DivAss, ModAss, LShiftAss, RShiftAss, BitAndAss, BitOrAss, BitXorAss
	}

	public Expr left, right;
	public OpType op;

	public Op() {
		left = right = new Expr();
		kind = Node.Kind.Op;
	}

	public Op(Expr _left, OpType _op, Expr _right) {
		left = _left;
		right = _right;
		op = _op;
		kind = Node.Kind.Op;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		left.print(out, len + 1);
		space(out, len + 1);
		out.println(op);
		right.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		left.update(table);
		right.update(table);
		if (!OpUpdate())
			error("Expression error");
	}

	public boolean OpUpdate() {
		lvalue = false;
		if (isAdd() || isLog() || isCond()) {
			if (isPointer(left.type)
					&& (isInt(right.type) || isPointer(right.type))) {
				type = left.type;
				return (true);
			}
			if (isInt(left.type) && (isPointer(right.type))) {
				type = left.type;
				return (true);
			}
			if (isInt(left.type) && isInt(right.type)) {
				type = max(left.type, right.type);
				return (true);
			}
			return (false);
		}
		if (isSub()) {
			if (isPointer(left.type) && isInt(right.type)) {
				type = left.type;
				return (true);
			}
			if (isInt(left.type) && isInt(right.type)) {
				type = max(left.type, right.type);
				return (true);
			}
			return (false);
		}
		if (isMul() || isShift() || isBit()) {
			if (isInt(left.type) && isInt(right.type)) {
				type = max(left.type, right.type);
				return (true);
			}
			return (false);
		}
		if (!left.lvalue)
			return (false);
		type = left.type;
		return (left.type.same(right.type));
	}

	boolean isAdd() {
		return (op == OpType.Add);
	}

	boolean isSub() {
		return (op == OpType.Sub);
	}

	boolean isMul() {
		return (op == OpType.Mul || op == OpType.Div || op == OpType.Mod);
	}

	boolean isLog() {
		return (op == OpType.LogAnd || op == OpType.LogOr);
	}

	boolean isBit() {
		return (op == OpType.BitAnd || op == OpType.BitOr || op == OpType.BitXor);
	}

	boolean isShift() {
		return (op == OpType.LShift || op == OpType.RShift);
	}

	boolean isCond() {
		return (op == OpType.Less || op == OpType.Greater
				|| op == OpType.LEqual || op == OpType.GEqual
				|| op == OpType.Equal || op == OpType.NotEqual);
	}

	public boolean isAssign() {
		return (op == OpType.Assign || op == OpType.AddAss
				|| op == OpType.SubAss || op == OpType.MulAss
				|| op == OpType.DivAss || op == OpType.ModAss
				|| op == OpType.LShiftAss || op == OpType.RShiftAss
				|| op == OpType.BitAndAss || op == OpType.BitOrAss || op == OpType.BitXorAss);
	}

	public OpType changeAss() {
		if (op == OpType.AddAss)
			return (OpType.Add);
		else if (op == OpType.SubAss)
			return (OpType.Sub);
		else if (op == OpType.MulAss)
			return (OpType.Mul);
		else if (op == OpType.DivAss)
			return (OpType.Div);
		else if (op == OpType.ModAss)
			return (OpType.Mod);
		else if (op == OpType.LShiftAss)
			return (OpType.LShift);
		else if (op == OpType.RShiftAss)
			return (OpType.RShift);
		else if (op == OpType.BitAndAss)
			return (OpType.BitAnd);
		else if (op == OpType.BitOrAss)
			return (OpType.BitOr);
		else if (op == OpType.BitXorAss)
			return (OpType.BitXor);
		return (null);
	}

	Type max(Type a, Type b) {
		if (a.name == Type.TypeName.Int || b.name == Type.TypeName.Int)
			return (new Type(Type.TypeName.Int));
		return (new Type(Type.TypeName.Char));
	}
}
