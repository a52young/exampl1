import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class byteArrayToCasting {
   public int[] changeInt(byte[] bytes) {
      int[] intArray = new int[bytes.length];
      for (int i = 0; i < bytes.length; i++) {
         // intArray[i] = bytes[i] & 0xFF; 부호 비트가 1인 음수 바이트 값을 부호비트가 0인 32비트로 만든다. 0xFF =
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