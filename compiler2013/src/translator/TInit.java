package translator;

public class TInit {
	public boolean init[];

	public TInit(int register) {
		init = new boolean[register];
		for (int i = 0; i < register; i++)
			init[i] = false;
	}

	public TInit(TInit a, int register) {
		init = new boolean[register];
		for (int i = 0; i < register; i++)
			init[i] = a.init[i];
	}
}
