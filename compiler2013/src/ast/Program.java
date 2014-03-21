package ast;

import java.io.PrintStream;
import java.util.*;

import symbol.*;

public class Program extends Node {
	public List<Node> global;

	public Program() {
		global = new ArrayList<Node>();
		kind = Kind.Program;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		for (Node i : global)
			i.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		for (Node i : global)
			i.update(table);
		Enumeration<Symbol> temp = table.dict.elements();
		for (; temp.hasMoreElements();) {
			Symbol i = temp.nextElement();
			if (i.kind == Symbol.Kind.function && !((SFunction) i).really)
				error("Not defined function");
		}
	}
}
