package translator;

import ast.*;
import java.io.*;

public class COp extends Code {
	public Op.OpType op;
	public int dest, first, second;

	public COp(Op.OpType _op, int _dest, int _first, int _second) {
		op = _op;
		dest = _dest;
		first = _first;
		second = _second;
		kind = Code.Kind.Op;
	}

	@Override
	public void print(PrintStream out) {
		out.println(blank(op.toString()) + blank(reg(dest)) + blank(reg(first))
				+ reg(second));
	}
}
