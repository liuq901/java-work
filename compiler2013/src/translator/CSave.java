package translator;

import java.io.*;

public class CSave extends Code {
	public String add, value;

	public CSave(String _add, String _value) {
		add = _add;
		value = _value;
		kind = Code.Kind.Save;
	}

	@Override
	public void print(PrintStream out) {
		out.println(blank("Save") + blank(add) + value);
	}
}
