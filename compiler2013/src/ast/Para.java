package ast;

import java.io.PrintStream;
import java.util.*;

public class Para extends Node {
	public List<Expr> para;

	public Para() {
		para = new ArrayList<Expr>();
		kind = Node.Kind.Para;
	}

	public void print(PrintStream out, int len) {
		super.print(out, len);
		for (Expr i : para)
			i.print(out, len + 1);
	}
}
