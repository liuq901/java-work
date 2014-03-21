package ast;

import java.io.PrintStream;

import symbol.*;

public class Jumps extends Stat {
	public static enum JumpType {
		Break, Continue, Return
	}

	public JumpType type;
	public Expr expr;

	public Jumps() {
		expr = new Expr();
		kind = Node.Kind.Jumps;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		space(out, len + 1);
		out.println(type);
		expr.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		if ((type == JumpType.Break || type == JumpType.Continue)
				&& inLoop == false)
			error("Break and continue must in loop");
		if (type == JumpType.Return) {
			expr.update(table);
			if (!expr.type.same(retType))
				error("Return type error");
		}
	}
}
