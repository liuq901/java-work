package ast;

import java.io.PrintStream;

import symbol.*;

public class Terminal extends Init {
	public Expr expr;

	public Terminal() {
		expr = new Expr();
		kind = Node.Kind.Terminal;
	}

	public Terminal(Expr _expr) {
		super();
		expr = _expr;
		kind = Node.Kind.Terminal;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		expr.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		expr.update(table);
	}
}
