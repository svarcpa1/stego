import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {

        CryptoLSB cryptoLSB = new CryptoLSB();
        CryptoDCT cryptoDCT = new CryptoDCT();
        int sourceMode = 1;

        //squares
        Path pathSource = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\lenna.png");
        Path pathDecode = Paths.get("C:\\_DATA-local\\WS_Java\\stego\\lenna_output.png");

        URL url = new URL("https://www.biggmagg.cz/system/newsitems/perexes/000/007/443/article" +
                "/DHsDCSMzQr2VeCg0KaNvxg.jpg?1561098480");

        String textToHide = "Michal je chytrý kluk jako buk";
        //cryptoLSB.code(pathSource, textToHide, sourceMode);
        //crypto.codeURL(url, textToHide, sourceMode);
        //System.out.println(cryptoLSB.decode(pathDecode));

        cryptoDCT.codeDCT(pathSource);
    }
}
