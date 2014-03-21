package translator;

public class TVar {
	public int address;

	public TVar(int _address) {
		address = _address;
	}

	public TVar clone() {
		TVar ret = new TVar(address);
		return (ret);
	}

	public int size() {
		return (4);
	}
}
