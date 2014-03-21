package symbol;

import java.util.*;

import ast.*;

public class SFunction extends Symbol {
	public Type type;
	public List<Type> list;
	public boolean infinity, really;

	public SFunction() {
		kind = Symbol.Kind.function;
		type = new Type();
		list = new ArrayList<Type>();
	}

	public SFunction(Type _type, Arg arg, boolean _really) throws Exception {
		kind = Symbol.Kind.function;
		type = _type;
		list = new ArrayList<Type>();
		for (Var i : arg.list)
			list.add(i.type);
		infinity = arg.infinity;
		really = _really;
		if (list.isEmpty() && infinity) {
			System.err.println("Function Defination error");
			throw new RuntimeException();
		}
	}

	public boolean same(Function func) {
		if (!type.same(func.type))
			return (false);
		if (list.size() != func.arg.list.size())
			return (false);
		if (infinity != func.arg.infinity)
			return (false);
		for (int i = 0; i < list.size(); i++)
			if (!list.get(i).same(func.arg.list.get(i).type))
				return (false);
		return (true);
	}

	public boolean callable(List<Type> para) {
		if (para.size() < list.size())
			return (false);
		if (para.size() > list.size() && !infinity)
			return (false);
		for (int i = 0; i < list.size(); i++)
			if (!list.get(i).same(para.get(i)))
				return (false);
		return (true);
	}
}
