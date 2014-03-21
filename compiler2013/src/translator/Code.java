package translator;

import java.io.*;

public class Code {
	public static enum Kind {
		FuncRet, Jump, IfNot, Save, Move, Load, Op, Unary
	};

	public Kind kind;

	public String reg(int i) {
		return ("R" + i);
	}

	public String blank(String s) {
		String ret = s;
		for (int i = s.length(); i < 10; i++)
			ret += " ";
		return (ret);
	}

	public void print(PrintStream out) {
	}
}
