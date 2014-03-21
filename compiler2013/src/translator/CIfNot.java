package translator;

import java.io.*;

public class CIfNot extends Code {
	public int cond, pos;

	public CIfNot(int _cond) {
		cond = _cond;
		kind = Code.Kind.IfNot;
	}

	public CIfNot(int _cond, int _pos) {
		cond = _cond;
		pos = _pos;
		kind = Code.Kind.IfNot;
	}

	@Override
	public void print(PrintStream out) {
		out.println(blank("IfNot") + blank(reg(cond)) + pos);
	}
}
