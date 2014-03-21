package translator;

import java.io.*;

public class CJump extends Code {
	public int pos;

	public CJump() {
		kind = Code.Kind.Jump;
	}

	public CJump(int _pos) {
		pos = _pos;
		kind = Code.Kind.Jump;
	}

	@Override
	public void print(PrintStream out) {
		out.println(blank("Jump") + pos);
	}
}
