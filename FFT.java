import java.util.ArrayList;

import org.apache.commons.math3.complex.Complex;

public class FFT {
	int n, m;

	ArrayList<Double> ree = new ArrayList<>();
	ArrayList<Double> imm = new ArrayList<>();
	ArrayList<Integer> fft_abs = new ArrayList<>();
	ArrayList<Complex> complex = new ArrayList<>();

	double[] cos, sin;
	long end, start;

	public FFT(int n) {
		this.n = n;
		this.m = (int) (Math.log(n) / Math.log(2));

		if (n != (1 << m))
			throw new RuntimeException("FFT length must be power of 2"); // 쿨리튜키 알고리즘을 사용하기 위해 N이 2의 거듭제곱임을 확인.

		cos = new double[n / 2];
		sin = new double[n / 2];

		for (int i = 0; i < n / 2; i++) {
			cos[i] = Math.cos(-2 * Math.PI * i / n); // 실수부
			sin[i] = Math.sin(-2 * Math.PI * i / n); // 허수부
//         System.out.println(sin[i]);
		}

	}

	public ArrayList<Integer> fft(ArrayList<Double> r) {
		fft_abs.clear();
		ree.clear();
		imm.clear();
		complex.clear();

		int i, j, k, n1, n2, a;
		double c, s, t1, t2;
		double[] re = new double[r.size()];
		
		for (int x = 0; x < r.size(); x++) {
			re[x] = r.get(x);
		}

		double[] im = new double[re.length];

		for (i = 0; i < re.length; i++) {
			im[i] = 0;
		}

		j = 0;
		n2 = n / 2;

		start = System.currentTimeMillis();

		for (i = 1; i < n - 1; i++) {
			n1 = n2;
			while (j >= n1) {
				j -= n1;
				n1 = n1 / 2;
			}
			j += n1;

			if (i < j) {
				t1 = re[i];
				re[i] = re[j];
				re[j] = t1;
				t1 = im[i];
				im[i] = im[j];
				im[j] = t1;
			}
		}

		n1 = 0;
		n2 = 1;

		// Cooley-Tukey Algorithm
		for (i = 0; i < m; i++) {
			n1 = n2;
			n2 = n2 + n2;
			a = 0;

			for (j = 0; j < n1; j++) {
				c = cos[a];
				s = sin[a];
				a += 1 << (m - i - 1);

				for (k = j; k < n; k = k + n2) {
					t1 = c * re[k + n1] - s * im[k + n1];
					t2 = s * re[k + n1] + c * im[k + n1];
					re[k + n1] = re[k] - t1;
					im[k + n1] = im[k] - t2;
					re[k] = re[k] + t1;
					im[k] = im[k] + t2;
				}
			}
		}

		for (i = 0; i < re.length; i++) {
			ree.add(((re[i] * 1000) / 1000.0));
			imm.add(((im[i] * 1000) / 1000.0));

			complex.add(new Complex(ree.get(i), imm.get(i)));

			if (complex.get(i).abs() < 1000) {
				fft_abs.add(0);
			} else {
				fft_abs.add((int) complex.get(i).abs());
			}
		}
		// 시작하는 시점 계산

		/*
		 * 실행시간을 측정하고싶은 코드
		 */

		end = System.currentTimeMillis(); // 프로그램이 끝나는 시점 계산
		int empty = 0;
		
		for (i = 0; i < fft_abs.size(); i++) {
			if (empty < fft_abs.get(i)) {
				empty = fft_abs.get(i);
			}
		}

		//System.out.println("fft 주파수는 : " + empty);
		return fft_abs;

	}

	public Double fft_runTime() {
		return (end - start) / 1000.0;
	}

	public static void main(String[] args) {
		int N = 1024;

		FFT fft = new FFT(N);

		/*
		 * double[] re = new double[N]; double[] im = new double[N];
		 * 
		 * re[0] = 1; im[0] = 0; // impulse for (int i = 1; i < N; i++) { re[i] = im[i]
		 * = 0; } System.out.println("<impulse>"); beforeAfter(fft, re, im); // nyquist
		 * for (int i = 0; i < N; i++) { re[i] = Math.pow(-1, i); im[i] = 0; }
		 * System.out.println("<nyquist>"); beforeAfter(fft, re, im); // single cos for
		 * (int i = 0; i < N; i++) { re[i] = Math.cos(2 * Math.PI * i / N); im[i] = 0; }
		 * System.out.println("<single cos>"); beforeAfter(fft, re, im); // single sin
		 * for (int i = 0; i < N; i++) { re[i] = Math.sin(2 * Math.PI * i / N); im[i] =
		 * 0; } System.out.println("<single sin>"); beforeAfter(fft, re, im); // ramp
		 * for (int i = 0; i < N; i++) { re[i] = i; im[i] = 0; }
		 * System.out.println("<ramp>"); beforeAfter(fft, re, im);
		 */

		long time = System.currentTimeMillis();
		double iter = 30000;
		/*
		 * for (int i = 0; i < iter; i++) { fft.fft(re, im); }
		 */

		time = System.currentTimeMillis() - time;
		System.out.println("Averaged " + (time / iter) + "ms per iteration");
	}

	public ArrayList<Integer> get() {

		return fft_abs;
	}

	protected static void beforeAfter(FFT fft, double[] re, double[] im) {
		System.out.println("Befor : ");
		printReIm(re, im);
		// fft.fft(re, im);
		System.out.println("After : ");
		printReIm(re, im);
	}

	protected static void printReIm(double[] re, double[] im) {
		System.out.print("Re: [ ");
		for (int i = 0; i < re.length; i++) {
			System.out.print(((int) (re[i] * 1000) / 1000.0) + " ");
		}

		System.out.print("]\nIm: [ ");
		for (int i = 0; i < im.length; i++) {
			System.out.print(((int) (im[i] * 1000) / 1000.0) + " ");
		}
		System.out.println("]");
	}
}