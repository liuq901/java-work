package ast;

import java.io.PrintStream;
import java.util.*;

import symbol.*;

public class Init extends Node {
	public List<Init> son;

	public Init() {
		son = new ArrayList<Init>();
		kind = Node.Kind.Init;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		for (Init i : son)
			i.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		for (Init i : son)
			i.update(table);
	}
}
