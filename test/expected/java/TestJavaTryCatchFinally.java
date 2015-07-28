public class TestJavaTryCatchFinally {

    public static void main(String[] args) {
        try {
            System.out.println("Hello world!");
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Done");
        }
    }
}