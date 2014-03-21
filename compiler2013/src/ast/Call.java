package ast;

import java.io.PrintStream;
import java.util.*;

import symbol.*;

public class Call extends Postfix {
	public Para para;

	public Call() {
		para = new Para();
		op = Postfix.OpType.Call;
		kind = Node.Kind.Postfix;
	}

	public Call(Para _para) {
		para = _para;
		op = Postfix.OpType.Call;
		kind = Node.Kind.Postfix;
	}

	@Override
	public void print(PrintStream out, int len) {
		super.print(out, len);
		para.print(out, len + 1);
	}

	@Override
	public void update(Table table) throws Exception {
		expr.update(table);
		if (expr.kind != Node.Kind.Variable)
			error("Call function error");
		Variable name = (Variable) expr;
		SFunction func = table.getFunction(name.id);
		if (func == null)
			error("No such function");
		else {
			List<Type> temp = new ArrayList<Type>();
			for (Expr i : para.para) {
				i.update(table);
				temp.add(i.type);
			}
			if (!func.callable(temp))
				error("Can't callable");
		}
		type = func.type;
		lvalue = isPointer(type);
	}
}
