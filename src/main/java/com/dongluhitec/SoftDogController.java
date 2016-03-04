package com.dongluhitec;

import com.dongluhitec.core.crypto.appauth.AppAuthorization;
import com.dongluhitec.core.crypto.appauth.AppVerifier;
import com.dongluhitec.core.crypto.appauth.AppVerifierImpl;
import com.dongluhitec.core.crypto.softdog.SoftDogWin;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.pan.alert.AlertType;
import org.pan.alert.Alerts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by xiaopan on 2016-03-04.
 */
public class SoftDogController {
    final Logger LOGGER = LoggerFactory.getLogger(SoftDogController.class);
    @FXML public TextArea tx_snContent;
    @FXML public TextField txt_companyName;
    @FXML public TextField txt_projectName;
    @FXML public TextField txt_models;
    @FXML public Label messageLabel;
    @FXML public Label detailsLabel;
    @FXML public GridPane rootPane;

    private Date validateTo;

    private AppVerifier appVerifier = new AppVerifierImpl(new SoftDogWin());

    public void selectSnFile(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if(file == null){
            return;
        }
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(file.toURI()));
            this.tx_snContent.setText(new String(bytes, "utf-8"));
        } catch (IOException e) {
            Alerts.create(AlertType.ERROR).message("读取文件内容时发生错误，请重新选择一个新的注册码").show();
            return;
        }
        validateSn(this.tx_snContent.getText());
    }

    public void yes_onAction(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(1);
    }

    public void no_onAction(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(1);
    }

    private void validateSn(String tx_snContent) {
        try {
            AppAuthorization decrypt = appVerifier.decrypt(tx_snContent);
            this.txt_companyName.setText(decrypt.getCompanyName());
            this.txt_models.setText(Arrays.toString(decrypt.getAuthorizations()));
            this.txt_projectName.setText(decrypt.getProjectId());
            this.validateTo = decrypt.getDateOfExpire();

            LOGGER.info("手动验证注册码,公司名称:{} 项目名称:{} 授权模块:{} 有效期:{} 注册码:{}", this.txt_companyName.getText(),
                    this.txt_projectName.getText(), this.txt_models.getText(),
                    new SimpleDateFormat("yyyy-MM-dd").format(this.validateTo), this.tx_snContent.getText());
            Alerts.create(AlertType.INFO).message("检验注册码成功，请单击确定将当前信息保存到服务器数据库").show();
        }catch (Exception e){
            this.txt_companyName.setText("");
            this.txt_models.setText("");
            this.txt_projectName.setText("");
            this.validateTo = null;
            LOGGER.error("导入注册码验证失败",e);
            Alerts.create(AlertType.ERROR).message("配合加密狗解密失败，请检查加密狗与注册码是否正常").show();
        }
    }
}
