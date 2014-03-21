package translator;

public class TStruct extends TVar {
	StructBody body;

	public TStruct(StructBody _body, int address) {
		super(address);
		body = _body;
	}

	public int get(String str) {
		return (body.body.get(str).address);
	}

	@Override
	public int size() {
		return (body.size());
	}
}
