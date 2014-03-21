package symbol;

import java.util.*;

import ast.*;

public class Table {
	public Table parent;
	public Dictionary<String, Symbol> dict;

	public Table() {
		parent = null;
		dict = new Hashtable<String, Symbol>();
	}

	public Table(Table _parent) {
		parent = _parent;
		dict = new Hashtable<String, Symbol>();
	}

	public boolean hasDupe(Id id) {
		if (dict.get(id.str) != null) {
			System.err.println("Identifier define dupe");
			return (true);
		}
		return (false);
	}

	public boolean has(Id id) {
		if (dict.get(id.str) != null)
			return (true);
		if (parent == null)
			return (false);
		return (parent.has(id));
	}

	private Symbol get(Id id) {
		if (dict.get(id.str) != null)
			return (dict.get(id.str));
		if (parent == null)
			return (null);
		return (parent.get(id));
	}

	public void addVariable(Id id, Type type) throws Exception {
		if (hasDupe(id))
			throw new RuntimeException();
		dict.put(id.str, new SVariable(type));
	}

	public void addDefine(Id id, Type type) throws Exception {
		if (hasDupe(id))
			throw new RuntimeException();
		dict.put(id.str, new SDefine(type));
	}

	public void addFunction(Id id, Type type, Arg arg, boolean really)
			throws Exception {
		SFunction func = getFunction(id);
		if (func == null)
			dict.put(id.str, new SFunction(type, arg, really));
		else if (func.same(new Function(type, arg)) && func.really == false) {
			dict.remove(id.str);
			dict.put(id.str, new SFunction(type, arg, really));
		} else
			throw new RuntimeException();
	}

	public SVariable getVariable(Id id) {
		Symbol now = get(id);
		if (now == null || now.kind != Symbol.Kind.variable)
			return (null);
		return ((SVariable) now);
	}

	public Type getType(Id id) {
		Symbol now = get(id);
		if (now == null || now.kind != Symbol.Kind.typedef)
			return (null);
		return (((SDefine) now).type);
	}

	public SFunction getFunction(Id id) {
		Symbol now = get(id);
		if (now == null || now.kind != Symbol.Kind.function)
			return (null);
		return ((SFunction) now);
	}

	public void updateVariable(Id id, Object _value) {
		SVariable now = (SVariable) get(id);
		now.value = _value;
	}

	public void init() {
		// scanf
		SFunction temp = new SFunction();
		temp.type = new Type(Type.TypeName.Int);
		temp.list.add(new Pointer(new Type(Type.TypeName.Char)));
		temp.infinity = true;
		temp.really = true;
		dict.put("scanf", temp);
		// printf
		temp = new SFunction();
		temp.type = new Type(Type.TypeName.Int);
		temp.list.add(new Pointer(new Type(Type.TypeName.Char)));
		temp.infinity = true;
		temp.really = true;
		dict.put("printf", temp);
		// malloc
		temp = new SFunction();
		temp.type = new Pointer(new Type(Type.TypeName.Void));
		temp.list.add(new Type(Type.TypeName.Int));
		temp.infinity = false;
		temp.really = true;
		dict.put("malloc", temp);
		// NULL
		dict.put("NULL", new SVariable(new Type(Type.TypeName.Int)));
	}
}
