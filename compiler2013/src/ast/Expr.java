package ast;

import java.io.PrintStream;
import java.util.*;

import symbol.*;

public class Expr extends Stat {
	public List<Expr> expr;
	public Type type;
	public boolean lvalue;

	public Expr() {
		expr = new ArrayList<Expr>();
		kind = Node.Kind.Expr;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		for (Expr i : expr)
			i.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		for (Expr i : expr) {
			i.inLoop = inLoop;
			i.retType = retType;
			i.update(table);
		}
		if (expr.isEmpty())
			type = new Type(Type.TypeName.Void);
		else
			type = expr.get(0).type;
		if (expr.size() == 1)
			lvalue = expr.get(0).lvalue;
		else
			lvalue = false;
	}

	public boolean empty() {
		return (expr.isEmpty());
	}
}
