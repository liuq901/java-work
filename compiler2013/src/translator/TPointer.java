package translator;

import ast.*;

public class TPointer extends TVar {
	Type type;

	public TPointer(Type _type, int address) {
		super(address);
		type = _type;
	}
}
