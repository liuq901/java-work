package translator;

import java.io.*;

public class CFuncRet extends Code {
	public CFuncRet() {
		kind = Code.Kind.FuncRet;
	}

	@Override
	public void print(PrintStream out) {
		out.println("FuncRet");
	}
}
