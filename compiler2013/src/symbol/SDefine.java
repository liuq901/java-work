package symbol;

import ast.*;

public class SDefine extends Symbol {
	public Type type;

	public SDefine() {
		kind = Symbol.Kind.typedef;
	}

	public SDefine(Type _type) {
		kind = Symbol.Kind.typedef;
		type = _type;
	}
}
