package translator;

import java.util.*;

public class TArray extends TVar {
	public List<Integer> p;

	public TArray(List<Integer> _p, int _address) {
		super(_address);
		p = _p;
	}

	public TArray clone() {
		TArray ret = new TArray(p, address);
		return (ret);
	}

	@Override
	public int size() {
		int ret = 4;
		for (int i = 0; i < p.size(); i++)
			ret *= p.get(i);
		return (ret);
	}
}
