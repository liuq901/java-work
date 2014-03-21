package translator;

import java.util.*;

public class Address {
	public Dictionary<String, TVar> a;
	public Address prev;

	public Address() {
		a = new Hashtable<String, TVar>();
		prev = null;
	}
}
