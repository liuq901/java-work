package translator;

import java.io.*;

public class CUnary extends Code {
	public static enum OpType {
		Neg, BitNot, LogNot
	}

	public OpType op;
	public int pos;

	public CUnary(OpType _op, int _pos) {
		op = _op;
		pos = _pos;
		kind = Code.Kind.Unary;
	}

	@Override
	public void print(PrintStream out) {
		out.println(blank(op.toString()) + reg(pos));
	}
}
