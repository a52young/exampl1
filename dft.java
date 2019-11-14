
import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;

public class dft {

	static int sample_size = 200;
	static ArrayList<Double> test = new ArrayList<>();
	long start, end;

	public void original() {
		System.out.println("<original>");

		for (int i = 0; i < sample_size; i++) {

			test.add(10.0 * Math.sin(Math.toRadians(2 * Math.PI * (sample_size / 2) * i)));
			System.out.print(test.get(i) + " ");
			if (i % 10 == 9) {
				System.out.println();
			}

		}

		ArrayList<Integer> copy = new ArrayList<>();

		System.out.println("<DFT>");
		for (int i = 0; i < sample_size; i++) {

			System.out.print(copy.get(i) + " ");
			if (i % 10 == 9) {
				System.out.println();
			}
		}
	}

	static double real_data[] = null;
	static double img_data[] = null;

	public ArrayList<Integer> dft(ArrayList<Integer> v) {
		int N = v.size();

		double _2Pik_over_N;
		double _2Pijk_over_N;
		double _2Pi_over_N = 2 * Math.PI / N;
		real_data = new double[N];
		img_data = new double[N];
		ArrayList<Integer> magnitude = new ArrayList<>();
		ArrayList<Complex> dft_result = new ArrayList<>();

		// System.out.println(N + "개 데이터의 DFT를 실행합니다. ");

		start = System.currentTimeMillis();

		for (int k = 0; k < N; k++) {

			_2Pik_over_N = _2Pi_over_N * k;

			for (int j = 0; j < N; j++) {

				_2Pijk_over_N = _2Pik_over_N * j;
				real_data[k] += v.get(j) * Math.cos(_2Pijk_over_N);
				img_data[k] -= v.get(j) * Math.sin(_2Pijk_over_N);

			}

			real_data[k] = real_data[k] / N;
			img_data[k] = img_data[k] / N;

			Complex dft = new Complex(real_data[k], img_data[k]);
			if (dft.abs() < 0.7) {
				magnitude.add(0);
			} else {
				magnitude.add((int) dft.abs());
			}
		}

		end = System.currentTimeMillis(); // 프로그램이 끝나는 시점 계산

		return magnitude;
	}

	public Double dft_runTime() {
		return (end - start) / 1000.0;
	}
}