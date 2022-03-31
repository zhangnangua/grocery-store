public class ByteTest {
    public static void main(String[] args) {
        //        正二的三十一次方减一
        System.out.println(0x7FFFFFFF);
        //        负二的三十一次方    以为负0表示负数最小值 100..00 符号位不动其他取反加一 100..00 = 0X80000000
        System.out.println(0x80000000);
        //        负二的三十一次方+1    111..11 符号位不动其他取反加一 100..01 = 0X80000001
        System.out.println(0x80000001);
        //        负一    100..01 符号位不动其他取反加一 111..11 = 0xFFFFFFFF
        System.out.println(0xFFFFFFFF);
    }
}
