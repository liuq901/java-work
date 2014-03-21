package symbol;

import ast.*;

public class SVariable extends Symbol {
	public Type type;
	public Object value;

	public SVariable() {
		kind = Symbol.Kind.variable;
	}

	public SVariable(Type _type) {
		type = _type;
		kind = Symbol.Kind.variable;
	}
}
