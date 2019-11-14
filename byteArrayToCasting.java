import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class byteArrayToCasting {
   public int[] changeInt(byte[] bytes) {
      int[] intArray = new int[bytes.length];
      for (int i = 0; i < bytes.length; i++) {
         // intArray[i] = bytes[i] & 0xFF; ��ȣ ��Ʈ�� 1�� ���� ����Ʈ ���� ��ȣ��Ʈ�� 0�� 32��Ʈ�� �����. 0xFF =
         // 255
         intArray[i] = (int) bytes[i];
      }
      return intArray;
   }

   public double[] changeDouble(byte[] bytes) {
      double[] doubleArray = new double[bytes.length];
      for (int i = 0; i < bytes.length; i++) {
         doubleArray[i] = (double) bytes[i];
      }
      return doubleArray;
   }
}