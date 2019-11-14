import java.util.ArrayList;

public class autocorrelation {

	static byteArrayToCasting toInt = new byteArrayToCasting();
	static ArrayList<Integer> before_signal = new ArrayList<>();
	static boolean list_count = true;
	long start, end;
	public int max_auco;

	public ArrayList<Integer> auco(byte[] v) {
		ArrayList<Integer> result = new ArrayList<>();
		int[] input = new int[v.length];
		int N = v.length;
		input = toInt.changeInt(v);
		/*
		 * for (int i = 0; i < N; i++) { input[i] = v.get(i); }
		 */
		max_auco = 0;
		start = System.currentTimeMillis();

		if (list_count) { // 초기에 before_signal에 신호가없을 때 한번만 실행

			for (int i = 0; i < N; i++) { // 배열에 대입

				before_signal.add(input[i]);
			}
			list_count = false; // 모두 동작하고 빠져나가면서 list_count를 false로 만들어 다시 동작하지 않게함
			// System.out.println("before_signal : " + before_signal);
			result.add(0);
			return result; // 이 if문을 실행하였을 경우 0을 결과로 출력
		}

		if (!list_count) { // list_count가 false라는건 위의 코드가 실행되어서 before_signal에 신호가
			// 저장되었음을 의미하므로 false가 되었을경우 if문이 동작하게함

			try {

				result = new ArrayList<>(); // 결과 ArrayList 초기화

				for (int i = 0; i < 2 * N + 1; i++) {

					result.add(0); // ArrayList result 2*N+1 크기만큼 0으로 초기화

				}

				for (int i = 0; i < N; i++) {
					int front_count = N;

					for (int j = i; j < N; j++) { // 0 은 항상 0, 1부터 N까지 autocorrelation 계산

						result.set(front_count, result.get(front_count) + before_signal.get(i) * input[j]);
						if (max_auco < result.get(front_count) + before_signal.get(i) * input[j]) {
							max_auco = result.get(front_count) + before_signal.get(i) * input[j];
						}
						front_count--;
					}

					int rear_count = N + 1;

					for (int j = i + 1; j < N; j++) { // N부터 2N까지 autocorrelation 계산, 2N+1 은항상 0

						result.set(rear_count, result.get(rear_count) + before_signal.get(j) * input[i]);
						if (max_auco < result.get(rear_count) + before_signal.get(i) * input[j]) {
							max_auco = result.get(rear_count) + before_signal.get(i) * input[j];
						}
						rear_count++;
					}

				}

				for (int i = 0; i < N; i++) { // 실행이 끝난후 input을 before_signal에 저장

					before_signal.set(i, input[i]);
				}
			}

			catch (Exception e) {
				System.out.println("오류 발생 : " + e);
			}
		}
		/*
		 * System.out.println("결과 : " + result); System.out.println("before_signal : " +
		 * before_signal);
		 */
		end = System.currentTimeMillis(); // 프로그램이 끝나는 시점 계산
		return result;
	}

	public int auco_max() {
		return max_auco;
	}

	public Double auco_runTime() {
		return (end - start) / 1000.0;
	}
}