package ast;

import java.io.PrintStream;

import symbol.*;

public class For extends Stat {
	public Expr init, expr, iter;
	public Stat stat;

	public For() {
		init = expr = iter = new Expr();
		stat = new Expr();
		kind = Node.Kind.For;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		init.print(out, len + 1);
		expr.print(out, len + 1);
		iter.print(out, len + 1);
		stat.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		init.update(table);
		expr.update(table);
		iter.update(table);
		stat.inLoop = true;
		stat.retType = retType;
		stat.update(table);
		if (!isInt(expr.type))
			error("Can't determine for");
	}
}
