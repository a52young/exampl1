import java.util.ArrayList;

public class Calculator {

	public int pick(ArrayList<Integer> v) {
		int pos_sum = 0;
		int neg_sum = 0;
		int n = v.size();

		for (int i = 0; i < n; i++) {

			if (v.get(i) >= -2 && v.get(i) <= 2) {
			} else {
				if (v.get(i) > 3) {
					pos_sum = maxInt(pos_sum, v.get(i));
				} else {
					neg_sum = minInt(neg_sum, v.get(i));
				}
			}
		}
		if (pos_sum > Math.abs(neg_sum))
			return pos_sum;
		else
			return Math.abs(neg_sum);
	}

	public ArrayList<Integer> init_set(ArrayList<Integer> v) {
		int n = v.size();

		for (int i = 0; i < n; i++) {

			if (v.get(i) >= -2 && v.get(i) <= 2)
				v.set(i, 0);
		}
		return v;
	}
	
	public ArrayList<Integer> init_set(int[] v) {
		int n = v.length;
		ArrayList<Integer> b = new ArrayList<>();
		
		for (int i = 0; i < n; i++) {

			if (v[i] >= -2 && v[i] <= 2)
				v[i] = 0;
			b.add(v[i]);
		}
		return b;
	}
	
	public ArrayList<Double> init_set(double[] v) {
		int n = v.length;
		ArrayList<Double> b = new ArrayList<>();
		
		for (int i = 0; i < n; i++) {

			if (v[i] >= -2.0 && v[i] <= 2.0)
				v[i] = 0.0;
			b.add(v[i]);
		}
		return b;
	}

	public int maxInt(int v, int n) {

		if (v > n)
			return v;
		else
			return n;

	}

	public int minInt(int v, int n) {
		if (v < n)
			return v;
		else
			return n;
	}

}