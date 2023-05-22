package kryptografiaInt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kryptografia.AES;
import kryptografia.OperationsWithFiles;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static kryptografia.OperationsWithFiles.readFileAES;
import static kryptografia.OperationsWithFiles.writeFileAES;

public class HelloController {
    @FXML
    private TextField textFieldKey;
    private Stage stage;
    @FXML
    private TextArea textAreaEncrypted, textAreaUnencrypted;
    private File wczytanyPlik, zapisanyPlik;
    private byte [] tekstWBajtach, keyWBajtach = null, szyfrWBajtach;
    private final AES aes = new AES();
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    protected void onGenerateKeyButtonClick() {
        keyWBajtach = aes.generateKey();
        textFieldKey.setText(new String(keyWBajtach));
    }

    @FXML
    protected void onLoadFileButtonClick() throws Exception {
        FileChooser fileChooser = new FileChooser();
        try {
            File file = fileChooser.showOpenDialog(stage);
            String sciezka = file.getPath();
            wczytanyPlik = new File(sciezka);
            tekstWBajtach = readFileAES(sciezka);
            textAreaUnencrypted.setText(new String(tekstWBajtach));
         }
        catch (Exception exception) {
            throw new Exception("File could not be read");
        }

    }
    @FXML
    protected void onSaveUnencryptedFileButtonClick() throws Exception {
        FileChooser fileChooser = new FileChooser();
        try {
            File file = fileChooser.showSaveDialog(stage);
            String sciezka = file.getPath();
            zapisanyPlik = new File(sciezka);
            writeFileAES(sciezka, textAreaUnencrypted.getText().getBytes());
        } catch (Exception exception) {
            throw new Exception("File could not be write");
        }
    }

    @FXML
    protected void onSaveFileButtonClick() throws Exception {
        FileChooser fileChooser = new FileChooser();
        try {
            File file = fileChooser.showSaveDialog(stage);
            String sciezka = file.getPath();
            zapisanyPlik = new File(sciezka);
            writeFileAES(sciezka, textAreaEncrypted.getText().getBytes());
        } catch (Exception exception) {
            throw new Exception("File could not be write");
        }
    }


    @FXML
    protected void onEncryptButtonClick() throws UnsupportedEncodingException {
        if (keyWBajtach == null) {
            onGenerateKeyButtonClick();
        }
        szyfrWBajtach = aes.encrypt(textAreaUnencrypted.getText().getBytes(), textFieldKey.getText().getBytes());
        textAreaEncrypted.setText(OperationsWithFiles.bytesToHex(szyfrWBajtach));
    }

    @FXML
    protected void onDecryptButtonClick() throws Exception {
        if (keyWBajtach == null) {
            throw new Exception("Podaj klucz");
        }

        tekstWBajtach = aes.decrypt(szyfrWBajtach, textFieldKey.getText().getBytes());
        textAreaUnencrypted.setText(new String(tekstWBajtach));
    }


}