package ast;

import java.io.PrintStream;

import symbol.*;

public class If extends Stat {
	public Expr expr;
	public Stat Then, Else;

	public If() {
		expr = new Expr();
		Then = Else = new Stat();
		kind = Node.Kind.If;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		expr.print(out, len + 1);
		Then.print(out, len + 1);
		Else.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		expr.update(table);
		Then.inLoop = Else.inLoop = inLoop;
		Then.retType = Else.retType = retType;
		Then.update(table);
		Else.update(table);
		if (!isInt(expr.type))
			error("Can't determine for");
	}
}
