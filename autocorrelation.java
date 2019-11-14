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

		if (list_count) { // �ʱ⿡ before_signal�� ��ȣ������ �� �ѹ��� ����

			for (int i = 0; i < N; i++) { // �迭�� ����

				before_signal.add(input[i]);
			}
			list_count = false; // ��� �����ϰ� ���������鼭 list_count�� false�� ����� �ٽ� �������� �ʰ���
			// System.out.println("before_signal : " + before_signal);
			result.add(0);
			return result; // �� if���� �����Ͽ��� ��� 0�� ����� ���
		}

		if (!list_count) { // list_count�� false��°� ���� �ڵ尡 ����Ǿ before_signal�� ��ȣ��
			// ����Ǿ����� �ǹ��ϹǷ� false�� �Ǿ������ if���� �����ϰ���

			try {

				result = new ArrayList<>(); // ��� ArrayList �ʱ�ȭ

				for (int i = 0; i < 2 * N + 1; i++) {

					result.add(0); // ArrayList result 2*N+1 ũ�⸸ŭ 0���� �ʱ�ȭ

				}

				for (int i = 0; i < N; i++) {
					int front_count = N;

					for (int j = i; j < N; j++) { // 0 �� �׻� 0, 1���� N���� autocorrelation ���

						result.set(front_count, result.get(front_count) + before_signal.get(i) * input[j]);
						if (max_auco < result.get(front_count) + before_signal.get(i) * input[j]) {
							max_auco = result.get(front_count) + before_signal.get(i) * input[j];
						}
						front_count--;
					}

					int rear_count = N + 1;

					for (int j = i + 1; j < N; j++) { // N���� 2N���� autocorrelation ���, 2N+1 ���׻� 0

						result.set(rear_count, result.get(rear_count) + before_signal.get(j) * input[i]);
						if (max_auco < result.get(rear_count) + before_signal.get(i) * input[j]) {
							max_auco = result.get(rear_count) + before_signal.get(i) * input[j];
						}
						rear_count++;
					}

				}

				for (int i = 0; i < N; i++) { // ������ ������ input�� before_signal�� ����

					before_signal.set(i, input[i]);
				}
			}

			catch (Exception e) {
				System.out.println("���� �߻� : " + e);
			}
		}
		/*
		 * System.out.println("��� : " + result); System.out.println("before_signal : " +
		 * before_signal);
		 */
		end = System.currentTimeMillis(); // ���α׷��� ������ ���� ���
		return result;
	}

	public int auco_max() {
		return max_auco;
	}

	public Double auco_runTime() {
		return (end - start) / 1000.0;
	}
}